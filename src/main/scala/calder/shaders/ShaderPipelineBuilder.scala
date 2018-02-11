/**
 * ShaderPipelineBuilder.scala
 */
package calder.shaders

import calder.shaders.Shader
import calder.shaders.ShaderPipeline
import calder.variables.VariableSource

import org.scalajs.dom.raw.WebGLRenderingContext

class ShaderPipelineBuilder(private val vertexShader: Shader, private val fragmentShader: Shader) {
  private val inputVariables: Set[VariableSource]  = fragmentShader.inputs
  private val outputVariables: Set[VariableSource] = vertexShader.outputs

  /** Public Interface */
  val isWellFormed: Boolean = inputVariables.subsetOf(outputVariables)

  def build(gl: WebGLRenderingContext): ShaderPipeline =
    new ShaderPipeline(gl, vertexShader, fragmentShader)
}
