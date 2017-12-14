/**
 * Expression.scala
 */

package calder.expressions

import calder.SyntaxNode
import calder.Type

trait Expression extends SyntaxNode {
  def returnType(): Type
}
