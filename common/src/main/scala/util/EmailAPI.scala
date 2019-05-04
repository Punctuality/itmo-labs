package util

import java.util.Properties

import javax.mail._
import javax.mail.internet._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class EmailAPI(host: String = "smtp.gmail.com", verbose: Boolean = true)
              (implicit auth: PasswordAuthentication,
               ec: ExecutionContext) {

  private val session: Session = {
    val props = new Properties
    props.put("mail.smtp.host", host)
    props.put("mail.smtp.auth", "true")
    props.put("mail.smtp.starttls.enable", "true")

    Session.getDefaultInstance(props, new Authenticator {
      protected override def getPasswordAuthentication: PasswordAuthentication = auth
    })
  }

  private def sendMessage(message: MimeMessage): Future[Unit] =
    Future(Transport.send(message))

  def sendMessage(address: String)(subject: String, body: String): Future[Unit] =
    Future{
      val message = new MimeMessage(session)
      message.setFrom(new InternetAddress(auth.getUserName))
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(address))
      message.setSubject(subject)
      message.setText(body)
      message
    }.flatMap(this.sendMessage).andThen{
      case Success(_) => if (verbose) println(
        s"""
          |-------------------
          |Message to $address
          |Subject: $subject
          |Sent successfully.
          |-------------------
        """.stripMargin)
      case Failure(exp) => exp.printStackTrace()
    }

}