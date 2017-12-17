/**
 * AssignmentExpression.scala
 */

package calder.expressions.assignment

import calder.Reference
import calder.expressions.Expression
import calder.types.Type

abstract class AssignmentExpression(val lhs: Reference, val rhs: Reference) extends Expression {
  def source(): String

  def returnType(): Type = lhs.returnType
}
