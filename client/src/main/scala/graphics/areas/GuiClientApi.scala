package graphics.areas

import java.awt._
import java.util.UUID

import commands.Command
import javax.swing.{JFrame, WindowConstants}

import scala.concurrent.ExecutionContext

class GuiClientApi(size: (Int, Int), userId: UUID, supportWidth: Int)
                  (implicit ec: ExecutionContext)
  extends JFrame("GUIClient"){

  this.setLayout(new GridLayout(1, 2))
  this.setSize(size._1, size._2)
//  private val gridConstr = new GridBagConstraints()

  private val responseArea = new ResponseArea
  private val pickCommandArea = new PickCommandArea(userId, supportWidth)

  def addResponse(response: String): Unit = responseArea.addResponse(response)
  def waitForCommand: Option[Command] = pickCommandArea.waitForCommand

//  gridConstr.fill = GridBagConstraints.HORIZONTAL
//  gridConstr.weightx = 0.8
//  gridConstr.gridx = 0
//  gridConstr.gridy = 0
//  this.add(responseArea, gridConstr)

//  gridConstr.fill = GridBagConstraints.HORIZONTAL
//  gridConstr.weightx = 0.2
//  gridConstr.gridx = 1
//  gridConstr.gridy = 0
//  this.add(pickCommandArea, gridConstr)

  this.add(responseArea)
  this.add(pickCommandArea)

  this.setVisible(true)
  this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
}
