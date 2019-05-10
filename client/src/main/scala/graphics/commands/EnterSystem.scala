package graphics.commands

import commands.Command
import graphics.templates.Template

import scala.concurrent.{ExecutionContext, Future}

final class EnterSystem(width: Int)(implicit ec: ExecutionContext) extends Template[Command]{

  val loginProc = new LoginProcedureFrame(width)(ec)

  override def showAndWait(implicit ec: ExecutionContext): Command ={
    loginProc.showAndWait.showAndWait
  }

  override def showAndFuture(implicit ec: ExecutionContext): Future[Command] =
    loginProc.showAndWait.showAndFuture
}
