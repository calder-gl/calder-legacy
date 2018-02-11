/**
 * VariableDeclaration.scala
 */
package calder.expressions.other

import calder.expressions.Expression
import calder.types.Type
import calder.variables.InterfaceVariable

class VariableDeclaration(private val _variable: InterfaceVariable) extends Expression {
  def variable(): InterfaceVariable = _variable

  def returnType(): Type = _variable.getType

  def source(): String = _variable.declaration
}
