import java.net.{InetSocketAddress, PortUnreachableException}
import java.util.UUID

import commands.Command
import commands.CommandType._
import commands.Constants._
import users.{PasswordGenerator, User}
import util.Serialization._

import scala.concurrent.ExecutionContext
import scala.io.StdIn.readLine

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

    val toOption: String => Option[String] = {
      case ""  => None
      case str => Some(str)
    }

    println("Enter your NAME:")
    val name: Option[String] = toOption(readLine)
    println("Enter your EMAIL:")
    val email: Option[String] = toOption(readLine)

    val userData: Array[Byte] = name -> email

    Command(UUID.randomUUID(), REGISTER, None, Some(userData))
  }

  private def login: Command = {
    print("Enter your ID: ")
    val id: UUID = UUID.fromString(readLine())
    print("Enter your Password: ")
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

  private def enterSystem: (User, String) = {
    val loginCommand: Command = loginProcedure
    this.sendCommand(loginCommand)
    val received: Option[(User, String)] = this.receiveResponse[Option[(User, String)]]
    received match {
      case Some(pair) =>
        println(pair._2)
        println(s"Logined as ${pair._1}"); pair
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

  @deprecated
  private def printResponse(resp: Seq[String]): Unit =
    resp.foreach(print)

  override def run(): Unit = {
    isRunning = true

    val currentUser: User = enterSystem._1

    while (isRunning) {
      print(s"|user:${currentUser.name.getOrElse("Anonymous")}| |>")
      try {
        val toSend: Command = (currentUser.id, readLine())
        this.sendCommand(toSend)
        println(this.receiveResponse[String])
      } catch {
        case _: PortUnreachableException => println("Server is unreachable."); isRunning = false
      }
    }
  }

}
