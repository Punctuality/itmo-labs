package graphics.commands

import java.util.UUID

import commands.Command
import commands.CommandType._
import graphics.templates.FormTemplate

import scala.concurrent.ExecutionContext

final class AddFrame(userId: UUID, width: Int)
                    (implicit ec: ExecutionContext)
  extends FormTemplate[String, Command](Seq("Word To Add"),
    seq => userId -> s"""ADD {"s":"${seq.head}"}""")(width)
