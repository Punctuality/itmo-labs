package commands

import java.util.UUID

import CSVcontrol._
import commands.CommandType._
import dao.{UserDao, WordDao}
import domain._
import javax.mail.PasswordAuthentication
import org.json4s._
import org.json4s.jackson.JsonMethods._
import users.User
import util.Serialization._

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

class ConsoleOperator(pathToStart: String, pathToSave: String, emailHost: String)
                     (implicit ec: ExecutionContext,
                      users: UserDao,
                      words: WordDao,
                      auth: PasswordAuthentication) {

  import ConsoleOperator._

  protected val printBuffer: mutable.Buffer[String] = mutable.Buffer.empty

  def writeResponse(message: String): Unit =
    printBuffer append (message + "\n")

  def getPrintBuffer(clear: Boolean = true): Seq[String] = {
    val out: Seq[String] = printBuffer.clone
    if (clear) printBuffer.clear
    out
  }

  def save(pathToSave: String, words: Seq[String]): Unit = {
    val csvToSave = new CSVWriter(pathToSave)
    val currentTextState: Seq[Array[String]] = words.map(Array(_))
    csvToSave.writeSeq(currentTextState)
    csvToSave.close()
  }

  def importText(entity: Array[Byte])(implicit userId: UUID): Future[Unit] = {
    val wordValues: Seq[String] = ConsoleOperator.importText(entity).toSeq
    words.find(userId).map { someSeq =>
      someSeq.get.map(_.position).max
    }.flatMap(maxPos => words.insert(Word.makeWords(wordValues, maxPos, userId)))
  }

  def load(pathToText: String)(implicit userId: UUID): Future[Unit] = {
    val wordValues: Seq[String] = ConsoleOperator.load(pathToText).toSeq
    words.find(userId).map { someSeq =>
      someSeq.get.map(_.position).max
    }.flatMap(maxPos => words.insert(Word.makeWords(wordValues, maxPos, userId)))
  }

  def endSession(implicit userId: UUID): Future[Unit] =
    saveText(this.pathToSave)

  def saveText(pathToSave: String)(implicit userId: UUID): Future[Unit] =
    words.find(userId).map(someSeq =>
      save(pathToSave, someSeq.getOrElse(Seq()).map(_.value))
    )

  def consoleUIStep(userInput: Command): Future[Array[Byte]] = {
    implicit val userId: UUID = userInput.userId

    val command: CommandType.Value = userInput.command
    val parsed: Option[String] = userInput.subEntity
    val data: Option[Array[Byte]] = userInput.data

    val stepFuture: Future[Array[Byte]] =
      command match {
        case INFO =>
          info().map(i => i)
        case SHOW =>
          show().map(i => i)
        case ADD =>
          add(parsed.get).map(_ => s"Add command: ${parsed.get}")
        case ADDIFMAX =>
          add_if_max(parsed.get).map(_ => s"AddIfMax command: ${parsed.get}")
        case REMOVE =>
          remove(parsed.get).map(_ => s"Remove command: ${parsed.get}")
        case REMOVELOWER =>
          remove_lower(parsed.get).map(_ => s"RemoveLower command: ${parsed.get}")
        case REMOVEALL =>
          remove_all(parsed.get).map(_ => s"RemoveAll command: ${parsed.get}")
        case EXIT => exit.map(i => i)
        case LOGIN => login(parsed.get).map(i => i)
        case REGISTER => register(data.get, emailHost).map(i => i)
        case HELP => help().map(i => i)
        case IMPORT =>
          importText(data.get).map(_ => "Imported new text")
        case SAVE =>
          saveText(parsed.get).map(_ => s"Saved current text: ${parsed.get}")
        case LOAD =>
          load(parsed.get).map(_ => s"Loaded new text: ${parsed.get}")
        case UNKNOWN =>
          help().andThen {
            case Success(_) => writeResponse("Incorrect command.")
          }.map(_ => s"Unknown command")
      }

    stepFuture.recover {
      case exp: Exception =>
        val message = s"Exception occured: ${exp.getMessage}"
        writeResponse(message)
        exp.printStackTrace()
        message
    }
  }

  def register(data: (Option[String], Option[String]), emailHost: String)
              (implicit userId: UUID): Future[Option[(User, String)]] =
    User.generateUser(data._1, data._2.map(email => (email, auth, emailHost)))
      .flatMap { pair =>
        users.insert(pair._1)
          .map(_ => Some(pair))
      }
      .recover {
        case _ => None
      }

  def login(password: String)(implicit userId: UUID): Future[Option[(User, String)]] =
    users.find(userId).map {
      case Some(user) => if (user.passwordHash == password) Some(user)
        .map(_ -> "Correct Credentials.") else None
      case None => None
    }

  def exit(implicit userId: UUID): Future[String] = // TODO
    endSession.map(_ => "Exit command")

  def info(printOut: Boolean = true)(implicit userId: UUID): Future[String] =
    words.find(userId).map { someSeq =>
      val seq: Seq[Word] = someSeq.getOrElse(Seq())
      val infoStr: String =
        """
          | Info:
          | $1$
          | $2$
        """.stripMargin
          .replace("$1$", s"Text's hashCode: ${
            seq.hashCode
          }")
          .replace("$2$", s"Text's length: ${
            seq.length
          }")

      if (printOut) writeResponse(infoStr)

      infoStr
    }

  def help(printOut: Boolean = true)(implicit userId: UUID): Future[String] =
    users.find(userId).map { user =>
      val out: String =
        s"""
           | As ${user.get.name.getOrElse("Anonymous")}
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

  def show(printOut: Boolean = true)(implicit userId: UUID): Future[String] =
    words.compile(userId).andThen {
      case Success(text) if printOut => writeResponse(text)
    }

  def add(jsonInput: String)(implicit userId: UUID): Future[Unit] =
    words.find(userId).map {
      case Some(seq) => seq.last.position
      case None => 0
    }.map { maxPosition =>
      val template: WordParseTemplate = jsonParser(jsonInput)
      Word.makeWord(template, maxPosition + 1, userId)
    }.flatMap { word =>
      words.insert(word)
    }


  def add_if_max(jsonInput: String)(implicit userId: UUID): Future[Unit] =
    words.find(userId).map {
      case Some(seq) =>
        val newWord: Word = Word.makeWord(jsonParser(jsonInput), seq.last.position + 1, userId)
        val maxTextValue: Int = seq.map(_.value.length).max
        if (newWord.value.length >= maxTextValue) Some(newWord) else None
      case None => Some(Word.makeWord(jsonParser(jsonInput), 0 + 1, userId))
    }.flatMap {
      _.map(words.insert).getOrElse(Future.successful())
    }

  def remove(jsonInput: String)(implicit userId: UUID): Future[Unit] =
    words.find(userId).map(_.flatMap { seq =>
      val maxPosition = seq.last.position
      val newWord: Word = Word.makeWord(jsonParser(jsonInput), maxPosition + 1, userId)
      seq.find(_.value == newWord.value).map(_.position)
    }).flatMap(_.map(words.delete(userId, _)).getOrElse(Future.successful()))

  def remove_lower(jsonInput: String)(implicit userId: UUID): Future[Unit] =
    words.find(userId).map {
      _.map { seq =>
        val maxPosition = seq.last.position
        val newWord: Word = Word.makeWord(jsonParser(jsonInput), maxPosition + 1, userId)
        seq.filter(_.value.length <= newWord.value.length).map(_.position)
      }
    }.flatMap(_.map(pos => Future.traverse(pos)(words.delete(userId, _)))
      .getOrElse(Future.successful()).map(_ => ()))

  def remove_all(jsonInput: String)(implicit userId: UUID): Future[Unit] =
    words.find(userId).map {
      _.map { seq =>
        val maxPosition = seq.last.position
        val newWord: Word = Word.makeWord(jsonParser(jsonInput), maxPosition + 1, userId)
        seq.filter(_.value.length == newWord.value.length).map(_.position)
      }
    }.flatMap(_.map(pos => Future.traverse(pos)(words.delete(userId, _)))
      .getOrElse(Future.successful()).map(_ => ()))


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

  private implicit val format: DefaultFormats.type = DefaultFormats

  private def jsonParser(json: String): WordParseTemplate = parse(json).extract[WordParseTemplate]

  def importText(entity: Array[Byte]): Array[String] = {
    val csvInput: Array[Array[String]] = entity
    val words: Array[String] = csvInput.map(_ (0))
    words
  }

  def load(pathToText: String): Array[String] = {
    val csv: CSVReader = new CSVReader(pathToText)
    val csvInput: Array[Array[String]] = csv.readAll
    val words: Array[String] = csvInput.map(_ (0))
    words
  }
}