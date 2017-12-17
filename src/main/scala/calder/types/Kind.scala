/**
 * Kind.scala
 */

package calder.types

object Kind extends Enumeration {
  val Void = "void"
  val Bool = "bool"
  val Int = "int"
  val Float = "float"
  val Vec2 = "vec2"
  val Vec3 = "vec3"
  val Vec4 = "vec4"
  val BVec2 = "bvec2"
  val BVec3 = "bvec3"
  val BVec4 = "bvec4"
  val IVec2 = "ivec2"
  val IVec3 = "ivec3"
  val IVec4 = "ivec4"
  val Mat2 = "mat2"
  val Mat3 = "mat3"
  val Mat4 = "mat4"
  val Sampler2D = "sampler2D"
  val SamplerCube = "samplerCube"
}
