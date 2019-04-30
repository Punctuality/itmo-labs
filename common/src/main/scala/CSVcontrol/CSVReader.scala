package CSVcontrol

import java.io.File
import java.util.Scanner

import scala.collection.mutable

trait Reader[T]{
  def readAll: Array[T]
  def read: T
  def close(): Unit
}

class CSVReader(path: String, separator: String = ";") extends Reader[Array[String]]{
  private val reader: Scanner = new Scanner(new File(path)) // java.util.Scanner
  private val storage: mutable.Buffer[Array[String]] = mutable.Buffer()

  override def readAll: Array[Array[String]] = {
    while(reader.hasNext) read
    storage.toArray
  }

  override def read: Array[String] = {
    val tempStr: Array[String] = reader.nextLine.split(separator)
    storage.append(tempStr)
    tempStr
  }

  override def close(): Unit = reader.close()
}
