package graphics.areas

import java.awt.{BorderLayout, Color}

import javax.swing._

import scala.collection.mutable

final class ResponseArea extends JPanel{

  private val responsesBuffer: mutable.Buffer[String] = mutable.Buffer[String]()
  private val responses = new JTextArea()
  private val scrollOutput = new JScrollPane(responses, 20, 30)

  responses.setLineWrap(true)
  responses.setEditable(false)

  this.setLayout(new BorderLayout())
  this.add(scrollOutput, BorderLayout.CENTER)
  this.setForeground(Color.BLACK)

  def addResponse(response: String): Unit = {
    this.setForeground(Color.BLUE)
    responsesBuffer.append(response)
    responses.setText(responsesBuffer.mkString("\n"))
    this.setForeground(Color.BLACK)
  }
}
