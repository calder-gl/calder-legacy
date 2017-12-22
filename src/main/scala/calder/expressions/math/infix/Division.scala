/**
 * Division.scala
 */

package calder.expressions.math.infix

import calder.expressions.Expression
import calder.expressions.math.infix.InfixExpression

class Division(override val lhs: Expression, override val rhs: Expression) extends InfixExpression(lhs, rhs) {
  def source(): String = s"(${lhs.source} / ${rhs.source})"
}
