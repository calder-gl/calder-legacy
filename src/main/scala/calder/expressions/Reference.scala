/**
 * Reference.scala
 */
package calder.expressions

import calder.expressions.Expression
import calder.types.Type
import calder.variables.Variable

class Reference(private val variable: Variable) extends Expression {
  def source(): String = variable.name

  def returnType(): Type = variable.getType
}
