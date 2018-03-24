const canvas = document.getElementById('stage');
const gl = canvas.getContext('webgl');
const ext = gl.getExtension('WEBGL_draw_buffers');
const extFloat = gl.getExtension('OES_texture_float');
const extDepth = gl.getExtension('WEBGL_depth_texture');
console.log(extDepth);

const vertexShaderSource = `
precision highp float;
attribute vec3 vertexPosition;
attribute vec3 vertexNormal;
uniform mat4 camera;
uniform mat4 projection;
uniform mat4 teapotTransform;

varying vec4 diffuse;
varying vec4 normal;
varying vec4 position;

void main() {
  vec4 transformedPosition = teapotTransform * vec4(vertexPosition, 1.0);
  vec4 transformedNormal = teapotTransform * vec4(vertexNormal, 0.0);

  diffuse = vec4(1.0, 1.0, 1.0, 1.0);
  normal = camera * transformedNormal;
  position = camera * transformedPosition;

  gl_Position = projection * position;
}
`;

const fragmentShaderSource = `
#extension GL_EXT_draw_buffers : require
precision mediump float;

varying vec4 diffuse;
varying vec4 normal;
varying vec4 position;

void main() {
  // Diffuse
  gl_FragData[0] = vec4(diffuse.xyz, 1.0);

  // Normal
  gl_FragData[1] = vec4(normalize(normal.xyz), 1.0);

  // Position
  gl_FragData[2] = vec4(position.xyz, 1.0);
}
`;

const fragmentShader = gl.createShader(gl.FRAGMENT_SHADER);
gl.shaderSource(fragmentShader, fragmentShaderSource);
gl.compileShader(fragmentShader);

const vertexShader = gl.createShader(gl.VERTEX_SHADER);
gl.shaderSource(vertexShader, vertexShaderSource);
gl.compileShader(vertexShader);

const geometryPass = gl.createProgram();
gl.attachShader(geometryPass, vertexShader);
gl.attachShader(geometryPass, fragmentShader);
gl.linkProgram(geometryPass);

const vertexShaderSourceFinal = `
precision highp float;
attribute vec2 vertexPosition;

void main() {
  gl_Position = vec4(vertexPosition, 0.0, 1.0);
}
`;

const fragmentShaderSourceFinal = `
precision mediump float;

uniform sampler2D diffuseBuf;
uniform sampler2D normalBuf;
uniform sampler2D positionBuf;

uniform vec2 screenSize;

void main() {
  vec2 texCoords = gl_FragCoord.xy * vec2(1.0/screenSize.x, 1.0/screenSize.y);

  vec4 diffuseBufInfo = texture2D(diffuseBuf, texCoords);
  vec4 normalBufInfo = texture2D(normalBuf, texCoords);
  vec4 positionBufInfo = texture2D(positionBuf, texCoords);

  gl_FragColor = vec4(positionBufInfo.xyz, 1.0);
  //gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
}
`;

const fragmentShaderFinal = gl.createShader(gl.FRAGMENT_SHADER);
gl.shaderSource(fragmentShaderFinal, fragmentShaderSourceFinal);
gl.compileShader(fragmentShaderFinal);

const vertexShaderFinal = gl.createShader(gl.VERTEX_SHADER);
gl.shaderSource(vertexShaderFinal, vertexShaderSourceFinal);
gl.compileShader(vertexShaderFinal);

const finalPass = gl.createProgram();
gl.attachShader(finalPass, vertexShaderFinal);
gl.attachShader(finalPass, fragmentShaderFinal);
gl.linkProgram(finalPass);

const infoFinal = {
  diffuseBuf: gl.getUniformLocation(finalPass, 'diffuseBuf'),
  normalBuf: gl.getUniformLocation(finalPass, 'normalBuf'),
  positionBuf: gl.getUniformLocation(finalPass, 'positionBuf'),
  screenSize: gl.getUniformLocation(finalPass, 'screenSize'),
  vertexPosition: gl.getAttribLocation(finalPass, 'vertexPosition'),
};

const positionFinal = gl.createBuffer();
gl.bindBuffer(gl.ARRAY_BUFFER, positionFinal);
gl.bufferData(
  gl.ARRAY_BUFFER,
  new Float32Array([
    -1.0, -1.0,
    1.0, -1.0,
    -1.0, 1.0,
    1.0, 1.0]),
  gl.STATIC_DRAW);


const info = {
  camera: gl.getUniformLocation(geometryPass, 'camera'),
  projection: gl.getUniformLocation(geometryPass, 'projection'),
  teapotTransform: gl.getUniformLocation(geometryPass, 'teapotTransform'),
  vertexPosition: gl.getAttribLocation(geometryPass, 'vertexPosition'),
  vertexNormal: gl.getAttribLocation(geometryPass, 'vertexNormal'),
};

const fb = gl.createFramebuffer();
gl.bindFramebuffer(gl.FRAMEBUFFER, fb);

const diffuseBuf = gl.createTexture();
const normalBuf = gl.createTexture();
const positionBuf = gl.createTexture();
const depthBuf = gl.createTexture();

gl.bindTexture(gl.TEXTURE_2D, diffuseBuf);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, canvas.width, canvas.height, 0, gl.RGBA, gl.FLOAT, null);

gl.bindTexture(gl.TEXTURE_2D, normalBuf);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, canvas.width, canvas.height, 0, gl.RGBA, gl.FLOAT, null);

gl.bindTexture(gl.TEXTURE_2D, positionBuf);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, canvas.width, canvas.height, 0, gl.RGBA, gl.FLOAT, null);

