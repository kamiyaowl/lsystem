
/**
 * Created by kamiya on 2015/03/09.
 * Language : Scala 2.11.6
 */
package kamiya
import kamiya.util.OpenCVWrapper._
import kamiya.util.OpenCVWrapper.Color._
import org.opencv.core.{CvType, Mat}

object Main extends DrawApplication {
  def main(args: Array[String]): Unit = {
    initialize
    implicit val mat = createMat

    draw {
      drawCircle(Point(320,240),100,red,-1)
      drawLine(Point(0,0),Point(200,200),green,5)
      drawRect(Point(220,10),Point(400,200),purple,5)
      drawArrowLine(Point(400,300),Point(600,50),yellow,3)
      drawPolyLines(List(Point(0,0),Point(200,100),Point(0,200),Point(200,300)),false,white,5)
      drawFillPoly(List(Point(500,500),Point(200,100),Point(500,200),Point(200,300)),aqua,5)
    }
  }

  override val title: String = "kamiya"
  override val width: Int = 640
  override val height: Int = 480
}
