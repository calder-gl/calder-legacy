/**
 * Block.scala
 */
package calder.expressions.constructs

import calder.expressions.Expression
import calder.expressions.constructs.Statement
import calder.types.Kind
import calder.types.Type

class Block(private val statements: Array[Statement] = Array()) extends Expression {
  def isEmpty(): Boolean = statements.length == 0

  def returnType(): Type = Kind.Void.asInstanceOf[Type]

  def source(): String =
    "{" + statements.map(statement => statement.source).mkString("\n") + "}"
}
