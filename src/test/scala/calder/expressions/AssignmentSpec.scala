/**
 * AssignmentSpec.scala
 */

package calder.expressions.assignment

import org.scalatest._

import calder.expressions.Reference
import calder.expressions._
import calder.expressions.assignment._
import calder.types._
import calder.variables._

class AssignmentSpec extends FunSpec {
  describe ("Unary Expressions") {
    val lhs = new Reference(
      new InterfaceVariable(Qualifier.In, new VariableSource(Kind.Bool.asInstanceOf[Type], "lhs"))
    )
    val rhs = new Reference(
      new InterfaceVariable(Qualifier.In, new VariableSource(Kind.Bool.asInstanceOf[Type], "rhs"))
    )

    describe ("source") {
      it ("equal assignment") {
        val operation = new EqualAssignment(lhs, rhs)
        assert(operation.source == "lhs = rhs")
      }

      it ("plus equal assignment") {
        val operation = new PlusEqualAssignment(lhs, rhs)
        assert(operation.source == "lhs += rhs")
      }

      it ("minus equal assignment") {
        val operation = new MinusEqualAssignment(lhs, rhs)
        assert(operation.source == "lhs -= rhs")
      }

      it ("times equal assignment") {
        val operation = new TimesEqualAssignment(lhs, rhs)
        assert(operation.source == "lhs *= rhs")
      }

      it ("divide equal assignment") {
        val operation = new DivideEqualAssignment(lhs, rhs)
        assert(operation.source == "lhs /= rhs")
      }
    }
  }
}
