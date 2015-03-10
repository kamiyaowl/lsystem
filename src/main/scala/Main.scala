
/**
 * Created by kamiya on 2015/03/09.
 * Language : Scala 2.11.6
 */
package kamiya

import kamiya.LSystem._
import kamiya.LSystem.Conversions._
import kamiya.util.OpenCVWrapper._
import kamiya.util.OpenCVWrapper.Color._
import org.opencv.core.{CvType, Mat}

object Main extends DrawApplication {
  def main(args: Array[String]): Unit = {
    //val g = Grammar(Axiom("A"),List(Rule('A',"AB"),Rule('B',"A")))
    def test(g:Grammar) = {
      println(g)
      g.drawOption match {
        case Some(option) => println(option.params)
        case None => println("DrawOption is nothing.")
      }
      g.generate.take(5).foreach(println)
    }
    val algae = Grammar("A",List('A' -> "AB", 'B' -> "A"))
    test(algae)

    val pythagorasThree = Grammar("0",List('1' -> "11", '0' -> "1[0]0"))
    test(pythagorasThree)

    val cantorDust = Grammar("A",List('A' -> "ABA", 'B' -> "BBB"))
    test(cantorDust)

    val kochCurve = new Grammar("F",List('F' -> "F+F-F-F+F")) with DrawOption {
      override val params: List[DrawParameter] = List('+' angle 90,'-' angle -90)
    }
    test(kochCurve)

    val sierpinskiTriangle = new Grammar("A",List('A' -> "B-A-B", 'B' -> "A+B+A")) with DrawOption {
      override val params: List[DrawParameter] = List('+' angle 60,'-' angle -60)
    }
    test(sierpinskiTriangle)

    //val dragonCurve = new Grammar("FX", List('X' -> "X+YF+",'Y' -> "-FX-Y"))
//
//
//    initialize
//    implicit val mat = createMat
//
//    draw {
//      drawCircle(Point(320,240),100,red,-1)
//      drawLine(Point(0,0),Point(200,200),green,5)
//      drawRect(Point(220,10),Point(400,200),purple,5)
//      drawArrowLine(Point(400,300),Point(600,50),yellow,3)
//      drawPolyLines(List(Point(0,0),Point(200,100),Point(0,200),Point(200,300)),false,white,5)
//      drawFillPoly(List(Point(500,500),Point(200,100),Point(500,200),Point(200,300)),aqua,5)
//    }
  }

  override val title: String = "kamiya"
  override val width: Int = 640
  override val height: Int = 480
}
