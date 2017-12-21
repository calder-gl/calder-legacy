/**
 * BooleanExpression.scala
 */

package calder.expressions.boolean

import calder.Reference
import calder.expressions.Expression
import calder.types.Type
import calder.types.Kind

abstract class BooleanExpression(val lhs: Reference, val rhs: Reference) extends Expression {
  def source(): String

  def returnType(): Type = Kind.Bool.asInstanceOf[Type]
}
