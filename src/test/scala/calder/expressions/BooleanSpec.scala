/**
 * BooleanSpec.scala
 */

package calder.expressions.math.unary

import org.scalatest._

import calder.Reference
import calder.expressions._
import calder.expressions.boolean._
import calder.types._
import calder.variables._

class BooleanSpec extends FunSpec {
  describe ("Unary Expressions") {
    val lhs = new Reference(
      new InterfaceVariable(Qualifier.In, new VariableSource(Kind.Bool.asInstanceOf[Type], "lhs"))
    )
    val rhs = new Reference(
      new InterfaceVariable(Qualifier.In, new VariableSource(Kind.Bool.asInstanceOf[Type], "rhs"))
    )

    describe ("source") {
      it ("and expression") {
        val operation = new AndExpression(lhs, rhs)
        assert(operation.source == "(lhs && rhs)")
      }

      it ("equal expression") {
        val operation = new EqualExpression(lhs, rhs)
        assert(operation.source == "(lhs == rhs)")
      }

      it ("greater than equal expression") {
        val operation = new GreaterThanEqualExpression(lhs, rhs)
        assert(operation.source == "(lhs >= rhs)")
      }

      it ("greater than expression") {
        val operation = new GreaterThanExpression(lhs, rhs)
        assert(operation.source == "(lhs > rhs)")
      }

      it ("less than equal expression") {
        val operation = new LessThanEqualExpression(lhs, rhs)
        assert(operation.source == "(lhs <= rhs)")
      }

      it ("less than expression") {
        val operation = new LessThanExpression(lhs, rhs)
        assert(operation.source == "(lhs < rhs)")
      }

      it ("not equal expression") {
        val operation = new NotEqualExpression(lhs, rhs)
        assert(operation.source == "(lhs != rhs)")
      }

      it ("or expression") {
        val operation = new OrExpression(lhs, rhs)
        assert(operation.source == "(lhs || rhs)")
      }

      it ("xor expression") {
        val operation = new XorExpression(lhs, rhs)
        assert(operation.source == "(lhs ^^ rhs)")
      }

      it ("combined 1") {
        val operation = new OrExpression(new AndExpression(lhs, rhs), new NotEqualExpression(lhs, rhs))
        assert(operation.source == "((lhs && rhs) || (lhs != rhs))")
      }
    }
  }
}
