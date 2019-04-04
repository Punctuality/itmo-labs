package CSVcontrol

import java.io.{BufferedOutputStream, FileOutputStream}

import scala.util.{Failure, Success, Try}

trait Writer[T]{
  def write(toWrite: T): Try[T]
  def writeSeq(toWrite: Seq[T]): Try[Seq[T]]
  def close(): Unit
}

class CSVWriter(path: String, separator: String = ",") extends Writer[Array[String]]{
  private val fos: FileOutputStream = new FileOutputStream(path)
  private val stream: BufferedOutputStream = new BufferedOutputStream(fos)

  override def write(toWrite: Array[String]): Try[Array[String]] = {
    try{
      val tempStr = toWrite.reduce((a, b) => a + separator + b).+("\n")
      stream.write(tempStr.getBytes)
      Success(toWrite)
    } catch {
      case exp: Exception => Failure(exp)
    }
  }

  override def writeSeq(toWrite: Seq[Array[String]]): Try[Seq[Array[String]]] = {
    try{
      toWrite.foreach{line =>
        val tempStr = line.reduce((a, b) => a + separator + b).+("\n")
        stream.write(tempStr.getBytes)
      }
      Success(toWrite)
    } catch {
      case exp: Exception => Failure(exp)
    }
  }

  override def close(): Unit = stream.flush()
}
