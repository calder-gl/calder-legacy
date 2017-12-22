/**
 * PrefixDecrement.scala
 */

package calder.expressions.other

import calder.expressions.Reference
import calder.expressions.Expression
import calder.types.Type

class Comma(val lhs: Reference, val rhs: Expression) extends Expression {
  def source(): String = s"${lhs.source}, ${rhs.source}"

  def returnType(): Type = rhs.returnType
}
