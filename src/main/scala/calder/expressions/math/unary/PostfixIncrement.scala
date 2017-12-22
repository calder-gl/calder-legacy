/**
 * PostfixIncrement.scala
 */

package calder.expressions.math.unary

import calder.expressions.Reference
import calder.expressions.math.unary.UnaryExpression

class PostfixIncrement(override val lhs: Reference) extends UnaryExpression(lhs) {
  def source(): String = s"${lhs.source}++"
}
