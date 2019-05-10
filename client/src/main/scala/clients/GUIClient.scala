package clients

import java.net.{InetSocketAddress, PortUnreachableException}

import commands.Command
import commands.Constants._
import graphics.areas.GuiClientApi
import graphics.commands.EnterSystem
import users.User
import util.Serialization._

import scala.concurrent.ExecutionContext


class GUIClient(hostIP: String, port: Int, size: (Int, Int), supportWidth: Int = 300)
               (implicit ec: ExecutionContext) extends Thread {

  import java.nio.ByteBuffer
  import java.nio.channels.DatagramChannel

  private val address: InetSocketAddress = new InetSocketAddress(hostIP, port)
  private val data: ByteBuffer = ByteBuffer.allocate(BUFFER_SIZE)
  private val dgChannel: DatagramChannel = DatagramChannel.open()
  private var isRunning: Boolean = false

  dgChannel.socket().connect(address)


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

  override def run(): Unit = {

    def enterSystem: (User, String) = {
      val enterSystemFrame: EnterSystem = new EnterSystem(supportWidth)
      val loginCommand: Command = enterSystemFrame.showAndWait
      this.sendCommand(loginCommand)
      val received: Option[(User, String)] = this.receiveResponse[Option[(User, String)]]
      received match {
        case Some(pair) => pair
        case None => enterSystem
      }
    }

    isRunning = true

    val logged = enterSystem
    val currentUser: User = logged._1

    val guiApi = new GuiClientApi(size, currentUser.id, 300)

    def printToArea(entity: String,
                    prefix: String = s"|user:${currentUser.name.getOrElse("Anonymous")}| |>"): Unit =
      guiApi.addResponse(prefix + entity)

    def getCommand: Command =
      guiApi.waitForCommand.getOrElse(getCommand)

    printToArea(logged._2)

    while (isRunning) {
      try {
        val toSend: Command = getCommand // Infinite repeating command!!! TODO
        this.sendCommand(toSend)

        val response = this.receiveResponse[String]
        println(response)
        printToArea(response)
      } catch {
        case _: PortUnreachableException => printToArea("Server is unreachable."); isRunning = false
      }
    }
  }

}
