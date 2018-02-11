/**
 * VariableSource.scala
 */
package calder.variables

import calder.types.MetaKind
import calder.types.Type

import org.scalajs.dom.raw.WebGLRenderingContext
import org.scalajs.dom.raw.WebGLUniformLocation

class VariableSource(private val _srcType: Type, private val _srcName: String) {
  def srcType(): Type = _srcType

  def srcName(): String = _srcName

  def declaration(): String =
    srcType.metakind match {
      case MetaKind.Basic  => s"${_srcType.name} ${_srcName};"
      case MetaKind.Struct => s"${_srcType.name} ${_srcName};"
      case MetaKind.Array  => s"${_srcType.children()(0).name} ${_srcName}[];"
    }
}
