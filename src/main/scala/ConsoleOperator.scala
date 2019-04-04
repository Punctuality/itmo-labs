import CSVcontrol.{CSVReader, CSVWriter}
import Compilers._
import org.json4s._
import org.json4s.native.JsonMethods.parse

import scala.collection.mutable.Buffer
import scala.io.StdIn._

class ConsoleOperator(pathToStart: String, pathToSave: String) {

  private implicit val format: DefaultFormats.type = DefaultFormats

  private type Text = (SentenceCompiler, METAINFO)
  private type METAINFO = (Long, Buffer[String])// timestamp, commands logger

  private def jsonParser(json: String): Word = parse(json).extract[Word]

  def startSession(): Unit = {
    val csv: CSVReader = new CSVReader(this.pathToStart)
    val csvInput: Array[Array[String]] = csv.readAll
    val words: Array[String] = csvInput.map(_ (0))
    val sentence = new SentenceCompiler(words)

    implicit var curText: (SentenceCompiler, METAINFO) = (sentence, (System.currentTimeMillis(), Buffer("Started")))

    try {
      var isExiting = false
      while (!isExiting) {
        val stepResult: (String, (SentenceCompiler, METAINFO)) = consoleUIStep
        val newText = stepResult._2
        newText._2._2.append(stepResult._1)
        curText = newText
        isExiting = stepResult._1.equals("Exit command")
      }
    } finally {
      println("Shut down.")
      println("Log: ")
      curText._2._2.foreach(command => println("   " + command))
      endSession
    }
  }

  private def endSession(implicit text: Text): Unit = {
    val csvToSave = new CSVWriter(this.pathToSave)
    val currentTextState: Seq[Array[String]] = Range(0, text._1.size - 1).map(text._1.getSentences.get(_).getValue).map(
      Array(_)
    )
    csvToSave.writeSeq(currentTextState)
    csvToSave.close()
  }

  private def consoleUIStep(implicit text: Text): (String, Text)= {
    print(s":${text._2._2.last}: ==|> ")
    val userInput = readLine()
    try {
      val parsed = userInput.split(" ")
      val command = parsed(0)
      command.toLowerCase match {
        case "info" => info(); ("Info command", text)
        case "show" => show(); ("Show command", text)
        case "add" => (s"Add command: ${parsed(1)}", add(parsed(1)))
        case "add_if_max" => (s"AddIfMax command: ${parsed(1)}", add_if_max(parsed(1)))
        case "remove" => (s"Remove command: ${parsed(1)}", remove(parsed(1)))
        case "remove_lower" => (s"RemoveLower command: ${parsed(1)}", remove_lower(parsed(1)))
        case "remove_all" => (s"RemoveAll command: ${parsed(1)}", remove_all(parsed(1)))
        case "exit" => ("Exit command", text)
        case "help" => help(); ("Help command", text)
        case otherCommand: String => println("Incorrect command."); help(); (s"Incorrect command: $otherCommand", text)
      }
    } catch {
      case exp: Exception => println(s"Exception occured: ${exp.getMessage}"); ("$Error$", text)
    }
  }

  private def help(printOut: Boolean = true): String = {
    val out =
      """
        | You can type such commands as:
        | * info // showing an info about text state
        | * show // showing text
        | * add {"value":"TextForNewWord"} // adding new word to text
        | * add_if_max {"value":"TextForNewWord"} // adding new word to text if it's bigger than previous ones
        | * remove {"value":"TextOfWordToRemove"} // removes first occurring word with such a value
        | * remove_lower {"value":"TextOfWordToRemove"} // removes shorter words
        | * remove_all {"value":"TextOfWordToRemove"} // removes same length words
      """.stripMargin
    if (printOut) println(out)
    out
  }

  private def info(printOut: Boolean = true)(implicit text: Text): String = {
    val infoStr =
      """
        | Info:
        | $1$
        | $2$
        | $3$
        | $4$
      """.stripMargin
        .replace("$1$", s"Text's hashCode: ${text._1.hashCode}")
        .replace("$2$", s"Text's length: ${text._1.size}")
        .replace("$3$", s"Creation time: ${text._2._1}")
        .replace("$4$", s"Existing: ${System.currentTimeMillis() - text._2._1} ms")

    if (printOut) println(infoStr)

    infoStr
  }

  private def show(printOut: Boolean = true)(implicit text: Text): String = {
    val showStr = text._1.toString

    if (printOut) println(showStr)

    showStr
  }

  private def add(jsonInput: String)(implicit text: Text): Text = {
    val newWord: Word = jsonParser(jsonInput)
    text._1.add(newWord)
    text
  }

  private def add_if_max(jsonInput: String)(implicit text: Text): Text = {
    val newWord: Word = jsonParser(jsonInput)
    val wordValue: Int = newWord.value.length
    val maxTextValue: Int = text._1.getWordSizes.max
    if (wordValue >= maxTextValue) text._1.add(newWord)
    text
  }

  private def remove(jsonInput: String)(implicit text: Text): Text = {
    val newWord: Word = jsonParser(jsonInput)
    val deleteIndex: Option[Int] = Range(0, text._1.size-1).find(text._1.getSentences.get(_).getValue.equals(newWord.getValue))
    deleteIndex match {
      case None => text
      case Some(index) =>
        text._1.pop(index)
        text
    }
  }

  private def remove_lower(jsonInput: String)(implicit text: Text): Text = {
    val newWord: Word = jsonParser(jsonInput)
    val deleteIndices: Seq[Int] = Range(0, text._1.size-1).filter(text._1.getSentences.get(_).getValue.length < newWord.getValue.length)
    deleteIndices.foreach(index => text._1.pop(index))
    text
  }

  private def remove_all(jsonInput: String)(implicit text: Text): Text = {
    val newWord: Word = jsonParser(jsonInput)
    val deleteIndices: Seq[Int] = Range(0, text._1.size-1).filter(text._1.getSentences.get(_).getValue.length == newWord.getValue.length)
    deleteIndices.foreach(index => text._1.pop(index))
    text
  }
  /*
 *   info: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
 *   remove_all {element}: удалить из коллекции все элементы, эквивалентные заданному
 *   remove_lower {element}: удалить из коллекции все элементы, меньшие, чем заданный
 *   add_if_max {element}: добавить новый элемент в коллекцию, если его значение больше, чем у набольшего элемента этой коллекции
 *   add {element}: добавить новый элемент в коллекцию
 *   remove {element}: удалить элемент из коллекции по его значению
 *   show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении
 */
}