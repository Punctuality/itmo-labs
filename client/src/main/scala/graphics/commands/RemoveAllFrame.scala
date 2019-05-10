package graphics.commands

import java.util.UUID

import commands.Command
import commands.CommandType._
import graphics.templates.FormTemplate

import scala.concurrent.ExecutionContext

final class RemoveAllFrame(userId: UUID, width: Int)
                    (implicit ec: ExecutionContext)
  extends FormTemplate[String, Command](Seq("Word to Compare"),
    seq => userId -> s"""REMOVEALL {"s":"${seq.head}"}""")(width)
