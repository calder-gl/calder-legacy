/**
 * XorExpression.scala
 */

package calder.expressions.boolean

import calder.expressions.Expression
import calder.expressions.boolean.BooleanExpression

class XorExpression(override val lhs: Expression, override val rhs: Expression) extends BooleanExpression(lhs, rhs) {
  def source(): String = s"(${lhs.source} ^^ ${rhs.source})"
}
