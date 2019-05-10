import dao._
import dao.db._
import javax.mail.PasswordAuthentication

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Labor7Starter {

  def main(args: Array[String]): Unit = {

    Await.result(Schema.setup(), 1 minute)

    implicit val wDao: WordDao = new SlickWordDao()
    implicit val uDao: UserDao = new SlickUserDao()
    implicit val auth: PasswordAuthentication =
      new PasswordAuthentication(
        "e@mail.com",
        "password"
      )

    val port: Int = 8888

    val server: ConsoleServer = new ConsoleServer(
      "resources/startText.csv",
      "resources/saveText.csv",
      port
    )

    val serverThread = server.getThread
    serverThread.start()

    println("Server Deployed!")
  }
}