/**
 * Expression.scala
 */
package calder.expressions

import calder.SyntaxNode
import calder.types.Type

trait Expression extends SyntaxNode {
  def returnType(): Type
}
