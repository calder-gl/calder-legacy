/**
 * InfixExpression.scala
 */

package calder.expressions.math.infix

import calder.expressions.Expression
import calder.types.Kind
import calder.types.Type

abstract class InfixExpression(val lhs: Expression, val rhs: Expression) extends Expression {
  def source(): String

  def returnType(): Type =
    if (atLeastOneSideFloatType) Kind.Float.asInstanceOf[Type]
    else lhs.returnType

  // Type Checking Helper Methods

  protected def eitherSideVectorOrMatrix(lhs: Expression, rhs: Expression): Boolean =
    lhs.returnType.isVectorType || rhs.returnType.isVectorType || lhs.returnType.isMatrixType || rhs.returnType.isMatrixType

  protected def bothSidesScalarTypes(): Boolean =
    lhs.returnType.isScalarType && rhs.returnType.isScalarType

  protected def atLeastOneSideFloatType(): Boolean =
    lhs.returnType.checkEquals("float") || rhs.returnType.checkEquals("float")

  protected def bothSidesIntType(): Boolean =
    lhs.returnType.checkEquals("int") && rhs.returnType.checkEquals("int")
}
