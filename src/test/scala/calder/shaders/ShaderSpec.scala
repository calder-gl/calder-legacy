/**
 * ShaderSpec.scala
 */

package calder.expressions.shaders

import org.scalatest._

import calder.expressions.Reference
import calder.expressions._
import calder.expressions.assignment._
import calder.expressions.constructs._
import calder.expressions.other._
import calder.shaders._
import calder.types._
import calder.variables._

class ShaderSpec extends FunSpec {
  def basicShader(): Shader = {
    val glPosition = new Reference(
      new InterfaceVariable(Qualifier.Out, new VariableSource(Kind.Vec4.asInstanceOf[Type], "a"))
    )
    val vertexPosition = new InterfaceVariable(Qualifier.Attribute, new VariableSource(Kind.Vec4.asInstanceOf[Type], "vertexPosition"))

    // Return a newly built shader
    new Shader(
      new Function("main", Array(new Statement(new EqualAssignment(glPosition, new Reference(vertexPosition))))),
      Array(),
      Array(new VariableDeclaration(vertexPosition))
    )
  }

  describe ("Shader") {
    describe ("source") {
      it ("generates the expected source code") {
        val shader = basicShader
        val expected = """
          precision mediump float;
          attribute vec4 vertexPosition;

          void main() {
              gl_Position = vertexPosition;
          }
        """
        assert(shader.source == expected)
      }
    }
  }
}
