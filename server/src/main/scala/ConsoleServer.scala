import java.net.{DatagramPacket, DatagramSocket, InetAddress}
import java.util.UUID

import CSVcontrol.CSVReader
import commands.Constants._
import commands.{Command, ConsoleOperator}
import dao._
import domain.Word
import util.Serialization._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class ConsoleServer(pathToStart: String,
                    pathToSave: String,
                    port: Int)
                   (implicit ec: ExecutionContext,
                    users: UserDao,
                    words: WordDao)
  extends ConsoleOperator(pathToStart, pathToSave) with Runnable{

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

  def initText(userId: UUID): Seq[Word] = {
    val csv: CSVReader = new CSVReader(this.pathToStart)
    val csvInput: Array[Array[String]] = csv.readAll
    val words: Seq[Word] = Word.makeWords(csvInput.map(_.head).toSeq, 0, userId)
    words
  }

  override def run(): Unit = {
    running = true

    while (running) { // TODO Registration
      val command: Command = this.receive[Command]

      val stepResult: Future[String] = consoleUIStep(command)

      stepResult.map{message =>
//        running = message equals "Exit command"
//        this.send[Seq[String]](super.getPrintBuffer())
        this.send[String](message)
//        if (message equals "Exit command") {
//          this.send[String]("Shut down.")
//        }
      }

      stepResult.onComplete{
        case Success(_) => println("Finished another request.")
        case Failure(exception) => exception.printStackTrace()
      }

    }

//    super.writeResponse("Shut down.")
//    this.endSession
//    this.send[Seq[String]](super.getPrintBuffer())

    socket.close()
  }
}
