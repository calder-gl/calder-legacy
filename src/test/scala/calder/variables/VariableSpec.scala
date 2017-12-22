/**
 * VariableSpec.scala
 */

package calder.variables

import org.scalatest._

import calder.expressions._
import calder.types._
import calder.variables._

class VariableSpec extends FunSpec {
  describe ("Variable") {
    describe ("declaration") {
      it ("declares basic-type variables correctly") {
        val someVar = new VariableSource(Kind.Bool.asInstanceOf[Type], "someVar")
        assert(someVar.declaration == "bool someVar;")
      }

      it ("declares struct-type variables correctly") {
        val structVar = new VariableSource(
          new Type("aStruct", MetaKind.Struct, Array(Kind.Bool.asInstanceOf[Type], Kind.Int.asInstanceOf[Type])), "structVar"
        )
        assert(structVar.declaration == "aStruct someVar;")
      }

      it ("declares array-type variables correctly") {
        val arrayVar = new VariableSource(new Type("", MetaKind.Array, Array(Kind.Int.asInstanceOf[Type])), "someVar")
        assert(arrayVar.declaration == "int someVar[];")
      }
    }
  }
}
