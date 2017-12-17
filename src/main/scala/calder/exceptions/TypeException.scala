/**
 * Type.scala
 */

package calder.exceptions

case class TypeException(private val message: String = "", private val cause: Throwable = None.orNull)
    extends Exception(message, cause)
