package users

import java.util.UUID

import javax.mail.PasswordAuthentication
import util.EmailAPI

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

case class User(id: UUID,
                passwordHash: String,
                name: Option[String],
                email: Option[String])

object User {

  def generateUser(name: Option[String],
                   email: Option[(String, PasswordAuthentication, String)])
                  (implicit ec: ExecutionContext): Future[User] =
    Future(UUID.randomUUID()).flatMap { id =>
      val passwordGenerator = new PasswordGenerator
      passwordGenerator.generatePasswordAsync.map { pass =>
        val hash = PasswordGenerator.hashPassword(pass)

        (User.apply(id, hash, name, email.map(_._1)), pass)
      }
    }.flatMap { pair =>
      val message =
        s"""
           |Your new credentials:
           |ID: ${pair._1.id}
           |Password: ${pair._2}
        """.stripMargin

      val notify: Future[Unit] = if (email.isEmpty) {
        Future(println(message))
      } else {
        implicit val auth: PasswordAuthentication = email.get._2
        val emailAPI = new EmailAPI(email.get._3)
        emailAPI.sendMessage(email.get._1)("Password Notification", message)
      }

      notify.map(_ => pair._1)
    }
}