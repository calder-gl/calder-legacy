/**
 * Variable.scala
 */

package calder.variables

import calder.expressions.Expression
import calder.types.Type
import calder.util.ScalaJSArray
import calder.variables.VariableSource

import org.scalajs.dom.raw.WebGLRenderingContext
import org.scalajs.dom.raw.WebGLUniformLocation

class Variable(private val _variableSrc: VariableSource) {
  def variableSource(): VariableSource = _variableSrc

  def declaration(): String = _variableSrc.declaration

  def name(): String = _variableSrc.srcName

  def getType(): Type = _variableSrc.srcType

  def wrapAttributeBufferInTypedArray(value: Array[Any]): Any = getType.wrapAttributeBufferInTypedArray(value)

  def glType(gl: WebGLRenderingContext): Any = getType.glType(gl)

  def size(): Any = getType.size

  def setUniform[T: ScalaJSArray](gl: WebGLRenderingContext, position: WebGLUniformLocation, value: T): Unit =
    getType.setUniform(gl, position, value)
}
