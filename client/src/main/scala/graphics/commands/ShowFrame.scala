package graphics.commands

import java.util.UUID

import commands.Command
import commands.CommandType._
import graphics.templates.ConfirmationTemplate

final class ShowFrame(userId: UUID, width: Int)
  extends ConfirmationTemplate[Command](idValue = Some(userId -> "show"),
    question = "Request for Text?")("Request", width)
