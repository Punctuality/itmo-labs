package graphics.commands

import java.util.UUID

import commands.Command
import commands.CommandType._
import graphics.templates.FormTemplate

import scala.concurrent.ExecutionContext

final class RegistrationFrame(width: Int)
                             (implicit ec: ExecutionContext)
  extends FormTemplate[String, Command](Seq("Name: ", "Email: "),
    seq => Command(UUID.randomUUID(), REGISTER, None, Some(RegistrationFrame.prepareUserData(seq))))(width)

private object RegistrationFrame{
  def prepareUserData(seq: Seq[String]): Array[Byte] = {
    import util.Serialization._

    val toOption: String => Option[String] = {
      case "" => None
      case str => Some(str)
    }
    val name: Option[String] = toOption(seq.head)
    val email: Option[String] = toOption(seq(1))
    val userData: (Option[String], Option[String]) = name -> email
    println(s"New User Data: ${userData}")
    userData
  }
}