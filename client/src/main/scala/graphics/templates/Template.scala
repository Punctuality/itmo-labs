package graphics.templates

import scala.concurrent.{ExecutionContext, Future}

trait Template[T]{
  def showAndWait(implicit ec: ExecutionContext): T

  def showAndFuture(implicit ec: ExecutionContext): Future[T]
}
