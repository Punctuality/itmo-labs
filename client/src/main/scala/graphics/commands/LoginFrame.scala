package graphics.commands

import java.util.UUID

import commands.Command
import commands.CommandType._
import graphics.templates.FormTemplate
import users.PasswordGenerator

import scala.concurrent.ExecutionContext

final class LoginFrame(width: Int)
                    (implicit ec: ExecutionContext)
  extends FormTemplate[String, Command](Seq("ID: ", "Password: "), // Random UUID ~= Wrong ID
    seq => Command(try{UUID.fromString(seq.head)}catch{case _: Throwable => UUID.randomUUID()}, LOGIN,
      Some(PasswordGenerator.hashPassword(seq(1))), None))(width)