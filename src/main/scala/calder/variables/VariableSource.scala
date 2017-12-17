/**
 * VariableSource.scala
 */

package calder.variables

import calder.types.MetaKind
import calder.types.Type

import org.scalajs.dom.raw.WebGLRenderingContext
import org.scalajs.dom.raw.WebGLUniformLocation

class VariableSource(private val srcType: Type, srcName: String) {
  def getSrcType(): Type = srcType

  def getSrcName(): String = srcName

  def declaration(): String =
    srcType.getMetakind match {
      case MetaKind.Basic  ⇒ s"${srcType.getName} ${srcName};"
      case MetaKind.Struct ⇒ s"${srcType.getName} ${srcName};"
      case MetaKind.Array  ⇒ s"${srcType.getChildren()(0).getName} ${srcName}[];"
    }
}
