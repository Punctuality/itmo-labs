package graphics.commands

import java.util.UUID

import commands.Command
import commands.CommandType._
import graphics.templates.ConfirmationTemplate

final class InfoFrame(userId: UUID, width: Int)
  extends ConfirmationTemplate[Command](idValue = Some(userId -> "info"),
    question = "Request for INFO?")("Request", width)
