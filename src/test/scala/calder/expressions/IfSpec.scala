/**
 * IfSpec.scala
 */

package calder.expressions.constructs

import org.scalatest._

import calder.expressions.Reference
import calder.expressions._
import calder.expressions.assignment._
import calder.expressions.constructs._
import calder.types._
import calder.variables._

class IfSpec extends FunSpec with Matchers {
  describe ("If") {
    val a = new Reference(new InterfaceVariable(Qualifier.In, new VariableSource(Kind.Bool.asInstanceOf[Type], "a")))
    val b = new Reference(new InterfaceVariable(Qualifier.In, new VariableSource(Kind.Bool.asInstanceOf[Type], "b")))
    val c = new Reference(new InterfaceVariable(Qualifier.In, new VariableSource(Kind.Bool.asInstanceOf[Type], "c")))

    describe ("source") {
      it ("has no else block if none is provided") {
        val ifStatement = new If(a, new Block(Array(new Statement(new EqualAssignment(a, b)))))
        assert(ifStatement.source == "if (a) { a=b; }")
      }

      it ("has no else block if an empty block is provided") {
        val ifStatement = new If(a, new Block(Array(new Statement(new EqualAssignment(a, b)))), new Block(Array()))
        assert(ifStatement.source == "if (a) { a=b; }")
      }

      it ("has else block provided") {
        val ifStatement = new If(
          a,
          new Block(Array(new Statement(new EqualAssignment(a, b)))),
          new Block(Array(new Statement(new EqualAssignment(a, c))))
        )
        assert(ifStatement.source == "if (a) { a=b; } else { a=c; }")
      }
    }
  }
}
