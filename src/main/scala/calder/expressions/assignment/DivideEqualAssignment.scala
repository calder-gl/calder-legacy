/**
 * DivideEqualAssignment.scala
 */

package calder.expressions.assignment

import calder.Reference
import calder.expressions.assignment.AssignmentExpression

class DivideEqualAssignment(override val lhs: Reference, override val rhs: Reference) extends AssignmentExpression(lhs, rhs) {
  def source(): String = s"${lhs.source()} /= ${rhs.source()}"
}
