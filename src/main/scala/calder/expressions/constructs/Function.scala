/**
 * Function.scala
 */

package calder.expressions.constructs

import calder.expressions.Expression
import calder.expressions.constructs.Statement
import calder.types.Kind
import calder.types.Type

class Function(private val _name: String, private val _returnType: Type,
               private val statements: Array[Statement] = Array()) extends Expression {
  def name(): String = _name

  def returnType(): Type = _returnType

  def source(): String = (s"${_returnType.name} $name() {\n"
                            + statements.map(statement ⇒ statement.source).mkString("\n") + "}\n")
}
