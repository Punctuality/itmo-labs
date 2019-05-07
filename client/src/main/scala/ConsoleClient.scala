import java.net.{InetSocketAddress, PortUnreachableException}
import java.util.UUID

import commands.Command
import commands.CommandType._
import commands.Constants._
import users.{PasswordGenerator, User}
import util.Serialization._

import scala.io.StdIn.readLine
import scala.concurrent.ExecutionContext

class ConsoleClient(hostIP: String, port: Int)
                   (implicit ec: ExecutionContext) extends Thread {

  import java.nio.ByteBuffer
  import java.nio.channels.DatagramChannel

  private val address: InetSocketAddress = new InetSocketAddress(hostIP, port)
  private val data: ByteBuffer = ByteBuffer.allocate(BUFFER_SIZE)
  private val dgChannel: DatagramChannel = DatagramChannel.open()
  private var isRunning: Boolean = false

  dgChannel.socket().connect(address)

  private def register: Command = {
    print("Enter your new Password")
    val password: String = readLine()
    val hashedPassword: String = PasswordGenerator.hashPassword(password)
    Command(UUID.randomUUID(), REGISTER, Some(hashedPassword), None)
  }

  private def login: Command = {
    print("Enter your ID: ")
    val id: UUID = UUID.fromString(readLine())
    print("Enter your Password")
    val password: String = readLine()
    val hashedPassword: String = PasswordGenerator.hashPassword(password)
    Command(id, LOGIN, Some(hashedPassword), None)
  }

  private def loginProcedure: Command = {
    println(
      """
        |Please choose one of the following variants:
        |1. Login
        |2. Register
        |_. Again
      """.stripMargin)
    readLine().head match {
      case '1' => println("Signing up procedure:"); login
      case '2' => println("Registering procedure:"); register
      case _ => loginProcedure
    }
  }

  private def enterSystem: User = {
    val loginCommand: Command = loginProcedure
    this.sendCommand(loginCommand)
    this.receiveResponse[Option[User]] match {
      case Some(user) => println(s"Logined as $user"); user
      case None => enterSystem
    }
  }

  private def sendCommand(command: Command): Unit = {
    data.clear()
    data.put(command: Array[Byte])
    data.flip()
    dgChannel.send(data, address)
  }

  private def receiveResponse[T]: T = {
    data.clear()
    dgChannel.receive(data)
    data.flip()
    data.array
  }

  private def printResponse(resp: Seq[String]): Unit =
    resp.foreach(print)

  override def run(): Unit = {
    isRunning = true

    val currentUser: User = enterSystem

    while (isRunning) {
      print(s"|user:${currentUser.name.getOrElse("Anonymous")}| |>")
      try {
        val toSend: Command = (currentUser.id, readLine())
        this.sendCommand(toSend)
        this.printResponse(this.receiveResponse[Seq[String]])
      } catch {
        case _: PortUnreachableException => println("Server is unreachable."); isRunning = false
      }
    }
  }

}
