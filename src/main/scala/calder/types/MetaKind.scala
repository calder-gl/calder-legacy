/**
 * MetaKind.scala
 */

package calder.types

object MetaKind extends Enumeration {
  type MetaKind = Value
  val Basic, Struct, Array = Value
}
