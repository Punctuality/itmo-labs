package users

import java.util.UUID

import javax.mail.PasswordAuthentication
import util.EmailAPI

import scala.concurrent.{ExecutionContext, Future}

case class User(id: UUID,
                passwordHash: String,
                name: Option[String],
                email: Option[String])

object User {

  def generateUser(name: Option[String],
                   email: Option[(String, PasswordAuthentication, String)])
                  (implicit ec: ExecutionContext): Future[(User, String)] =
    Future(UUID.randomUUID()).flatMap { id =>
      val passwordGenerator = new PasswordGenerator
      passwordGenerator.generatePasswordAsync.map { pass =>
        val hash = PasswordGenerator.hashPassword(pass)

        (User.apply(id, hash, name, email.map(_._1)), pass)
      }
    }.flatMap { pair =>
      val message =
        s"""
           |Dear ${pair._1.name.getOrElse("Anonymous")}!
           |Your new credentials:
           |ID:
           |${pair._1.id}
           |
           |Password:
           |${pair._2}
           |
           |Please remember them properly. :)
        """.stripMargin

      val notify: Future[String] = if (email.isEmpty) {
        println("No Email Provided") // TODO
        Future.successful(message)
      } else {
        implicit val auth: PasswordAuthentication = email.get._2
        val emailAPI = new EmailAPI(email.get._3)
        emailAPI.sendMessage(email.get._1)("Password Notification", message)
          .map(_ => s"Password was sent to ${email.get._1}")
      }

      notify.map(pair._1 -> _)
    }
}