/**
 * Created by owl on 2015/03/11.
 */
package kamiya

object LSystem {
  object Conversions {
    implicit def CharToString(c:Char) = c.toString
    implicit def StringToAxiom(str:String) = Axiom(str)
    implicit def Tuple2ToRule(t:(Char,String)) = Rule(t._1,t._2)

    implicit class CharConversion(val self:Char) {
      def angle(angle:Double) = Angle(self,angle)
    }
  }
  import Conversions._

  case class Grammar(axiom: Axiom, rules:List[Rule]) {
    def generate(implicit src:String = axiom.src) : Stream[String] = src #:: generate(replace(src))
    def replace(src:String) : String = src flatMap {
      c => rules.find(_.src == c) match {
        case Some(r) => r.dst
        case None => c.toString
      }
    }
    def drawOption : Option[DrawOption] = this match {
      case x : DrawOption => Some(x)
      case _ => None
    }
  }
  case class Axiom(src:String)
  case class Rule(src:Char,dst:String)

  trait DrawOption {
    val params : List[DrawParameter]
  }
  abstract class DrawParameter
  case class Angle(src:Char,deg:Double) extends DrawParameter
}
