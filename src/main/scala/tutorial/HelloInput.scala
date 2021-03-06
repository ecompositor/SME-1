package tutorial

import tutorial.Actions._
import com.jme3.app.{SimpleApplication, SimpleApplicationWrap}
import com.jme3.material.Material
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.shape.Box
import com.jme3.math.ColorRGBA
import com.jme3.input.{Action, InputManagerWrap, KeyInput, MouseInput}
import com.jme3.input.controls.{ActionListener, ActionListenerWrap}
import com.jme3.input.controls.{AnalogListener, AnalogListenerWrap}
import com.jme3.input.controls.KeyTrigger
import com.jme3.input.controls.MouseButtonTrigger
import com.jme3.syntax._

//import cats._
//import cats.instances.all._
//import cats.syntax.eq._


/** Sample 5 - how to map keys and mousebuttons to actions */
class HelloInput extends SimpleApplication {
 
  protected lazy val player: Geometry = Geometry("Player", Box(1, 1, 1))
  var isRunning: Boolean = true

  override def simpleInitApp: Unit = {
    val mat = Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat.setColor("Color", ColorRGBA.Blue)
    player.setMaterial(mat)
    discard{ rootNode.attachChild(player) }
    initKeys() // load my custom keybinding
  }

  /** Custom Keybinding: Map named actions to inputs. */
  def initKeys(): Unit = {
    // You can map one or several inputs to one named action
    inputManager.addMapping(Pause,  new KeyTrigger(KeyInput.KEY_P))
    inputManager.addMapping(Left,   new KeyTrigger(KeyInput.KEY_J))
    inputManager.addMapping(Right,  new KeyTrigger(KeyInput.KEY_K))
    inputManager.addMapping(Rotate, new KeyTrigger(KeyInput.KEY_SPACE),
                                      new MouseButtonTrigger(MouseInput.BUTTON_LEFT))
    // Add the names to the action listener.
    inputManager.addListener(actionListener, Pause)
    inputManager.addListener(analogListener, Left, Right, Rotate)
  }

  private val actionListener = new ActionListener {
    //TODO: think about if it is possible to generate this signature
    //TODO: from typed signature using Scala.Meta
    def onAction(name: String, keyPressed: Boolean, tpf: Float): Unit = {
      if (name == Pause.name && !keyPressed) {
        println("pausing!...")
        isRunning = !isRunning
      }
    }
  }
 
  private val analogListener = new AnalogListener {
    def onAnalog(name: String, value: Float, tpf: Float): Unit = {
      if (isRunning) {
        Action(name) match {
          case Rotate =>
            discard{ player.rotate(0, value*speed, 0) }
            ()
          case Right =>
            val vv = player.getLocalTranslation
            player.setLocalTranslation(vv.x + value*speed, vv.y, vv.z)
          case Left =>
            val vv = player.getLocalTranslation
            player.setLocalTranslation(vv.x - value*speed, vv.y, vv.z)
          case _ => ()
        }
      }
      else {
        println("Press P to unpause.")
      }
    }
  }
}

object HelloInput {
  def main(args:Array[String]): Unit = {

    import java.util.logging.{Logger,Level}
    Logger.getLogger("").setLevel(Level.WARNING)

    val app = new HelloInput
    app.start()
  }
}




