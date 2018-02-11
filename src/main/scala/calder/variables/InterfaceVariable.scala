/**
 * InterfaceVariable.scala
 */
package calder.variables

import calder.types.Qualifier
import calder.variables.Variable
import calder.variables.VariableSource

class InterfaceVariable(private val _qualifier: String, private val variableSrc: VariableSource)
    extends Variable(variableSrc) {
  def qualifier(): String = _qualifier

  override def declaration(): String = s"${qualifier} ${super.declaration}"
}
