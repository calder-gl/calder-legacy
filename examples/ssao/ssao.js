const canvas = document.getElementById('stage');
const gl = canvas.getContext('webgl');
const ext = gl.getExtension('WEBGL_draw_buffers');
const extFloat = gl.getExtension('OES_texture_float');
const extDepth = gl.getExtension('WEBGL_depth_texture');

const zNear = 1;
const zFar = 1000;

const vertexShaderSource = `
precision highp float;
attribute vec3 vertexPosition;
attribute vec3 vertexNormal;
attribute vec3 vertexColor;
uniform mat4 camera;
uniform mat4 projection;
uniform mat4 teapotTransform;

varying vec4 diffuse;
varying vec4 normal;
varying vec4 position;

void main() {
  vec4 transformedPosition = teapotTransform * vec4(vertexPosition, 1.0);
  vec4 transformedNormal = teapotTransform * vec4(vertexNormal, 0.0);

  diffuse = vec4(vertexColor.xyz, 1.0);
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
precision highp float;

uniform sampler2D diffuseBuf;
uniform sampler2D normalBuf;
uniform sampler2D positionBuf;
uniform sampler2D depthBuf;

uniform vec2 screenSize;

const int NUM_SAMPLES = 30;
const int NUM_SPIRAL_TURNS = 7;
const float bias = 1.0;

#define M_PI 3.1415926535897932384626433832795

float random(vec3 scale, float seed) {
  return fract(sin(dot(gl_FragCoord.xyz + seed, scale)) * 43758.5453 + seed);
}

vec2 tapLocation(int sampleNumber, float spinAngle, out float radiusSS) {
  // radius relative to radiusSS
  float alpha = (float(sampleNumber) + 0.5) * (1.0 / float(NUM_SAMPLES));
  float angle = alpha * (float(NUM_SPIRAL_TURNS) * 6.28) + spinAngle;
  
  radiusSS = alpha;
  return vec2(cos(angle), sin(angle));
}

vec3 getPositionVS(const vec2 uv) {
  return texture2D(positionBuf, uv).xyz;
}

vec3 getOffsetPositionVS(vec2 uv, vec2 unitOffset, float radiusSS) {
  vec2 offsetUV = uv + radiusSS * unitOffset * vec2(1.0/screenSize.x, 1.0/screenSize.y);
  return getPositionVS(offsetUV);
}

float sampleAO(vec2 uv, vec3 positionVS, vec3 normalVS, float sampleRadiusSS, float sampleRadiusWS, int tapIndex, float rotationAngle) {
  const float epsilon = 0.1;
  float radiusSquared = sampleRadiusWS * sampleRadiusWS;

  float radiusSS;
  vec2 unitOffset = tapLocation(tapIndex, rotationAngle, radiusSS);
  radiusSS *= sampleRadiusSS;

  vec3 Q = getOffsetPositionVS(uv, unitOffset, radiusSS);
  vec3 v = Q - positionVS;

  float vv = dot(v, v);
  float vn = dot(v, normalVS) - bias;

  float f = max(radiusSquared - vv, 0.0) / radiusSquared;
  return f * f * f * max(vn / (epsilon + vv), 0.0);
}


void main() {
  vec2 texCoords = gl_FragCoord.xy * vec2(1.0/screenSize.x, 1.0/screenSize.y);
  vec2 vUV = texCoords;

  vec4 diffuseBufInfo = texture2D(diffuseBuf, texCoords);
  vec4 normalBufInfo = texture2D(normalBuf, texCoords);
  vec4 positionBufInfo = texture2D(positionBuf, texCoords);

  vec3 originVS = getPositionVS(vUV);
  vec3 normalVS = normalBufInfo.xyz;

  vec3 sampleNoise = vec3(
    random(vec3(12.9898, 78.233, 151.7182), 0.0),
    random(vec3(63.7264, 10.873, 623.6736), 100.0),
    random(vec3(63.7264, 10.873, 623.6736), 1000.0));

  float randomPatternRotationAngle = 2.0 * M_PI * sampleNoise.x;

  float radiusWS  = 20.0; // radius of influence in world space
  float radiusSS  = 100.0 * radiusWS / originVS.y; // radius of influence in screen space
  float occlusion = 0.0;

  for (int i = 0; i < NUM_SAMPLES; i++) {
    occlusion += sampleAO(texCoords, originVS, normalVS, radiusSS, radiusWS, i, randomPatternRotationAngle);
  }

  occlusion = 1.0 - occlusion / (4.0 * float(NUM_SAMPLES));
  float intensity = 200.0;
  occlusion = clamp(pow(occlusion, 1.0 + intensity), 0.0, 1.0);
  
  vec3 lightDir = normalize(vec3(150.0, 80.0, 50.0) - positionBufInfo.xyz);
  vec3 viewDir = normalize(vec3(0.0, 0.0, 0.0) - positionBufInfo.xyz);
  vec3 normal = normalize(normalBufInfo.xyz);
  float spec = pow(max(dot(viewDir, reflect(-lightDir, normal)), 0.0), 50.0);
  vec3 specular = 0.3 * spec * vec3(1.0, 1.0, 1.0);

  gl_FragColor = vec4((diffuseBufInfo.xyz + specular) * occlusion, 1.0);
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
  depthBuf: gl.getUniformLocation(finalPass, 'depthBuf'),
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
  vertexColor: gl.getAttribLocation(geometryPass, 'vertexColor'),
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

teapot.vertexColor = teapot.vertexPositions.map((_, i) => {
  switch (i % 3) {
    case 0: return 92/256;
    case 1: return 130/256;
    case 2: return 153/256;
  }
});

const ground = {
  vertexPositions: [
    -30, -10, 40,
    -30, -10, -40,
    30, -10, -40,
    30, -10, 40,
  ],
  vertexNormals: [
    0, 1, 0,
    0, 1, 0,
    0, 1, 0,
    0, 1, 0,
  ],
  vertexColor: [
    0.7, 0.75, 0.75,
    0.7, 0.75, 0.75,
    0.7, 0.75, 0.75,
    0.7, 0.75, 0.75,
  ],
  indices: [
    0, 1, 2,
    0, 2, 3,
  ],
};

const position = gl.createBuffer();
gl.bindBuffer(gl.ARRAY_BUFFER, position);
gl.bufferData(
  gl.ARRAY_BUFFER,
  new Float32Array([...teapot.vertexPositions, ...ground.vertexPositions]),
  gl.STATIC_DRAW);

const normal = gl.createBuffer();
gl.bindBuffer(gl.ARRAY_BUFFER, normal);
gl.bufferData(
  gl.ARRAY_BUFFER,
  new Float32Array([...teapot.vertexNormals, ...ground.vertexNormals]),
  gl.STATIC_DRAW);

const color = gl.createBuffer();
gl.bindBuffer(gl.ARRAY_BUFFER, color);
gl.bufferData(
  gl.ARRAY_BUFFER,
  new Float32Array([...teapot.vertexColor, ...ground.vertexColor]),
  gl.STATIC_DRAW);

const index = gl.createBuffer();
gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, index);
gl.bufferData(
  gl.ELEMENT_ARRAY_BUFFER,
  new Uint16Array([...teapot.indices, ...ground.indices.map(i => i + teapot.vertexPositions.length/3)]),
  gl.STATIC_DRAW);

const fieldOfView = 45 * Math.PI / 180;
const aspect = gl.canvas.clientWidth / gl.canvas.clientHeight;

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

  gl.bindBuffer(gl.ARRAY_BUFFER, color);
  gl.vertexAttribPointer(
    info.vertexColor,
    3,
    gl.FLOAT,
    false,
    0,
    0);
  gl.enableVertexAttribArray(info.vertexColor);

  gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, index);
  ext.drawBuffersWEBGL([
    ext.COLOR_ATTACHMENT0_WEBGL, // gl_FragData[0]
    ext.COLOR_ATTACHMENT1_WEBGL, // gl_FragData[1]
    ext.COLOR_ATTACHMENT2_WEBGL, // gl_FragData[2]
  ]);
  
  gl.drawElements(gl.TRIANGLES, teapot.indices.length + ground.indices.length, gl.UNSIGNED_SHORT, 0);

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

  gl.activeTexture(gl.TEXTURE3);
  gl.bindTexture(gl.TEXTURE_2D, depthBuf);
  gl.uniform1i(infoFinal.depthBuf, 3);

  gl.uniform2f(infoFinal.screenSize, canvas.width, canvas.height);
  
  gl.drawArrays(gl.TRIANGLE_STRIP, 0, 4);

  requestAnimationFrame(draw);
}

draw();
