package commands

import java.util.UUID

import CSVcontrol.CSVReader

object CommandType extends Enumeration {
  val INFO,
  SHOW,
  ADD,
  ADDIFMAX,
  REMOVE,
  REMOVELOWER,
  REMOVEALL,
  EXIT,
  HELP,
  IMPORT,
  SAVE,
  LOAD,
  REGISTER,
  LOGIN,
  UNKNOWN = Value


  implicit def stringToCommand(command: (UUID, String)): Command = {
    import util.Serialization._

    val p = command._2.split(" ").toList
    val com = p.head.toUpperCase match {
      case "INFO" => INFO
      case "SHOW" => SHOW
      case "ADD" => ADD
      case "ADDIFMAX" => ADDIFMAX
      case "REMOVE" => REMOVE
      case "REMOVELOWER" => REMOVELOWER
      case "REMOVEALL" => REMOVEALL
      case "EXIT" => EXIT
      case "HELP" => HELP
      case "IMPORT" => IMPORT
      case "SAVE" => SAVE
      case "LOAD" => LOAD
      case _ => UNKNOWN
    }

    Command(command._1, com, p.lift(1), p.lift(2).map { path =>
      val csv: CSVReader = new CSVReader(path)
      val csvInput: Array[Array[String]] = csv.readAll
      csvInput
    })
  }

  def commandToString(command: Command): String = {
    List(command.command.toString, command.subEntity.getOrElse("")).mkString(" ")
  }
}