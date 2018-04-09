const canvas = document.getElementById('stage');
const gl = canvas.getContext('webgl');
const ext = gl.getExtension('WEBGL_draw_buffers');
const extFloat = gl.getExtension('OES_texture_float');
const extDepth = gl.getExtension('WEBGL_depth_texture');
const extDeriv = gl.getExtension('OES_standard_derivatives');

//////////////////////////////////////////////////
// Pass 1: Geometry
//
// Renders color, normal, and position to buffers
//////////////////////////////////////////////////

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

const info = {
  camera: gl.getUniformLocation(geometryPass, 'camera'),
  projection: gl.getUniformLocation(geometryPass, 'projection'),
  teapotTransform: gl.getUniformLocation(geometryPass, 'teapotTransform'),
  vertexPosition: gl.getAttribLocation(geometryPass, 'vertexPosition'),
  vertexNormal: gl.getAttribLocation(geometryPass, 'vertexNormal'),
  vertexColor: gl.getAttribLocation(geometryPass, 'vertexColor'),
};

// Set up the buffers
const geometryFB = gl.createFramebuffer();
gl.bindFramebuffer(gl.FRAMEBUFFER, geometryFB);

const diffuseBuf = gl.createTexture();
const normalBuf = gl.createTexture();
const positionBuf = gl.createTexture();
const depthBuf = gl.createTexture();

// Each buffer needs a texture to render to. We're storing arbitrary floats in each,
// but since it's a texture, we still have to tell it it's storing RGBA values. We'll
// just read them as points/vectors instead of colors.
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

// The depth one is different because we won't read from it ourselves, we just need it
// to be able to tell which fragment is closest to the camera and therefore visible.
gl.bindTexture(gl.TEXTURE_2D, depthBuf);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
gl.texImage2D(gl.TEXTURE_2D, 0, gl.DEPTH_COMPONENT, canvas.width, canvas.height, 0, gl.DEPTH_COMPONENT, gl.UNSIGNED_SHORT, null);

// In order to be able to render to multiple buffers, we need to bind each texture
// to a color attachment. Unfortunately there are just constants for each. It's not pretty.
gl.framebufferTexture2D(
  gl.FRAMEBUFFER, ext.COLOR_ATTACHMENT0_WEBGL, gl.TEXTURE_2D, diffuseBuf, 0);
gl.framebufferTexture2D(
  gl.FRAMEBUFFER, ext.COLOR_ATTACHMENT1_WEBGL, gl.TEXTURE_2D, normalBuf, 0);
gl.framebufferTexture2D(
  gl.FRAMEBUFFER, ext.COLOR_ATTACHMENT2_WEBGL, gl.TEXTURE_2D, positionBuf, 0);
gl.framebufferTexture2D(
  gl.FRAMEBUFFER, gl.DEPTH_ATTACHMENT, gl.TEXTURE_2D, depthBuf, 0);

// Set up the geometry to render

// The teapot model didn't come with colors for each vertex so let's add some
teapot.vertexColor = teapot.vertexPositions.map((_, i) => {
  // There are 3 components to position and 3 components to color, so this maps xyz to rgb
  switch (i % 3) {
    case 0: return 92/256;
    case 1: return 130/256;
    case 2: return 153/256;
  }
});

// A rectangle below the teapot
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

// Put the vertex information into buffers
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

const zNear = 1;
const zFar = 1000;
const projectionMatrix = mat4.create();
mat4.perspective(projectionMatrix, fieldOfView, aspect, zNear, zFar);

const cameraMatrix = mat4.create();
mat4.translate(cameraMatrix, cameraMatrix, [0.0, 0.0, -40.0]);

// We will update this each frame to rotate the teapot
const teapotTransform = mat4.create();



//////////////////////////////////////////////////
// Pass 2: Ambient Occlusion
//
// Calculates shadows based on buffered info
//////////////////////////////////////////////////

const vertexShaderSourceAO = `
precision highp float;
attribute vec2 vertexPosition;

void main() {
  gl_Position = vec4(vertexPosition, 0.0, 1.0);
}
`;

