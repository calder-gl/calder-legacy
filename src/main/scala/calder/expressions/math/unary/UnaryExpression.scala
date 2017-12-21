/**
 * UnaryExpression.scala
 */

package calder.expressions.math.unary

import calder.Reference
import calder.exceptions.TypeException
import calder.expressions.Expression
import calder.types.Kind
import calder.types.Type

abstract class UnaryExpression(val lhs: Reference) extends Expression {
  if (!lhs.returnType.checkEquals("int")) throw new TypeException("Can only perform unary expression on integer type.")

  def source(): String

  def returnType(): Type = Kind.Int.asInstanceOf[Type]
}
