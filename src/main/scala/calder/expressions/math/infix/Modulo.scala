/**
 * Modulo.scala
 */

package calder.expressions.math.infix

import calder.Reference
import calder.exceptions.TypeException
import calder.expressions.math.infix.InfixExpression

class Modulo(override val lhs: Reference, override val rhs: Reference) extends InfixExpression(lhs, rhs) {
  if (!super.bothSidesIntType) throw new TypeException("LHS and RHS must be of type Int.")

  def source(): String = s"(${lhs.source} % ${rhs.source})"
}