const fragmentShaderSourceAO = `
#extension GL_OES_standard_derivatives : enable
precision highp float;

uniform sampler2D diffuseBuf;
uniform sampler2D normalBuf;
uniform sampler2D positionBuf;
uniform sampler2D depthBuf;

uniform vec2 screenSize;

const int NUM_SAMPLES = 11;
const int NUM_SPIRAL_TURNS = 7;
const float EPSILON = 0.1;
const float BIAS = 0.5;
const float WORLD_SPACE_RADIUS = 20.0; // radius of influence in world space
const float INTENSITY = 200.0;

const float M_PI = 3.1415926535897932384626433832795;

int AND(int n1, int n2){
  float v1 = float(n1);
  float v2 = float(n2);

  int byteVal = 1;
  int result = 0;

  for(int i = 0; i < 32; i++){
    bool keepGoing = v1>0.0 || v2 > 0.0;
    if(keepGoing){

      bool addOn = mod(v1, 2.0) > 0.0 && mod(v2, 2.0) > 0.0;

      if(addOn){
        result += byteVal;
      }

      v1 = floor(v1 / 2.0);
      v2 = floor(v2 / 2.0);
      byteVal *= 2;
    } else {
      return result;
    }
  }
  return result;
}

float random(vec3 scale, float seed) {
  return fract(sin(dot(gl_FragCoord.xyz + seed, scale)) * 43758.5453 + seed);
}

vec3 worldFromScreen(const vec2 screen) {
  return texture2D(positionBuf, screen).xyz;
}

vec3 getOffsetPositionVS(vec2 screenOrigin, vec2 unitOffset, float screenSpaceRadius) {
  // Offset by screenSpaceRadius pixels in the direction of unitOffset
  vec2 screenOffset = screenOrigin +
    screenSpaceRadius * unitOffset * vec2(1.0 / screenSize.x, 1.0 / screenSize.y);

  // Get the world coordinate from the offset screen space coordinate
  return worldFromScreen(screenOffset);
}

void main() {
  vec2 screenSpaceOrigin = gl_FragCoord.xy * vec2(1.0/screenSize.x, 1.0/screenSize.y);
  ivec2 pixel = ivec2(gl_FragCoord.xy);

  vec3 worldSpaceOrigin = worldFromScreen(screenSpaceOrigin);
  vec3 normalAtOrigin = normalize(texture2D(normalBuf, screenSpaceOrigin).xyz);
  vec3 colorAtOrigin = texture2D(diffuseBuf, screenSpaceOrigin).xyz;

  vec3 randomScale = vec3(12.9898, 78.233, 151.7182);
  vec3 sampleNoise = vec3(
    random(randomScale, 0.0),
    random(randomScale, 1.0),
    random(randomScale, 2.0));

  float initialAngle = 2.0 * M_PI * sampleNoise.x;

  // radius of influence in screen space
  float screenSpaceSampleRadius  = 100.0 * WORLD_SPACE_RADIUS / worldSpaceOrigin.y;

  float occlusion = 0.0;
  for (int sampleNumber = 0; sampleNumber < NUM_SAMPLES; sampleNumber++) {
    // Step 1:
    // Looking at the 2D image of the scene, sample the points surrounding the current one
    // in a spiral pattern

    float sampleProgress = (float(sampleNumber) + 0.5) * (1.0 / float(NUM_SAMPLES));
    float angle = sampleProgress * (float(NUM_SPIRAL_TURNS) * 2.0 * M_PI) + initialAngle;
    
    float sampleDistance = sampleProgress * screenSpaceSampleRadius;
    vec2 angleUnitVector = vec2(cos(angle), sin(angle));

    // Step 2:
    // Get the 3d coordinate corresponding to the sample on the spiral
    vec3 worldSpaceSample =
      getOffsetPositionVS(screenSpaceOrigin, angleUnitVector, sampleDistance);

    // Step 3:
    // Approximate occlusion from this sample
    vec3 originToSample = worldSpaceSample - worldSpaceOrigin;
    float squaredDistanceToSample = dot(originToSample, originToSample);

    // vn is proportional to how close the sample point is to the origin point along
    // the normal at the origin
    float vn = dot(originToSample, normalAtOrigin) - BIAS;

    // f is proportional to how close the sample point is to the origin point in the
    // sphere of influence in world space
    float radiusSquared = WORLD_SPACE_RADIUS * WORLD_SPACE_RADIUS;
    float f = max(radiusSquared - squaredDistanceToSample, 0.0) / radiusSquared;
    float sampleOcclusion =  f * f * f * max(vn / (EPSILON + squaredDistanceToSample), 0.0);

    // Accumulate occlusion
    occlusion += sampleOcclusion;
  }

  occlusion = 1.0 - occlusion / (4.0 * float(NUM_SAMPLES));

  occlusion = clamp(pow(occlusion, 1.0 + INTENSITY), 0.0, 1.0);
  if (abs(dFdx(worldSpaceOrigin.z)) < 0.5) {
    occlusion -= dFdx(occlusion) * (float(AND(pixel.x, 1)) - 0.5);
  }
  if (abs(dFdy(worldSpaceOrigin.z)) < 0.5) {
    occlusion -= dFdy(occlusion) * (float(AND(pixel.y, 1)) - 0.5);
  }

  gl_FragData[0] = vec4(occlusion, occlusion, occlusion, 1.0);
}
`;

