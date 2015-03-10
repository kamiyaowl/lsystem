/**
 * Created by owl on 2015/03/11.
 */
package kamiya

object LSystem {
  object Conversions {
    implicit def CharToString(c:Char) = c.toString
    implicit def StringToAxiom(str:String) = Axiom(str)
    implicit def Tuple2ToRule(t:(Char,String)) = Rule(t._1,t._2)
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
  }
  case class Axiom(src:String)
  case class Rule(src:Char,dst:String)

}
