/**
 * AssignmentExpression.scala
 */
package calder.expressions.assignment

import calder.expressions.Expression
import calder.expressions.Reference
import calder.types.Type

abstract class AssignmentExpression(val lhs: Reference, val rhs: Expression) extends Expression {
  def source(): String

  def returnType(): Type = lhs.returnType
}
