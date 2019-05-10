package graphics.templates

import java.awt.event.{ActionEvent, WindowEvent}
import java.awt.{BorderLayout, GridLayout}

import javax.swing._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future, Promise}

//Length Unsafe
class ChoiceTemplate[A, B](choices: Map[A, B])
                          (width: Int, height: Option[Int] = None)
  extends JFrame with Template[B]{
  this.setLayout(new BorderLayout(5, 5))

  private val DELTA: Int = 30

  private val formPanel = new JPanel(new GridLayout(choices.size, 1, 0, 10))

  private val choiceButtons: Seq[(JButton, A)] =
    choices.keySet.toSeq.map(key => new JButton(key.toString) -> key)

  private var result: Promise[B] = Promise[B]

  choiceButtons.foreach { case (button, key) =>
    button.addActionListener { _: ActionEvent =>
//      println(s"Printing: ${choices(key)}")
      result success choices(key) }
    formPanel.add(button)
  }


  this.add(formPanel, BorderLayout.CENTER)

  this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
  this.setSize(width, height.getOrElse(choices.size * (10 + DELTA)))

  override def showAndWait(implicit ec: ExecutionContext): B = {
    Await.result(this.showAndFuture, Duration.Inf)
  }

  override def showAndFuture(implicit ec: ExecutionContext): Future[B] = {
    this.setVisible(true)
    val resultFuture = result.future
    resultFuture.andThen{
      case _ =>
        this.result = Promise[B]
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING))
    }
    resultFuture
  }
}
