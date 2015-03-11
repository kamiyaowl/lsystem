
/**
 * Created by kamiya on 2015/03/09.
 * Language : Scala 2.11.6
 */
package kamiya

import kamiya.LSystem._
import kamiya.LSystem.Conversions._
import kamiya.LSystemDrawer._
import kamiya.util.OpenCVWrapper._
import org.opencv.core.{Core, CvType, Mat}
import org.opencv.highgui.Highgui._

object Main extends Drawing {
  def main(args: Array[String]): Unit = {
    def test(g:Grammar)(implicit start: Point = Point.zero, angle: Double = 0, r: Double = 10, color: Color = Color.green, thickness: Int = 3,gen:Int = 5) = {
      println(g)
      g.drawParam match {
        case Some(option) => println(option.options)
        case None => println("DrawOption is nothing.")
      }
      val result = g.drawStep(g.generate.take(gen).reverse.head)(start = start,angle = angle, r = r,color = color, thickness =  thickness)
      imwrite("result.png",result)
    }
    val algae = Grammar("A",List('A' -> "AB", 'B' -> "A"))
    //test(algae)

    val pythagorasThree = Grammar("0",List('1' -> "11", '0' -> "1[0]0"))
    //test(pythagorasThree)

    val cantorDust = Grammar("A",List('A' -> "ABA", 'B' -> "BBB"))
    //test(cantorDust)

    val kochCurve = new Grammar("F",List('F' -> "F+F-F-F+F")) with DrawParameter {
      override val options: List[DrawOption] = List('+' angle 90,'-' angle -90, 'F' drawForward)
    }
    //test(kochCurve)(color = yellow)

    val sierpinskiTriangle = new Grammar("A",List('A' -> "B-A-B", 'B' -> "A+B+A")) with DrawParameter {
      override val options: List[DrawOption] = List('+' angle 60,'-' angle -60, 'A' drawForward, 'B' drawForward)
    }
//    test(sierpinskiTriangle)(start = Point(width / 4, height / 8),angle = 90,r = 5,gen = 8,color = gray)//0,0 =? 90 5

    val dragonCurve = new Grammar("FX", List('X' -> "X+YF+",'Y' -> "-FX-Y")) with DrawParameter {
      override val options: List[DrawOption] = List('+' angle 90, '-' angle -90, 'F' drawForward)
    }
//    test(dragonCurve)(start = Point(width / 2,height / 4), gen = 12, color = red)

    val fractalPlant = new Grammar("X",List('X' -> "F-[[X]+X]+F[+FX]-X",'F' -> "FF")) with DrawParameter {
      override val options: List[DrawOption] = List('+' angle 25,'-' angle -25, '[' push,']' pop,'F' drawForward)
    }
//   test(fractalPlant)(start = Point(0, height),angle = -45,gen = 8, thickness = 1,r = 3,color = lime)

    val fractalPlant2 = new Grammar("X",List('X' -> "F[+X][-X]FX",'F' -> "FF")) with DrawParameter {
      override val options: List[DrawOption] = List('+' angle 30,'-' angle -30, '[' push,']' pop,'F' drawForward)
    }
    test(fractalPlant2)(start = Point(width / 2, height),angle = -90,gen = 8, thickness = 1,r = 3,color = Color.fuchsia)
  }

  override val title: String = "kamiya"
  override val width: Int = 1024
  override val height: Int = 768
}
