/**
 * PrefixDecrement.scala
 */

package calder.expressions.other

import calder.Reference
import calder.expressions.Expression
import calder.types.Type

class Comma(val lhs: Reference, val rhs: Reference) extends Expression {
  def source(): String = s"${lhs.source}, ${rhs.source}"

  def returnType(): Type = rhs.returnType
}
