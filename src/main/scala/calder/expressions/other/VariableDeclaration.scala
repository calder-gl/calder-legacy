/**
 * VariableDeclaration.scala
 */

package calder.expressions.other

import calder.expressions.Expression
import calder.types.Type
import calder.variables.Variable

class VariableDeclaration(private val _variable: Variable) extends Expression {
  def variable(): Variable = _variable

  def returnType(): Type = _variable.getType

  def source(): String = _variable.declaration
}
