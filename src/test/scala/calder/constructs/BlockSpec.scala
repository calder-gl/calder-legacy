/**
 * BlockSpec.scala
 */

package calder.expressions.constructs

import org.scalatest._

import calder.expressions.Reference
import calder.expressions._
import calder.expressions.assignment._
import calder.types._
import calder.variables._

class BlockSpec extends FunSpec {
  val a = new Reference(
    new InterfaceVariable(Qualifier.In, new VariableSource(Kind.Int.asInstanceOf[Type], "lhs"))
  )
  val b = new Reference(
    new InterfaceVariable(Qualifier.In, new VariableSource(Kind.Int.asInstanceOf[Type], "rhs"))
  )

  describe ("Block") {
    describe ("source") {
      it ("references all included statements") {
        val block = new Block(Array(new Statement(a), new Statement(b)))
        assert(block.source == "{a; b;}")
      }

      it ("handles an empty block") {
        val block = new Block()
        assert(block.source == "{}")
      }
    }
  }

  describe ("isEmpty") {
    it ("correctly detects a non-empty block") {
      val block = new Block(Array(new Statement(a), new Statement(b)))
      assert(block.isEmpty == false)
    }

    it ("correctly detects an empty block") {
      val block = new Block()
      assert(block.isEmpty == true)
    }
  }
}
