package graphics.commands

import java.io.File
import java.util.UUID

import commands.Command
import commands.CommandType._
import graphics.templates.ConfirmationTemplate
import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView

final class ImportFrame(userId: UUID, width: Int)
  extends ConfirmationTemplate[Command](idValue = ImportFrame.generateCommand(userId),
    question = "Choose CSV File")("Choose", width)

private object ImportFrame{
  def generateCommand(userId: UUID): Option[Command] = {
    val jfc = new JFileChooser(FileSystemView.getFileSystemView.getHomeDirectory)
    val returnValue = jfc.showOpenDialog(null)

    returnValue match {
      case JFileChooser.APPROVE_OPTION =>
        val file: File = jfc.getSelectedFile
        if (file.isFile) {
          if (file.getAbsolutePath.split('.').last == "csv") {
            Some(userId -> s"import <- ${file.getAbsolutePath}")
          } else None
        } else None
      case _ => None
    }
  }
}