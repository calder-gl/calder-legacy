/**
 * Function.scala
 */
package calder.expressions.constructs

import calder.expressions.Expression
import calder.expressions.constructs.Statement
import calder.types.Kind
import calder.types.Type
import calder.variables.InterfaceVariable

class Function(private val _name: String,
               private val statements: Array[Statement] = Array(),
               private val parameters: Array[InterfaceVariable] = Array(),
               private val _returnType: Type = Kind.Void.asInstanceOf[Type])
    extends Expression {
  def name(): String = _name

  def returnType(): Type = _returnType

  def source(): String =
    (s"${_returnType.name} $name("
    + parameters.map(parameter => parameter.declaration).mkString(",") + "){\n"
    + statements.map(statement => statement.source).mkString("\n") + "}\n")
}
