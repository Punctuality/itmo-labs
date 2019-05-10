package graphics.commands

import java.util.UUID

import commands.Command
import commands.CommandType._
import graphics.templates.ConfirmationTemplate

final class HelpFrame(userId: UUID, width: Int)
  extends ConfirmationTemplate[Command](idValue = Some(userId -> "help"),
    question = "Request for HELP?")("Request", width)
