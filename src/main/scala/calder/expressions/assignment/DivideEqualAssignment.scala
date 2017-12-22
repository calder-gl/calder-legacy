/**
 * DivideEqualAssignment.scala
 */

package calder.expressions.assignment

import calder.expressions.Expression
import calder.expressions.Reference
import calder.expressions.assignment.AssignmentExpression

class DivideEqualAssignment(override val lhs: Reference, override val rhs: Expression) extends AssignmentExpression(lhs, rhs) {
  def source(): String = s"${lhs.source()} /= ${rhs.source()}"
}
