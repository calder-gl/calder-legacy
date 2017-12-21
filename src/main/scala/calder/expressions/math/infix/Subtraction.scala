/**
 * Subtraction.scala
 */

package calder.expressions.math.infix

import calder.Reference
import calder.expressions.math.infix.InfixExpression

class Subtraction(override val lhs: Reference, override val rhs: Reference) extends InfixExpression(lhs, rhs) {
  def source(): String = s"(${lhs.source} - ${rhs.source})"
}
