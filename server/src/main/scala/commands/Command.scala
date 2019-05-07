package commands

import java.util.UUID

case class Command(userId: UUID,
                   command: CommandType.Value,
                   subEntity: Option[String],
                   data: Option[Array[Byte]])