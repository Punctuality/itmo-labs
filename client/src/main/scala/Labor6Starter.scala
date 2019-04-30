
object Labor6Starter {

  def main(args: Array[String]): Unit = {
    val port: Int = 8888

    val server = new ConsoleServer(
      "resources/startText.csv",
      "resources/saveText.csv",
      port
    )

    server.start()

    val client = new ConsoleClient("localhost", port)

    client.run()
  }
}