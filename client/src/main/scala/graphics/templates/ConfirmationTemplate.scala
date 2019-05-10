package graphics.templates

import java.awt.GridLayout
import java.awt.event.ActionEvent

import javax.swing._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future, Promise}

//Length Unsafe
class ConfirmationTemplate[B](idValue: => Option[B], question: String)
                             (sendText: String, width: Int, height: Option[Int] = None)
  extends JFrame with Template[Option[B]] {

  this.setLayout(new GridLayout(2, 1, 5, 5))

  private val DELTA: Int = 60

  private val text: JLabel = new JLabel(question)
  private val sendButton: JButton = new JButton(sendText)

  type Result = Option[B]
  private var result: Promise[Result] = Promise[Result]

  sendButton.addActionListener {
    _: ActionEvent => result success idValue
  }

  this.add(text)
  this.add(sendButton)

  //  override def dispose(): Unit = {
  //    if (!result.isCompleted) result success None
  //    println("Disposing!!!")
  //    super.dispose()
  //  }

  this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
  this.setSize(width, height.getOrElse(DELTA))

  override def showAndWait(implicit ec: ExecutionContext): Result = {
    Await.result(this.showAndFuture, Duration.Inf)
  }

  override def showAndFuture(implicit ec: ExecutionContext): Future[Result] = {
    //    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_OPENED))
    this.setVisible(true)
    val resultFuture = result.future
    resultFuture.andThen {
      case _ =>
        this.result = Promise[Result]
        //        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING))
        this.setVisible(false)
    }
    resultFuture
  }

}
