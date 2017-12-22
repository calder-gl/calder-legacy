/**
 * If.scala
 */

package calder.expressions.constructs

import calder.expressions.Expression
import calder.expressions.constructs.Block
import calder.types.Kind
import calder.types.Type

class If(private val condition: Expression, private val thenBlock: Block, private val elseBlock: Block = new Block()) extends Expression {
  def returnType(): Type = Kind.Void.asInstanceOf[Type]

  def source(): String =
    s"if (${condition.source}) ${thenBlock.source}" + (if (!elseBlock.isEmpty) s"else ${elseBlock.source}" else "")
}
