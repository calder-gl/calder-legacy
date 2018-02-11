/**
 * ReturnStatement.scala
 */
package calder.expressions.constructs

import calder.SyntaxNode
import calder.expressions.Expression
import calder.types.Type

class ReturnStatement(private val expression: Expression) extends Statement(expression.asInstanceOf[SyntaxNode]) {
  def returnType(): Type = expression.returnType

  override def source(): String = s"return ${expression.source};"
}
