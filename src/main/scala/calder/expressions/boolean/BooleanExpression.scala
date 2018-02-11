/**
 * BooleanExpression.scala
 */
package calder.expressions.boolean

import calder.expressions.Expression
import calder.types.Type
import calder.types.Kind

abstract class BooleanExpression(val lhs: Expression, val rhs: Expression) extends Expression {
  def source(): String

  def returnType(): Type = Kind.Bool.asInstanceOf[Type]
}
