/**
 * InterfaceVariable.scala
 */

package calder.variables

import calder.types.Qualifier
import calder.variables.Variable
import calder.variables.VariableSource

class InterfaceVariable(private val qualifier: String, private val variableSrc: VariableSource)
    extends Variable(variableSrc) {
  override def declaration(): String = s"${qualifier} ${super.declaration}"
}