gl.bindTexture(gl.TEXTURE_2D, depthBuf);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
gl.texImage2D(gl.TEXTURE_2D, 0, gl.DEPTH_COMPONENT, canvas.width, canvas.height, 0, gl.DEPTH_COMPONENT, gl.UNSIGNED_SHORT, null);

gl.framebufferTexture2D(
  gl.FRAMEBUFFER, ext.COLOR_ATTACHMENT0_WEBGL, gl.TEXTURE_2D, diffuseBuf, 0);
gl.framebufferTexture2D(
  gl.FRAMEBUFFER, ext.COLOR_ATTACHMENT1_WEBGL, gl.TEXTURE_2D, normalBuf, 0);
gl.framebufferTexture2D(
  gl.FRAMEBUFFER, ext.COLOR_ATTACHMENT2_WEBGL, gl.TEXTURE_2D, positionBuf, 0);
gl.framebufferTexture2D(
  gl.FRAMEBUFFER, gl.DEPTH_ATTACHMENT, gl.TEXTURE_2D, depthBuf, 0);

const position = gl.createBuffer();
gl.bindBuffer(gl.ARRAY_BUFFER, position);
gl.bufferData(
  gl.ARRAY_BUFFER,
  new Float32Array(teapot.vertexPositions),
  gl.STATIC_DRAW);

const normal = gl.createBuffer();
gl.bindBuffer(gl.ARRAY_BUFFER, normal);
gl.bufferData(
  gl.ARRAY_BUFFER,
  new Float32Array(teapot.vertexNormals),
  gl.STATIC_DRAW);

const index = gl.createBuffer();
gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, index);
gl.bufferData(
  gl.ELEMENT_ARRAY_BUFFER,
  new Uint16Array(teapot.indices),
  gl.STATIC_DRAW);

const fieldOfView = 45 * Math.PI / 180;
const aspect = gl.canvas.clientWidth / gl.canvas.clientHeight;
const zNear = 0.1;
const zFar = 100.0;

const projectionMatrix = mat4.create();
mat4.perspective(projectionMatrix, fieldOfView, aspect, zNear, zFar);

const cameraMatrix = mat4.create();
mat4.translate(cameraMatrix, cameraMatrix, [0.0, 0.0, -40.0]);

const teapotTransform = mat4.create();

let frame = 0;
const startTime = (new Date()).getTime();
function draw() {
  gl.bindFramebuffer(gl.FRAMEBUFFER, fb);
  gl.clearColor(0.0, 0.0, 0.0, 1.0);
  gl.clearDepth(1.0);
  gl.enable(gl.DEPTH_TEST);
  gl.depthFunc(gl.LEQUAL);
  gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
  gl.useProgram(geometryPass);

  gl.uniformMatrix4fv(
    info.projection,
    false,
    projectionMatrix
  );

  gl.uniformMatrix4fv(
    info.camera,
    false,
    cameraMatrix
  );

  mat4.rotate(teapotTransform, teapotTransform, 0.01, [0.0, 1, 0.0]);
  gl.uniformMatrix4fv(
    info.teapotTransform,
    false,
    teapotTransform
  );

  gl.bindBuffer(gl.ARRAY_BUFFER, position);
  gl.vertexAttribPointer(
    info.vertexPosition,
    3,
    gl.FLOAT,
    false,
    0,
    0);
  gl.enableVertexAttribArray(info.vertexPosition);

  gl.bindBuffer(gl.ARRAY_BUFFER, normal);
  gl.vertexAttribPointer(
    info.vertexNormal,
    3,
    gl.FLOAT,
    false,
    0,
    0);
  gl.enableVertexAttribArray(info.vertexNormal);

  gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, index);
  ext.drawBuffersWEBGL([
    ext.COLOR_ATTACHMENT0_WEBGL, // gl_FragData[0]
    ext.COLOR_ATTACHMENT1_WEBGL, // gl_FragData[1]
    ext.COLOR_ATTACHMENT2_WEBGL, // gl_FragData[2]
  ]);
  
  gl.drawElements(gl.TRIANGLES, teapot.indices.length, gl.UNSIGNED_SHORT, 0);

  gl.disableVertexAttribArray(info.vertexPosition);
  gl.disableVertexAttribArray(info.vertexNormal);

  // Final pass

  gl.bindFramebuffer(gl.FRAMEBUFFER, null);
  gl.clearColor(0.0, 0.0, 0.0, 1.0);
  gl.clearDepth(1.0);
  gl.enable(gl.DEPTH_TEST);
  gl.depthFunc(gl.LEQUAL);
  gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

  gl.useProgram(finalPass);

  gl.bindBuffer(gl.ARRAY_BUFFER, positionFinal);
  gl.vertexAttribPointer(
    info.vertexPosition,
    2,
    gl.FLOAT,
    false,
    0,
    0);
  gl.enableVertexAttribArray(infoFinal.vertexPosition);

  gl.activeTexture(gl.TEXTURE0);
  gl.bindTexture(gl.TEXTURE_2D, diffuseBuf);
  gl.uniform1i(infoFinal.diffuseBuf, 0);

  gl.activeTexture(gl.TEXTURE1);
  gl.bindTexture(gl.TEXTURE_2D, normalBuf);
  gl.uniform1i(infoFinal.normalBuf, 1);

  gl.activeTexture(gl.TEXTURE2);
  gl.bindTexture(gl.TEXTURE_2D, positionBuf);
  gl.uniform1i(infoFinal.positionBuf, 2);

  gl.uniform2f(infoFinal.screenSize, canvas.width, canvas.height);
  
  gl.drawArrays(gl.TRIANGLE_STRIP, 0, 4);

  requestAnimationFrame(draw);
}

draw();
