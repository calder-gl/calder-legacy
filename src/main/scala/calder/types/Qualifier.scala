/**
 * Qualifier.scala
 */

package calder.types

object Qualifier extends Enumeration {
  val Attribute = "attribute"
  val Uniform = "uniform"
  val Varying = "varying"
  val Const = "const"
  val In = "in"
  val Out = "out"
  val InOut = "inout"
}
