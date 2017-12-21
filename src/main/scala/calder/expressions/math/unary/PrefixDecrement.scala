/**
 * PrefixDecrement.scala
 */

package calder.expressions.math.unary

import calder.Reference
import calder.expressions.math.unary.UnaryExpression

class PrefixDecrement(override val lhs: Reference) extends UnaryExpression(lhs) {
  def source(): String = s"--${lhs.source}"
}
