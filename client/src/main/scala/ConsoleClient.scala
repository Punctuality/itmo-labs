import java.net.{InetSocketAddress, PortUnreachableException}

import commands.Command
import commands.CommandType._
import commands.Constants._
import util.Serialization._

import scala.io.StdIn.readLine


class ConsoleClient(hostIP: String, port: Int) extends Thread {

  import java.nio.ByteBuffer
  import java.nio.channels.DatagramChannel

  private val address: InetSocketAddress = new InetSocketAddress(hostIP, port)
  private val data: ByteBuffer = ByteBuffer.allocate(BUFFER_SIZE)
  private val dgChannel: DatagramChannel = DatagramChannel.open()
  private var isRunning: Boolean = false

  dgChannel.socket().connect(address)

  override def run(): Unit = {
    isRunning = true
    while (isRunning) {
      print("|user:root| |>")
      try {
        val toSend: Command = readLine()
        data.clear()
        data.put(toSend: Array[Byte])
        data.flip()
        dgChannel.send(data, address)

        data.clear()
        dgChannel.receive(data)
        data.flip()
        val toPrint: Seq[String] = data.array

        toPrint.foreach(print)
      } catch {
        case _: PortUnreachableException => println("Server is unreachable.")
      }
    }
  }

}
