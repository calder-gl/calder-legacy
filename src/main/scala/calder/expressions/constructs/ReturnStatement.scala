/**
 * ReturnStatement.scala
 */

package calder.expressions.constructs

import calder.expressions.Expression
import calder.types.Type

class ReturnStatement(private val expression: Expression) extends Expression {
  def returnType(): Type = expression.returnType

  def source(): String = s"return ${expression.source};"
}
