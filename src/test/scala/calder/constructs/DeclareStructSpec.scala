/**
 * DeclareStructSpec.scala
 */

package calder.expressions.constructs

import org.scalatest._

import calder.expressions.Reference
import calder.expressions._
import calder.expressions.constructs._
import calder.types._
import calder.variables._

class DeclareStructSpec extends FunSpec {
  val a = new VariableSource(Kind.Bool.asInstanceOf[Type], "lhs")
  val b = new VariableSource(Kind.Bool.asInstanceOf[Type], "rhs")

  describe ("DeclareStruct") {
    describe ("source") {
      it ("is well formed") {
        val structDeclaration = new DeclareStruct("structName", Array(a, b))
        assert(structDeclaration.source == "struct structName { bool a; bool b; }")
      }
    }
  }
}
