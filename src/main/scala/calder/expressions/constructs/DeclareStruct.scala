/**
 * DeclareStruct.scala
 */
package calder.expressions.constructs

import calder.expressions.Expression
import calder.types.MetaKind
import calder.types.Type
import calder.variables.VariableSource

class DeclareStruct(private val _name: String, private val members: Array[VariableSource] = Array())
    extends Expression {
  def name(): String = _name

  def returnType(): Type =
    new Type(name, MetaKind.Struct, members.map(member => member.srcType))

  def source(): String =
    s"struct ${_name} {" + members
      .map(member => member.declaration)
      .mkString("\n") + "};"
}