const aoFB = gl.createFramebuffer();
gl.bindFramebuffer(gl.FRAMEBUFFER, aoFB);

const aoBuf = gl.createTexture();
gl.bindTexture(gl.TEXTURE_2D, aoBuf);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, canvas.width, canvas.height, 0, gl.RGBA, gl.FLOAT, null);
gl.framebufferTexture2D(
  gl.FRAMEBUFFER, ext.COLOR_ATTACHMENT0_WEBGL, gl.TEXTURE_2D, aoBuf, 0);

const fragmentShaderAO = gl.createShader(gl.FRAGMENT_SHADER);
gl.shaderSource(fragmentShaderAO, fragmentShaderSourceAO);
gl.compileShader(fragmentShaderAO);

const vertexShaderAO = gl.createShader(gl.VERTEX_SHADER);
gl.shaderSource(vertexShaderAO, vertexShaderSourceAO);
gl.compileShader(vertexShaderAO);

const aoPass = gl.createProgram();
gl.attachShader(aoPass, vertexShaderAO);
gl.attachShader(aoPass, fragmentShaderAO);
gl.linkProgram(aoPass);

const infoAO = {
  diffuseBuf: gl.getUniformLocation(aoPass, 'diffuseBuf'),
  normalBuf: gl.getUniformLocation(aoPass, 'normalBuf'),
  positionBuf: gl.getUniformLocation(aoPass, 'positionBuf'),
  depthBuf: gl.getUniformLocation(aoPass, 'depthBuf'),
  screenSize: gl.getUniformLocation(aoPass, 'screenSize'),
  vertexPosition: gl.getAttribLocation(aoPass, 'vertexPosition'),
};

// Create a rectangle so that we basically just call the fragment shader on
// each pixel of the screen without any extra geometry
const positionAO = gl.createBuffer();
gl.bindBuffer(gl.ARRAY_BUFFER, positionAO);
gl.bufferData(
  gl.ARRAY_BUFFER,
  new Float32Array([
    -1.0, -1.0,
    1.0, -1.0,
    -1.0, 1.0,
    1.0, 1.0]),
  gl.STATIC_DRAW);



//////////////////////////////////////////////////
// Pass 3: Final shading
//
// Combines AO and Phong shading
//////////////////////////////////////////////////

const vertexShaderSourceFinal = vertexShaderSourceAO;

