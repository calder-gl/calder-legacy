/**
 * While.scala
 */
package calder.expressions.constructs

import calder.expressions.Expression
import calder.types.Kind
import calder.types.Type

class While(protected val condition: Expression, protected val loopBlock: Block) extends Expression {
  def returnType(): Type = Kind.Void.asInstanceOf[Type]

  def source(): String = s"while (${condition.source}) ${loopBlock.source}"
}
