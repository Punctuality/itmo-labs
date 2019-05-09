import java.net.{DatagramPacket, DatagramSocket, InetAddress}
import java.util.UUID

import CSVcontrol.CSVReader
import commands.Constants._
import commands.{Command, ConsoleOperator}
import dao._
import domain.Word
import javax.mail.PasswordAuthentication
import users.User
import util.Serialization._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class ConsoleServer(pathToStart: String,
                    pathToSave: String,
                    port: Int,
                    emailHost: String = "smtp.gmail.com")
                   (implicit ec: ExecutionContext,
                    users: UserDao,
                    words: WordDao,
                    auth: PasswordAuthentication)
  extends ConsoleOperator(pathToStart, pathToSave, emailHost) with Runnable {

  //  import ConsoleOperator._
  import commands.CommandType._

  private val socket: DatagramSocket = new DatagramSocket(port)
  socket.setReuseAddress(true)
  private var running: Boolean = false
  private var address: InetAddress = _
  private var clientPort: Int = _
  private val buf: Array[Byte] = new Array[Byte](BUFFER_SIZE)

  lazy val getThread: Thread = new Thread(this)

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

    while (running) {
      val command: Command = this.receive[Command]

      println("New "+ command)

      val stepResult: Future[Array[Byte]] = consoleUIStep(command).map { message =>
        if (Seq(LOGIN, REGISTER).contains(command.command)) {
//          println("Message (Bytes): " + message)
//          println("Message (Parsed): " + (message: Option[(User, String)]))

          this.send[Option[(User, String)]](message)
        } else {
          this.send[String](message)
        }
      }

      stepResult.onComplete {
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
