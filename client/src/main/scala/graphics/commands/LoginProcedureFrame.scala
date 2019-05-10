package graphics.commands

import commands.Command
import graphics.templates.{ChoiceTemplate, Template}

import scala.concurrent.ExecutionContext

final class LoginProcedureFrame(width: Int)(implicit ec: ExecutionContext)
  extends ChoiceTemplate[String, Template[Command]](LoginProcedureFrame.logMap(width))(width)

private object LoginProcedureFrame{
  def logMap(width: Int)(implicit ec: ExecutionContext): Map[String, Template[Command]] = {
    lazy val login: (String, Template[Command]) = "LOG IN" -> new LoginFrame(width)
    lazy val register: (String, Template[Command]) = "SING UP" -> new RegistrationFrame(width)
    Map(login, register)
  }
}
//private def loginProcedure: Command = {
//    println(
//      """
//        |Please choose one of the following variants:
//        |1. Login
//        |2. Register
//        |_. Again
//      """.stripMargin)
//    readLine().head match {
//      case '1' => println("Signing up procedure:"); login
//      case '2' => println("Registering procedure:"); register
//      case _ => loginProcedure
//    }
//  }

//private def enterSystem: (User, String) = {
//    val loginCommand: Command = loginProcedure
//    this.sendCommand(loginCommand)
//    val received: Option[(User, String)] = this.receiveResponse[Option[(User, String)]]
//    received match {
//      case Some(pair) =>
//        println(pair._2)
//        println(s"Logined as ${pair._1}"); pair
//      case None => enterSystem
//    }
//  }

// private def register: Command = {
//
//    val toOption: String => Option[String] = {
//      case ""  => None
//      case str => Some(str)
//    }
//
//    println("Enter your NAME:")
//    val name: Option[String] = toOption(readLine)
//    println("Enter your EMAIL:")
//    val email: Option[String] = toOption(readLine)
//
//    val userData: Array[Byte] = name -> email
//
//    Command(UUID.randomUUID(), REGISTER, None, Some(userData))
//  }
//
//  private def login: Command = {
//    print("Enter your ID: ")
//    val id: UUID = UUID.fromString(readLine())
//    print("Enter your Password: ")
//    val password: String = readLine()
//    val hashedPassword: String = PasswordGenerator.hashPassword(password)
//    Command(id, LOGIN, Some(hashedPassword), None)
//  }