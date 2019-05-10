package users

import java.security.MessageDigest

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source

class PasswordGenerator(implicit ec: ExecutionContext){
  import PasswordGenerator._

  def generatePasswordAsync: Future[String] = Future(generatePassword)

  val hashPasswordAsync:
    Future[String] => Future[String] = future =>
    future.map(hashPassword(_))

}

object PasswordGenerator{
  private val urlGenerator: String = "https://www.dinopass.com/password/simple"

  val hashPassword: String => String = password =>
    MessageDigest.getInstance("SHA-256")
      .digest(password.getBytes("UTF-8"))
      .map("%02x".format(_)).mkString

  def generatePassword: String = {
    val src = Source.fromURL(urlGenerator)
    val password = src.mkString
    src.close()
    password
  }
}
