import java.net.{DatagramPacket, DatagramSocket, InetAddress}

import CSVcontrol.CSVReader
import commands.Constants._
import commands.{Command, ConsoleOperator}
import dao.inmemory.InMemoryWordDao
import util.Serialization._

import scala.collection.mutable
import scala.concurrent.ExecutionContext

class ConsoleServer(pathToStart: String,
                    pathToSave: String,
                    port: Int)
                   (implicit ec: ExecutionContext)
  extends ConsoleOperator(pathToStart, pathToSave) {

  import ConsoleOperator._

  private val socket: DatagramSocket = new DatagramSocket(port)
  socket.setReuseAddress(true)
  private var running: Boolean = false
  private var address: InetAddress = _
  private var clientPort: Int = _
  private val buf: Array[Byte] = new Array[Byte](BUFFER_SIZE)

  def receive[T]: T = {
    val packet: DatagramPacket = new DatagramPacket(buf, buf.length)
    socket.receive(packet)
    address = packet.getAddress
    clientPort = packet.getPort
    deSerialise(buf)
  }

  def send[T](entity: T): Unit = {
    val out: Array[Byte] = entity
    val packet: DatagramPacket = new DatagramPacket(out, out.length, address, clientPort)
    socket.send(packet)
  }

  override def run(): Unit = {
    running = true

    val csv: CSVReader = new CSVReader(this.pathToStart)
    val csvInput: Array[Array[String]] = csv.readAll
    val words: Array[String] = csvInput.map(_ (0))
    val sentence = new InMemoryWordDao
    sentence.insert(words.toSeq)
    implicit var curText: (InMemoryWordDao, METAINFO) = (sentence, (System.currentTimeMillis(), mutable.Buffer("Started")))

    while (running) {
      val command: Command = this.receive[Command]

      val stepResult: (String, (InMemoryWordDao, METAINFO)) = consoleUIStep(command)
      val newText = stepResult._2
      newText._2._2.append(stepResult._1)
      curText = newText
      running = !stepResult._1.equals("Exit command")

      this.send[Seq[String]](super.getPrintBuffer())
    }

    super.writeResponse("Shut down.")
    super.writeResponse("Log: ")
    curText._2._2.foreach(command => super.writeResponse("   " + command))
    this.endSession
    this.send[Seq[String]](super.getPrintBuffer())

    socket.close()
  }
}
