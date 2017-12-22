/**
 * DoWhile.scala
 */

package calder.expressions.constructs

import calder.expressions.Expression
import calder.expressions.constructs.Block
import calder.expressions.constructs.While

class DoWhile(override protected val condition: Expression,
              override protected val loopBlock: Block) extends While(condition, loopBlock) {
  override def source(): String = s"do ${loopBlock.source} while (${condition.source})"
}
