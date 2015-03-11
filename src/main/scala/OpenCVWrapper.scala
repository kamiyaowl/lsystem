/**
 * Created by owl on 2015/03/09.
 */
package kamiya.util

import org.opencv.core.{Point => CvPoint, Rect => CvRect, _}
import org.opencv.core.Core._
import com.atul.JavaOpenCV.Imshow
import scala.collection.JavaConversions._

object OpenCVWrapper {
  object Conversions {
    implicit def DoubleToInt(x: Double): Int = x.toInt
    implicit def IntToDouble(x: Int): Int = x.toDouble
    implicit def PointToCvPoint(src: Point): CvPoint = new CvPoint(src.x, src.y)
    implicit def Tuple2ToPoint[T <% Double](p: (T, T)): Point = Point(p._1, p._2)
    implicit def RectToCvRect(src: Rect): CvRect = new CvRect(src.x, src.y, src.w, src.h)
    implicit def ColorToCvScalar(src: Color): Scalar = new Scalar(src.b, src.g, src.r)
    implicit def PointsToMatOfPoint(xp: List[Point]) = new MatOfPoint(xp.map(PointToCvPoint).toArray: _*)
  }
  import Conversions._

  case class Point(x:Double,y:Double) {
    def toRect(p:Point) = Rect((x + p.x) / 2,(y + p.y) / 2,math.abs(p.x - x),math.abs(p.y - y))
    def <==>(p:Point) = toRect(p)
  }
  object Point{
    def zero = Point(0,0)
  }
  case class Rect(x:Double,y:Double,w:Double,h:Double)
  case class Color(r:Double,g:Double,b:Double)
  object Color {
    def black = Color(0,0,0)
    def maroon = Color(0x80,0,0)
    def green = Color(0,0x80,0)
    def navy = Color(0,0,0x80)
    def gray = Color(0x80,0x80,0x80)
    def red = Color(0xff,0,0)
    def lime = Color(0,0xff,0)
    def blue = Color(0,0,0xff)
    def silver = Color(0xc0,0xc0,0xc0)
    def olive = Color(0x80,0x80,0)
    def teal = Color(0,0x80,0x80)
    def purple = Color(0x80,0,0x80)
    def white = Color(0xff,0xff,0xff)
    def yellow = Color(0xff,0xff,0)
    def aqua = Color(0,0xff,0xff)
    def fuchsia = Color(0xff,0,0xff)
  }

  trait Drawing {
    val width:Int
    val height:Int
    val title:String

    var win : Imshow = null
    def initialize : Unit = {
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
      win = new Imshow(title)
    }
    def createMat = new Mat(height,width,CvType.CV_8UC3)
    def draw(f: => Unit)(implicit mat:Mat) : Unit = {
      clear
      f
      win.showImage(mat)
    }
    def drawNoClear(f: => Unit)(implicit mat:Mat) : Unit = {
      f
      win.showImage(mat)
    }
    def show(implicit mat:Mat) : Unit = {
      win.showImage(mat)
    }
    def drawHidden(f: => Unit)(implicit mat:Mat) : Unit = {
      f
    }
    def delay(ms:Long) = Thread.sleep(ms)
    def drawCircle(p:Point,r:Int,c:Color,thickness:Int)(implicit mat:Mat) = circle(mat,p,r,c,thickness,LINE_AA,0)
    def drawLine(p1:Point,p2:Point,c:Color,thickness:Int)(implicit mat:Mat) = line(mat,p1,p2,c,thickness,LINE_AA,0)
    def drawRect(p1:Point,p2:Point,c:Color,thickness:Int)(implicit mat:Mat) = rectangle(mat,p1,p2,c,thickness,LINE_AA,0)
    def clear(implicit mat:Mat) = drawRect(Point.zero,Point(mat.size.width,mat.size.height),Color.black,-1)
    def drawArrowLine(p1:Point,p2:Point,c:Color,thickness:Int)(implicit mat:Mat) = arrowedLine(mat,p1,p2,c,thickness,LINE_AA,0,0.1)
    def drawPolyLines(xp:List[Point],isClose:Boolean,c:Color,thickness:Int)(implicit mat:Mat) = polylines(mat,List(PointsToMatOfPoint(xp)),isClose,c,thickness,LINE_AA,0)
    def drawFillPoly(xp:List[Point],c:Color,thickness:Int)(implicit mat:Mat) = fillPoly(mat,List(PointsToMatOfPoint(xp)),c)
  }
}
