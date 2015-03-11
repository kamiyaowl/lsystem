/**
 * Created by owl on 2015/03/11.
 */
package kamiya
import kamiya.LSystem._
import kamiya.util.OpenCVWrapper._
import org.opencv.core.Mat

import scala.collection.immutable.Stack

object LSystemDrawer extends Drawing {
  initialize

  case class DrawStatus(p: Point, angle: Double, r: Double, depth: Int = 1, p2: Point = Point.zero) {
    def pushed = DrawStatus(p, angle, r, depth + 1)

    //drawable,next
    def newPoint: (DrawStatus, DrawStatus) = {
      val rad = (angle / 360.0) * 2 * math.Pi
      val nr = r / depth
      val nx = nr * math.cos(rad)
      val ny = nr * math.sin(rad)
      val np = Point(p.x + nx, p.y + ny)
      (DrawStatus(p, angle, r, depth, np), DrawStatus(np, angle, r, depth))
    }

    def newAngle(deg: Double) = DrawStatus(p, angle + deg, r, depth)
  }
  def parse(options: List[DrawOption], src: String, status: DrawStatus)(implicit stack: Stack[DrawStatus] = Stack.empty): Stream[DrawStatus] =
    src.headOption match {
      case None => Stream.empty
      case Some(head) => options.find(_.src == head) match {
        case None => parse(options, src tail, status)(stack)
        case Some(Push(_)) => parse(options, src tail, status pushed)(stack push status)
        case Some(Pop(_)) => {
          val (state2, stack2) = stack.pop2
          parse(options, src tail, state2)(stack2)
        }
        case Some(DrawForward(_)) => {
          val (draw, next) = status.newPoint
          draw #:: parse(options, src tail, next)(stack)
        }
        case Some(Angle(_, deg)) =>
          parse(options, src tail, status newAngle deg)(stack)
      }
    }
  //TODO:Optimize
  implicit class LSystemGrammarDrawer(val self:Grammar) {
    def drawStep(text: String)(implicit start: Point = Point.zero, angle: Double = 0, r: Double = 10, color: Color = Color.green, thickness: Int = 3) : Mat = {
      implicit val mat = createMat
      val initStatus = DrawStatus(start, angle, r)
      var count = 0//Debug
      self.drawParam match {
        case None => ()
        case Some(params) => parse(params.options, text, initStatus).foreach {
          ds => drawNoClear {
            drawLine(ds.p, ds.p2, color, thickness)
            count += 1
          }
        }
      }
      println(s"total = $count")
      mat
    }
    def drawAll(text: String)(implicit start: Point = Point.zero, angle: Double = 0, r: Double = 10, color: Color = Color.green, thickness: Int = 3) : Mat = {
      implicit val mat = createMat
      val initStatus = DrawStatus(start, angle, r)
      self.drawParam match {
        case None => ()
        case Some(params) => parse(params.options, text, initStatus).foreach {
          ds => drawHidden {
            drawLine(ds.p, ds.p2, color, thickness)
          }
        }
      }
      show { mat }
      mat
    }
  }
  override val width: Int = 1024
  override val title: String = "LSystem"
  override val height: Int = 768
}
