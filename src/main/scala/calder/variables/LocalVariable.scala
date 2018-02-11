/**
 * LocalVariable.scala
 */
package calder.variables

import calder.variables.Variable
import calder.variables.VariableSource

class LocalVariable(private val variableSrc: VariableSource) extends Variable(variableSrc) {}
