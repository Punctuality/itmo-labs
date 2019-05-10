package graphics.templates

import java.awt.event.ActionEvent
import java.awt.{BorderLayout, GridLayout}

import javax.swing._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future, Promise}

//Length Unsafe
class FormTemplate[A, B](fields: Seq[A], action: Seq[String] => B)
                     (width: Int, height: Option[Int] = None, sendText: String = "SEND")
  extends JFrame with Template[B]{
  this.setLayout(new BorderLayout(5, 5))

  private val DELTA: Int = 30

  private val formPanel = new JPanel(new GridLayout(fields.length, 2))

  private val annotations: Seq[JLabel] = fields.map(elem => new JLabel(elem.toString))
  private val forms: Seq[JTextField] = annotations.map(_ => new JTextField(40))
  private val sendButton: JButton = new JButton(sendText)

  private var result: Promise[B] = Promise[B]
  sendButton.addActionListener {
    _: ActionEvent =>
      val data: Seq[String] = forms.map(_.getText)
      result success action(data)
  }

  annotations.zip(forms).foreach {
    case (annotation, form) =>
      formPanel.add(annotation)
      formPanel.add(form)
  }
  this.add(formPanel, BorderLayout.CENTER)
  this.add(sendButton, BorderLayout.SOUTH)

  this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
  this.setSize(width, height.getOrElse(annotations.length * DELTA))

  override def showAndWait(implicit ec: ExecutionContext): B = {
    Await.result(this.showAndFuture, Duration.Inf)
  }

  override def showAndFuture(implicit ec: ExecutionContext): Future[B] = {
//    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_OPENED))
    this.setVisible(true)
    val resultFuture = result.future
    resultFuture.andThen{
      case _ =>
        this.result = Promise[B]
//        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING))
        this.setVisible(false)
    }
    resultFuture
  }
}