const fragmentShaderSourceFinal = `
#extension GL_OES_standard_derivatives : enable
precision highp float;

uniform sampler2D diffuseBuf;
uniform sampler2D normalBuf;
uniform sampler2D positionBuf;
uniform sampler2D aoBuf;
uniform sampler2D depthBuf;

uniform vec2 screenSize;

const float EDGE_SHARPNESS = 1.0;
const int SCALE = 2;

float blurAO(vec2 screenSpaceOrigin) {
  float sum = texture2D(aoBuf, screenSpaceOrigin).x;
  float originDepth = texture2D(depthBuf, screenSpaceOrigin).z;
  float totalWeight = 1.0;
  sum *= totalWeight;

  for (int x = -4; x <= 4; x++) {
    for (int y = -4; y <= 4; y++) {
      if (x != 0 || y != 0) {
        vec2 samplePosition = screenSpaceOrigin + vec2(float(x * SCALE), float(y * SCALE)) * vec2(1.0/screenSize.x, 1.0/screenSize.y);
        float ao = texture2D(aoBuf, samplePosition).x;
        float sampleDepth = texture2D(depthBuf, samplePosition).z;
        int kx = 4 - (x < 0 ? -x : x);
        int ky = 4 - (y < 0 ? -y : y);
        float weight = 0.3 + (abs(float(x * y)) / (25.0 * 25.0));
        weight *= max(0.0, 1.0 - (EDGE_SHARPNESS * 2000.0) * abs(sampleDepth - originDepth));

        sum += ao * weight;
        totalWeight += weight;
      }
    }
  }

  const float epsilon = 0.0001;
  return sum / (totalWeight + epsilon);
}

void main() {
  vec2 screenSpaceOrigin = gl_FragCoord.xy * vec2(1.0/screenSize.x, 1.0/screenSize.y);

  vec3 worldSpaceOrigin = texture2D(positionBuf, screenSpaceOrigin).xyz;
  vec3 normalAtOrigin = normalize(texture2D(normalBuf, screenSpaceOrigin).xyz);
  vec3 colorAtOrigin = texture2D(diffuseBuf, screenSpaceOrigin).xyz;
  vec3 aoAtOrigin = texture2D(aoBuf, screenSpaceOrigin).xyz;

  // Blur AO
  float occlusion = blurAO(screenSpaceOrigin);

  // Add specular highlights
  vec3 lightDir = normalize(vec3(150.0, 80.0, 50.0) - worldSpaceOrigin);
  vec3 viewDir = normalize(vec3(0.0, 0.0, 0.0) - worldSpaceOrigin);
  float spec = pow(max(dot(viewDir, reflect(-lightDir, normalAtOrigin)), 0.0), 50.0);
  vec3 specular = 0.3 * spec * vec3(1.0, 1.0, 1.0);

  gl_FragColor = vec4((colorAtOrigin + specular) * occlusion, 1.0);
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
  aoBuf: gl.getUniformLocation(finalPass, 'aoBuf'),
  depthBuf: gl.getUniformLocation(finalPass, 'depthBuf'),
  screenSize: gl.getUniformLocation(finalPass, 'screenSize'),
  vertexPosition: gl.getAttribLocation(finalPass, 'vertexPosition'),
};

const positionFinal = positionAO;

function draw() {
  // Render geometry pass
  gl.bindFramebuffer(gl.FRAMEBUFFER, geometryFB);
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
  gl.disableVertexAttribArray(info.vertexColor);

  // Render AO pass

  gl.bindFramebuffer(gl.FRAMEBUFFER, aoFB);
  gl.clearColor(0.0, 0.0, 0.0, 1.0);
  gl.clearDepth(1.0);
  gl.enable(gl.DEPTH_TEST);
  gl.depthFunc(gl.LEQUAL);
  gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
  gl.useProgram(aoPass);

  gl.bindBuffer(gl.ARRAY_BUFFER, positionAO);
  gl.vertexAttribPointer(
    info.vertexPosition,
    2,
    gl.FLOAT,
    false,
    0,
    0);
  gl.enableVertexAttribArray(infoAO.vertexPosition);

  gl.activeTexture(gl.TEXTURE0);
  gl.bindTexture(gl.TEXTURE_2D, diffuseBuf);
  gl.uniform1i(infoAO.diffuseBuf, 0);

  gl.activeTexture(gl.TEXTURE1);
  gl.bindTexture(gl.TEXTURE_2D, normalBuf);
  gl.uniform1i(infoAO.normalBuf, 1);

  gl.activeTexture(gl.TEXTURE2);
  gl.bindTexture(gl.TEXTURE_2D, positionBuf);
  gl.uniform1i(infoAO.positionBuf, 2);

  gl.activeTexture(gl.TEXTURE3);
  gl.bindTexture(gl.TEXTURE_2D, depthBuf);
  gl.uniform1i(infoAO.depthBuf, 3);

  gl.uniform2f(infoAO.screenSize, canvas.width, canvas.height);

  gl.drawArrays(gl.TRIANGLE_STRIP, 0, 4);

  // Render final pass

  gl.bindFramebuffer(gl.FRAMEBUFFER, null); // Important: don't use the old framebuffer
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
  gl.bindTexture(gl.TEXTURE_2D, aoBuf);
  gl.uniform1i(infoFinal.aoBuf, 3);

  gl.activeTexture(gl.TEXTURE4);
  gl.bindTexture(gl.TEXTURE_2D, depthBuf);
  gl.uniform1i(infoFinal.depthBuf, 4);

  gl.uniform2f(infoFinal.screenSize, canvas.width, canvas.height);

  gl.drawArrays(gl.TRIANGLE_STRIP, 0, 4);

  requestAnimationFrame(draw);
}

draw();
