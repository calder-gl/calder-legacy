/**
 * Statement.scala
 */
package calder.expressions.constructs

import calder.SyntaxNode

class Statement(private val node: SyntaxNode) extends SyntaxNode {
  def source(): String = s"${node.source};"
}
