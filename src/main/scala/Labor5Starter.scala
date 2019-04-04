

object Labor5Starter {
  private val pathToStart: String = "resources/startText.csv"

  def main(args: Array[String]): Unit = {
    val console = new ConsoleOperator("resources/startText.csv", "resources/saveText.csv")
    console.startSession()
  }
}