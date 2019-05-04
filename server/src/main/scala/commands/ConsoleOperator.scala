package commands

import CSVcontrol._
import domain._
import commands.CommandType._
import dao.inmemory.InMemoryWordDao
import org.json4s._
import org.json4s.jackson.JsonMethods._
import util.Serialization._

import scala.collection.mutable
import scala.io.StdIn._

class ConsoleOperator(pathToStart: String, pathToSave: String) extends Thread {
  import ConsoleOperator._

  protected val printBuffer: mutable.Buffer[String] = mutable.Buffer.empty

  def writeResponse(message: String): Unit =
    printBuffer append (message + "\n")


  def getPrintBuffer(clear: Boolean = true): Seq[String] = {
    val out: Seq[String] = printBuffer.clone
    if (clear) printBuffer.clear
    out
  }

  override def run(): Unit = this.startSession()

  def startSession(): Unit = {

    implicit var curText: Text = load(this.pathToStart)

    try {
      var isExiting = false
      while (!isExiting) {
        val stepResult: (String, (InMemoryWordDao, METAINFO)) = consoleUIStep(readLine())
        val newText = stepResult._2
        newText._2._2.append(stepResult._1)
        curText = newText
        isExiting = stepResult._1.equals("Exit command")
        getPrintBuffer().foreach(print)
      }
    } finally {
      writeResponse("Shut down.")
      writeResponse("Log: ")
      curText._2._2.foreach(command => writeResponse("   " + command))
      endSession
      getPrintBuffer().foreach(print)
    }
  }

  def endSession(implicit text: Text): Unit = {
    save()
    ()
  }

  def consoleUIStep(userInput: Command)(implicit text: Text): (String, Text) = {

    try {
      val data: Option[Array[Byte]] = userInput.data
      val parsed: Option[String] = userInput.subEntity
      val command: CommandType.Value = userInput.command
      command match {
        case INFO => info(); "Info command" -> text
        case SHOW => show(); "Show command" -> text
        case ADD => s"Add command: ${parsed.get}" -> add(parsed.get)
        case ADDIFMAX => s"AddIfMax command: ${parsed.get}" -> add_if_max(parsed.get)
        case REMOVE => s"Remove command: ${parsed.get}" -> remove(parsed.get)
        case REMOVELOWER => s"RemoveLower command: ${parsed.get}" -> remove_lower(parsed.get)
        case REMOVEALL => s"RemoveAll command: ${parsed.get}" -> remove_all(parsed.get)
        case EXIT => "Exit command" -> text
        case HELP => help(); "Help command" -> text
        case IMPORT => "Imported new text" -> ConsoleOperator.importText(data.get)
        case SAVE => s"Saved current text: ${parsed.get}" -> ConsoleOperator.save(parsed.get)
        case LOAD => s"Loaded new text: ${parsed.get}" -> ConsoleOperator.load(parsed.get)
        case UNKNOWN => writeResponse("Incorrect command."); help(); s"Unknown command" -> text
      }
    } catch {
      case exp: Exception => writeResponse(s"Exception occured: ${exp.getMessage}"); exp.printStackTrace(); ("$Error$", text)
    }
  }

  def save()(implicit text: Text): Text = {
    ConsoleOperator.save(this.pathToSave)
  }

  def info(printOut: Boolean = true)(implicit text: Text): String = {
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

    if (printOut) writeResponse(infoStr)

    infoStr
  }

  def help(printOut: Boolean = true): String = {
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
    if (printOut) writeResponse(out)
    out
  }

  def show(printOut: Boolean = true)(implicit text: Text): String = {
    val showStr = text._1.toString

    if (printOut) writeResponse(showStr)

    showStr
  }

  def add(jsonInput: String)(implicit text: Text): Text = {
    val newWord: Word = jsonParser(jsonInput)
    text._1.add(newWord)
    text
  }

  def add_if_max(jsonInput: String)(implicit text: Text): Text = {
    val newWord: Word = jsonParser(jsonInput)
    val wordValue: Int = newWord.value.length
    val maxTextValue: Int = text._1.getWordSizes.max
    if (wordValue >= maxTextValue) text._1.add(newWord)
    text
  }

  def remove(jsonInput: String)(implicit text: Text): Text = {
    val newWord: Word = jsonParser(jsonInput)
    val deleteIndex: Option[Int] = Range(0, text._1.size - 1).find(text._1.getSentences.get(_).getValue.equals(newWord.getValue))
    deleteIndex match {
      case None => text
      case Some(index) =>
        text._1.pop(index)
        text
    }
  }

  def remove_lower(jsonInput: String)(implicit text: Text): Text = {
    val newWord: Word = jsonParser(jsonInput)
    val deleteIndices: Seq[Int] = Range(0, text._1.size - 1).filter(text._1.getSentences.get(_).getValue.length < newWord.getValue.length)
    deleteIndices.zipWithIndex.foreach(pair => text._1.pop(pair._1 - pair._2))
    text
  }

  def remove_all(jsonInput: String)(implicit text: Text): Text = {
    val newWord: Word = jsonParser(jsonInput)
    val deleteIndices: Seq[Int] = Range(0, text._1.size - 1).filter(text._1.getSentences.get(_).getValue.length == newWord.getValue.length)
    deleteIndices.zipWithIndex.foreach(pair => text._1.pop(pair._1 - pair._2))
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

object ConsoleOperator {
  type Text = (InMemoryWordDao, METAINFO)
  type METAINFO = (Long, mutable.Buffer[String]) // timestamp, commands logger

  private implicit val format: DefaultFormats.type = DefaultFormats

  private def jsonParser(json: String): Word = parse(json).extract[Word]

  def save(pathToSave: String)(implicit text: Text): Text = {
    val csvToSave = new CSVWriter(pathToSave)
    val currentTextState: Seq[Array[String]] = Range(0, text._1.size - 1).map(text._1.getSentences.get(_).getValue).map(
      Array(_)
    )
    csvToSave.writeSeq(currentTextState)
    csvToSave.close()
    text
  }

  def importText(entity: Array[Byte]): Text = {
    val csvInput: Array[Array[String]] = entity
    val words: Array[String] = csvInput.map(_ (0))
    val sentence = new InMemoryWordDao(words)
    val newText: Text = (sentence, (System.currentTimeMillis(), mutable.Buffer("Started")))
    newText
  }

  def load(pathToText: String): Text = {
    val csv: CSVReader = new CSVReader(pathToText)
    val csvInput: Array[Array[String]] = csv.readAll
    val words: Array[String] = csvInput.map(_ (0))
    val sentence = new InMemoryWordDao(words)
    val newText: Text = (sentence, (System.currentTimeMillis(), mutable.Buffer("Started")))
    newText
  }
}