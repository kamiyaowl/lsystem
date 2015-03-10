
/**
 * Created by kamiya on 2015/03/09.
 * Language : Scala 2.11.6
 */
package kamiya

import kamiya.LSystem._
import kamiya.LSystem.Conversions._
import kamiya.LSystemDrawer.DrawStatus
import kamiya.util.OpenCVWrapper._
import kamiya.util.OpenCVWrapper.Color._
import org.opencv.core.{CvType, Mat}

object Main extends Drawing {
  def main(args: Array[String]): Unit = {
    //val g = Grammar(Axiom("A"),List(Rule('A',"AB"),Rule('B',"A")))
    def test(g:Grammar) = {
      println(g)
      g.drawParam match {
        case Some(option) => println(option.options)
        case None => println("DrawOption is nothing.")
      }
      g.generate.take(5).foreach(println)
      LSystemDrawer.drawWithText(g,g.generate.take(5).reverse.head,DrawStatus(Point.zero,0,10))
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
    test(kochCurve)//0,0 =? 0 10

    val sierpinskiTriangle = new Grammar("A",List('A' -> "B-A-B", 'B' -> "A+B+A")) with DrawParameter {
      override val options: List[DrawOption] = List('+' angle 60,'-' angle -60, 'A' drawForward, 'B' drawForward)
    }
    //test(sierpinskiTriangle)//0,0 =? 90 5

    val dragonCurve = new Grammar("FX", List('X' -> "X+YF+",'Y' -> "-FX-Y")) with DrawParameter {
      override val options: List[DrawOption] = List('+' angle 90, '-' angle -90, 'F' drawForward)
    }
    //test(dragonCurve)

    val fractalPlant = new Grammar("X",List('X' -> "F-[[X]+X]+F[+FX]-X",'F' -> "FF")) with DrawParameter {
      override val options: List[DrawOption] = List('+' angle 25,'-' angle -25, '[' push,']' pop)
    }
   // test(fractalPlant)

  }

  override val title: String = "kamiya"
  override val width: Int = 640
  override val height: Int = 480
}
