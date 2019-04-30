package commands

case class Command(command: CommandType.Value, subEntity: Option[String], data: Option[Array[Byte]])