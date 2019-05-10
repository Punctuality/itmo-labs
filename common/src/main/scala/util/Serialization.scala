package util

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

object Serialization{

  implicit def serialise[T](value: T): Array[Byte] = {
    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(value)
    oos.close()
    stream.toByteArray
  }

  implicit def deSerialise[T](bytes: Array[Byte]): T = {
    val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
    val value = ois.readObject
    ois.close()
    value.asInstanceOf[T]
  }

}
