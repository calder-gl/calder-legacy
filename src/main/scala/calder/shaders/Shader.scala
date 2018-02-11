/**
 * Shader.scala
 */
package calder.shaders

import calder.SyntaxNode
import calder.expressions.constructs.Function
import calder.expressions.other.VariableDeclaration
import calder.variables.InterfaceVariable
import calder.variables.VariableSource

class Shader(val main: Function = new Function("main"),
             val inputDeclarations: Array[VariableDeclaration] = Array(),
             val outputDeclarations: Array[VariableDeclaration] = Array())
    extends SyntaxNode {
  def source(): String =
    ("precision mediump float;\n"
    + inputDeclarations.map(input => input.source).mkString("\n") + "\n"
    + outputDeclarations.map(output => output.source).mkString("\n") + "\n"
    + main.source)

  def inputs(): Set[VariableSource] =
    inputDeclarations
      .map(declaration => declaration.variable.variableSource)
      .toSet

  def outputs(): Set[VariableSource] =
    outputDeclarations
      .map(declaration => declaration.variable.variableSource)
      .toSet
}
