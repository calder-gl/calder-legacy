/**
 * ScalaJSTypes.scala
 */

package calder.util

class ScalaJSArray[T]
object ScalaJSArray {
  implicit object JSIntArray    extends ScalaJSArray[scala.scalajs.js.Array[Int]]
  implicit object JSDoubleArray extends ScalaJSArray[scala.scalajs.js.Array[Double]]
}
