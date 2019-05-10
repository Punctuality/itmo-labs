package graphics.commands

import java.util.UUID

import commands.Command
import commands.CommandType._
import graphics.templates.FormTemplate

import scala.concurrent.ExecutionContext

final class LoadFrame(userId: UUID, width: Int)
                    (implicit ec: ExecutionContext)
  extends FormTemplate[String, Command](Seq("Text to Load"),
    seq => userId -> s"""LOAD ${seq.head}""")(width)
