package graphics.areas

import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.util.UUID

import commands.Command
import graphics.commands._
import graphics.templates.{ConfirmationTemplate, FormTemplate}
import javax.swing._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future, Promise}

final class PickCommandArea(userId: UUID, width: Int)
                           (implicit ec: ExecutionContext)
  extends JPanel {


  private val commandVariants_Confirm: Map[String, ConfirmationTemplate[Command]] =
    Map(
      "IMPORT" -> new ImportFrame(userId, width),
      "INFO" -> new InfoFrame(userId, width),
      "HELP" -> new HelpFrame(userId, width),
      "SHOW" -> new ShowFrame(userId, width)
    )

  private val commandVariants_Form: Map[String, FormTemplate[String, Command]] =
    Map(
      "ADD" -> new AddFrame(userId, width),
      "ADD IF MAX" -> new AddIfMaxFrame(userId, width),
      "REMOVE" -> new RemoveFrame(userId, width),
      "REMOVE LOWER" -> new RemoveLowerFrame(userId, width),
      "REMOVE ALL" -> new RemoveAllFrame(userId, width),
      "SAVE" -> new SaveFrame(userId, width),
      "LOAD" -> new LoadFrame(userId, width)
    )

  private val commands: Seq[String] =
    commandVariants_Confirm.keys.toSeq ++ commandVariants_Form.keys.toSeq
  private val buttonGroup: ButtonGroup = new ButtonGroup
  private val radioButtons: Seq[JRadioButton] = commands.map(new JRadioButton(_))
  private val submitButton: JButton = new JButton("TRY SELECTED")
  private var result: Promise[Option[Command]] = Promise[Option[Command]]

  this.setLayout(new GridLayout(commands.length + 1, 1, 5, 5))

  radioButtons.foreach { button =>
    buttonGroup.add(button)
    this.add(button)
  }

  private def chooser(command: String): Future[Option[Command]] = {
    if (commandVariants_Form.keySet.contains(command)){
      commandVariants_Form(command).showAndFuture.map(Some(_))
    } else {
      commandVariants_Confirm(command).showAndFuture
    }
  }

  def waitForCommand: Option[Command] = {
    val resultFuture = result.future
    println("Starting to wait...")
    val outRes: Option[Command] = Await.result(resultFuture, Duration.Inf)
    result = Promise[Option[Command]]
    outRes
  }

  submitButton.addActionListener {
    _: ActionEvent =>
      val selectedButtons: Seq[JRadioButton] = radioButtons.filter(_.isSelected)
      assert(selectedButtons.length == 1)
      println(selectedButtons.map(_.getText))
      val curCom: String = selectedButtons.head.getText
      result.completeWith(chooser(curCom))
      radioButtons.foreach(_.setSelected(false))
  }
  this.add(submitButton)

}
