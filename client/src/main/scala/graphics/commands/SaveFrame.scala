package graphics.commands

import java.util.UUID

import commands.Command
import commands.CommandType._
import graphics.templates.FormTemplate

import scala.concurrent.ExecutionContext

final class SaveFrame(userId: UUID, width: Int)
                     (implicit ec: ExecutionContext)
  extends FormTemplate[String, Command](Seq("Where to Save"),
    seq => userId -> s"""SAVE ${seq.head}""")(width)
