/**
 * Type.scala
 */

package calder.types

import calder.exceptions.TypeException
import calder.types.Kind._
import calder.types.MetaKind._
import calder.util.ScalaJSArray

import org.scalajs.dom.raw.WebGLRenderingContext
import org.scalajs.dom.raw.WebGLUniformLocation

class Type(private val _name: String, private val _metakind: MetaKind, private val _children: Array[Type]) {
  // Validate children types
  _metakind match {
    case MetaKind.Basic ⇒
      if (_children.length != 0) throw new TypeException("Basic kind cannot have any children types.")
    case MetaKind.Array ⇒
      if (_children.length != 1) throw new TypeException("Array kind must have exactly one child type.")
    case _ ⇒ None
  }

  def name(): String = _name

  def metakind(): MetaKind = _metakind

  def children(): Array[Type] = _children

  // TODO: implement, or scrap in new Type design

  def checkEquals(otherType: String): Boolean = _name == otherType

  def checkVectorEquals(otherType: Type): Boolean = true

  def checkMatrixEquals(otherType: Type): Boolean = true

  def wrapAttributeBufferInTypedArray(value: Array[Any]): Any = None

  def glType(gl: WebGLRenderingContext): Any = None

  def size(): Any = None

  def setUniform[T: ScalaJSArray](gl: WebGLRenderingContext, position: WebGLUniformLocation, value: T): Unit = {
    if (_metakind != MetaKind.Basic) throw new Error("Unsupported attribute type")

    /*
    _name match {
      case Kind.Int   ⇒ gl.uniform1iv(position, value)
      case Kind.IVec2 ⇒ gl.uniform2iv(position, value)
      case Kind.IVec3 ⇒ gl.uniform3iv(position, value)
      case Kind.IVec4 => gl.uniform4iv(position, value)
      case Kind.Float => gl.uniform1fv(position, value)
      case Kind.Vec2  => gl.uniform2fv(position, value)
      case Kind.Vec3  => gl.uniform3fv(position, value)
      case Kind.Vec4  => gl.uniform4fv(position, value)
      case Kind.Mat2  => gl.uniformMatrix2fv(position, false, value)
      case Kind.Mat3  => gl.uniformMatrix3fv(position, false, value)
      case Kind.Mat4  => gl.uniformMatrix4fv(position, false, value)
      case default ⇒ throw new Error("Unsupported uniform type")
    }
    */
  }

  // Type Checking Helper Methods

  def isScalarType(): Boolean =
    _name match {
      case "int" | "float" ⇒ true
      case _ ⇒ false
    }

  def isMatrixType(): Boolean =
    _name match {
      case "mat2" | "mat3" | "mat4" ⇒ true
      case _ ⇒ false
    }

  def isVectorType(): Boolean =
    _name match {
      case "vec2" | "vec3" | "vec4" | "bvec2" | "bvec3" | "bvec4" | "ivec2" | "ivec3" | "ivec4" ⇒ true
      case _ ⇒ false
    }
}
