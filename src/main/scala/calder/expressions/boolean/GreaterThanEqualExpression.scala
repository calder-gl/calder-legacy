/**
 * GreaterThanEqualExpression.scala
 */

package calder.expressions.boolean

import calder.Reference
import calder.expressions.boolean.BooleanExpression

class GreaterThanEqualExpression(override val lhs: Reference, override val rhs: Reference) extends BooleanExpression(lhs, rhs) {
  def source(): String = s"(${lhs.source} >= ${rhs.source})"
}