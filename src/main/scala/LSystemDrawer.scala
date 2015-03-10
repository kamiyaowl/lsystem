/**
 * Created by owl on 2015/03/11.
 */
package kamiya
import kamiya.LSystem._
import kamiya.util.OpenCVWrapper._

import scala.collection.immutable.Stack

object LSystemDrawer extends Drawing {
  initialize
  //TODO:描画パラメータの受け取り(or描画関数を投げてもらう
  def drawWithText(g:Grammar, text:String, status:DrawStatus) = {
    implicit val mat = createMat
    g.drawParam match {
      case None => ()
      case Some(params) => parse(params.options, text, status).foreach {
        ds => drawNoClear {
          drawLine(ds.p, ds.p2, Color.silver, 3)
        }
      }
    }
  }
  case class DrawStatus(p:Point, angle:Double, r:Double, depth:Int = 1, p2:Point = Point.zero){
    def pushed = DrawStatus(p,angle,r,depth + 1)
    //drawable,next
    def newPoint : (DrawStatus, DrawStatus) = {
      val rad = (angle / 360.0) * 2 * math.Pi
      val nr = r / depth
      val nx = nr * math.cos(rad)
      val ny = nr * math.sin(rad)
      val np = Point(p.x + nx, p.y + ny)
      (DrawStatus(p,angle,r,depth,np), DrawStatus(np,angle,r,depth))
    }
    def newAngle(deg:Double) = DrawStatus(p,angle + deg, r,depth)
  }
  private def parse(options:List[DrawOption],src:String, status:DrawStatus)(implicit stack:Stack[DrawStatus] = Stack.empty) : Stream[DrawStatus] =
    src.headOption match {
      case None => Stream.empty
      case Some(head) => options.find(_.src == head) match {
        case None => parse(options, src.tail,status)( stack)
        case Some(Push(_)) => parse(options, src tail,status pushed)( stack push status)
        case Some(Pop(_)) => {
          val (state2, stack2) = stack.pop2
          parse(options, src tail, state2)( stack2)
        }
        case Some(DrawForward(_)) => {
          val (draw,next) = status.newPoint
          draw #:: parse(options, src tail,next)(stack)
        }
        case Some(Angle(_,deg)) =>
          parse(options, src tail,status newAngle deg)(stack)
      }
    }
  override val width: Int = 1024
  override val title: String = "LSystem"
  override val height: Int = 768
}
