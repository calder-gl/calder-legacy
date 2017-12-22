/**
 * InfixSpec.scala
 */

package calder.expressions.math.infix

import org.scalatest._

import calder.Reference
import calder.expressions._
import calder.expressions.math.infix._
import calder.types._
import calder.variables._

class InfixSpec extends FunSpec {
  describe ("Infix Expressions") {
    val lhs = new Reference(new InterfaceVariable(Qualifier.In, new VariableSource(Kind.Int.asInstanceOf[Type], "1")))
    val rhs = new Reference(new InterfaceVariable(Qualifier.In, new VariableSource(Kind.Int.asInstanceOf[Type], "1")))
    val floatVar = new Reference(new InterfaceVariable(Qualifier.In, new VariableSource(Kind.Float.asInstanceOf[Type], "1.5")))

    describe ("source") {
      it ("addition") {
        val operation = new Addition(lhs, rhs)
        assert(operation.source == "1 + 1")
      }

      it ("subtraction") {
        val operation = new Subtraction(lhs, rhs)
        assert(operation.source == "1 - 1")
      }

      it ("multiplication") {
        val operation = new Multiplication(lhs, rhs)
        assert(operation.source == "1 * 1")
      }

      it ("division") {
        val operation = new Division(lhs, rhs)
        assert(operation.source == "1 / 1")
      }

      it ("modulo") {
        val operation = new Modulo(lhs, rhs)
        assert(operation.source == "1 % 1")
      }
    }

    describe ("returnType") {
      val otherIntType = new Reference(new InterfaceVariable(Qualifier.In, new VariableSource(Kind.Int.asInstanceOf[Type], "1")))

      it ("returns float when one of the variables is of type float") {
        val operation = new Subtraction(lhs, floatVar)
        assert(operation.returnType == Kind.Float.asInstanceOf[Type])
      }

      it ("returns int when both of the variables are of type int") {
        val operation = new Subtraction(lhs, rhs)
        assert(operation.returnType == Kind.Int.asInstanceOf[Type])
      }
    }
  }
}
