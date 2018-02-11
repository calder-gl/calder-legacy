/**
 * ShaderPipeline.scala
 */
package calder.shaders

import calder.shaders.Shader
import calder.types.Qualifier
import calder.variables.InterfaceVariable

import scala.scalajs.js
import scala.scalajs.js.typedarray.ArrayBuffer
import org.scalajs.dom.raw.WebGLBuffer
import org.scalajs.dom.raw.WebGLProgram
import org.scalajs.dom.raw.WebGLRenderingContext
import org.scalajs.dom.raw.WebGLShader
import org.scalajs.dom.raw.WebGLUniformLocation

class ShaderPipeline(private val gl: WebGLRenderingContext,
                     private val vertexShader: Shader,
                     private val fragmentShader: Shader) {

  /**
   * Public Interface
   */
  var program: WebGLProgram = null

  def setAttribute(input: String, value: Array[Any], usage: Int = WebGLRenderingContext.STATIC_DRAW): Unit = {
    // Get 'interface variable' for input
    val interfaceVariable = attributes(input)
    if (interfaceVariable == null) throw new Error(s"Unknown input $input")

    // Get 'buffer' for input
    val buffer = attributeBuffers(input)
    if (buffer == null) throw new Error(s"$input is not an attribute")

    // Get 'position' for input
    val position = attributePositions(input)
    if (position == null) throw new Error(s"$input is not an attribute")

    // Bind values
    gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, buffer)
    gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER,
                  interfaceVariable
                    .wrapAttributeBufferInTypedArray(value)
                    .asInstanceOf[ArrayBuffer],
                  usage)
    gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, buffer)

    // Set gl.vertexAttribPointer values
    gl.vertexAttribPointer(
      position,
      interfaceVariable.size,
      interfaceVariable.glType(gl),
      false, // TODO: support normalization
      0, // TODO: support nonzero stride
      0 // TODO: support nonzero offset
    )

    gl.enableVertexAttribArray(position)
  }

  // TODO: support variadic args
  def setUniform(input: String, value: Array[Any]): Unit = {
    // Get 'interface variable' for input
    val interfaceVariable = uniforms(input)
    if (interfaceVariable == null) throw new Error(s"Unknown input $input")

    // Get 'position' for input
    val position = uniformPositions(input)
    if (position == null) throw new Error(s"$input is not a uniform")

    interfaceVariable.setUniform(gl, position, value)
  }

  def useProgram(): Unit = gl.useProgram(program)

  def draw(vertices: Int, offset: Int = 0): Unit =
    gl.drawArrays(WebGLRenderingContext.TRIANGLE_STRIP, offset, vertices)

  /**
   * Private Interface
   */
  // Immutable maps
  private val attributes: Map[String, InterfaceVariable] =
    filterAttributeQualifiers
  private val uniforms: Map[String, InterfaceVariable] = filterUniformQualifiers

  // Mutable positions and buffers
  private val attributePositions = scala.collection.mutable.Map[String, Int]()
  private val attributeBuffers =
    scala.collection.mutable.Map[String, WebGLBuffer]()
  private val uniformPositions =
    scala.collection.mutable.Map[String, WebGLUniformLocation]()

  private def compileProgram(vertexShader: Shader, fragmentShader: Shader) {
    val _program = gl.createProgram()

    if (_program == null) throw new Error("Error initializing shader program")
    program = _program

    // Attach vertex and fragment shaders to program
    gl.attachShader(program, compileShader(vertexShader.source, WebGLRenderingContext.VERTEX_SHADER))
    gl.attachShader(program, compileShader(fragmentShader.source, WebGLRenderingContext.FRAGMENT_SHADER))

    gl.linkProgram(program)

    if (gl.getProgramParameter(program, WebGLRenderingContext.LINK_STATUS) == null) {
      val message = gl.getProgramInfoLog(program)
      throw new Error(s"Unable to initialize the shader program: $message")
    }
  }

  private def compileShader(source: String, _type: Int): WebGLShader = {
    val shader = gl.createShader(_type)
    gl.shaderSource(shader, source)
    gl.compileShader(shader)

    if (gl.getShaderParameter(shader, WebGLRenderingContext.COMPILE_STATUS) == null) {
      val message = gl.getShaderInfoLog(shader)
      gl.deleteShader(shader)
      throw new Error(s"An error occurred compiling the shaders: $message")
    }

    if (shader == null) throw new Error("Could not compile shader")

    shader
  }

  private def createBuffers(): Unit = {
    // Set 'uniform' positions
    uniforms.mapValues(uniform => {
      val position = gl.getUniformLocation(program, uniform.name)
      if (position == null)
        throw new Error(s"Unable to find uniform position for ${uniform.name}")
      uniformPositions(uniform.name) = position
    })

    // Set 'attribute' positions and buffers
    attributes.mapValues(attribute => {
      attributePositions(attribute.name) = gl.getAttribLocation(program, attribute.name)
      attributeBuffers(attribute.name) = makeBuffer
    })
  }

  private def makeBuffer(): WebGLBuffer = {
    val buffer = gl.createBuffer()
    if (buffer == null) throw new Error("Error creating buffer")
    buffer
  }

  private def filterAttributeQualifiers(): Map[String, InterfaceVariable] =
    vertexShader.inputDeclarations
      .filter(input => input.variable.qualifier == Qualifier.Attribute)
      .map(input => input.variable.name -> input.variable)
      .toMap

  private def filterUniformQualifiers(): Map[String, InterfaceVariable] =
    vertexShader.inputDeclarations
      .filter(input => input.variable.qualifier == Qualifier.Uniform)
      .map(input => input.variable.name -> input.variable)
      .toMap
}
