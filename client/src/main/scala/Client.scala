import java.net.{DatagramPacket, DatagramSocket, InetAddress}

import util.Serialization._

class Client() {
  private val socket = new DatagramSocket()
  private val address = InetAddress.getByName("localhost")
  private var buf: Array[Byte] = _


  def sendEcho[T](msg: T): T= {
    buf = msg
    var packet = new DatagramPacket(buf, buf.length, address, 4445)
    socket.send(packet)
    packet = new DatagramPacket(buf, buf.length)
    socket.receive(packet)
    val received: T = packet.getData
    received
  }

  def close(): Unit = {
    socket.close()
  }
}
