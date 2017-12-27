/**
 * FunctionSpec.scala
 */

package calder.expressions.constructs

import org.scalatest._

import calder.expressions.Reference
import calder.expressions._
import calder.expressions.assignment._
import calder.expressions.constructs._
import calder.types._
import calder.variables._

class FunctionSpec extends FunSpec {
  val glPosition = new Reference(
    new InterfaceVariable(Qualifier.In, new VariableSource(Kind.Vec4.asInstanceOf[Type], "glPosition"))
  )
  val vertexPosition = new Reference(
    new InterfaceVariable(Qualifier.In, new VariableSource(Kind.Vec4.asInstanceOf[Type], "vertexPosition"))
  )
  val integerVar = new Reference(
    new InterfaceVariable(Qualifier.In, new VariableSource(Kind.Int.asInstanceOf[Type], "intVar"))
  )

  describe ("Function") {
    describe ("source") {
      it ("creates the correct source") {
        val main = new Function("main", Array(new Statement(new EqualAssignment(glPosition, vertexPosition))))
        assert(main.source == "void main() { glPosition = vertexPosition; }")
      }

      it ("handles different return types") {
        val test = new Function("test", Array(new ReturnStatement(integerVar)), Kind.Int.asInstanceOf[Type])
        assert(test.source == "int test() { return intVar; }")

      }
    }
  }
}
