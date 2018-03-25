import * as cgl from 'calder-gl';

const canvas = document.getElementById('stage');
const gl = canvas.getContext('webgl');

const geometryPass = cgl.pipeline(
  cgl.vertex `
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
  `,
  cgl.fragment `
    precision mediump float;
    // If rendering to buffers and not to the screen, the size for all buffers must
    // be the same for all buffers
    buffersize {{width: int}}, {{height: int}};

    varying vec4 diffuse;
    varying vec4 normal;
    varying vec4 position;

    // Buffers have a format and properties that can be initialized in C struct
    // format, or just left as sane defaults. (In WebGL 2, the format and internal
    // format of a texture can be different, but in WebGL 1 they must be the same)
    buffer rgba diffuseBuf {
      .magFilter = NEAREST,
      .minFilter = NEAREST,
      .wrapS = CLAMP_TO_EDGE,
      .wrapT = CLAMP_TO_EDGE
    };
    buffer rgba normalBuf;
    buffer rgba positionBuf;

    // One depth buffer can be declared, and is implicitly used when it is.
    // This needs declaring because it is plausible that no depth buffer is needed
    // for some applications, and declaring it means it is given a name so that
    // other shaders can read its value in as a texture
    buffer depth depthBuf;

    void main() {
      diffuseBuf = vec4(diffuse.xyz, 1.0);
      normalBuf = vec4(normalize(normal.xyz), 1.0);
      positionBuf = vec4(position.xyz, 1.0);
    }
  `
).fill({
  // Use `dynamic` to tell the type system to just assume the value will be an int
  // at runtime so that the template can be filled now
  width: cgl.dynamicInt(canvas.width),
  height: cgl.dynamicInt(canvas.height),
}).build(gl);

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

// These values don't change, so we can buffer them once at the beginning
geometryPass.projection = projectionMatrix;
geometryPass.camera = cameraMatrix;


//////////////////////////////////////////////////
// Pass 2: Ambient Occlusion
//
// Calculates shadows based on buffered info
//////////////////////////////////////////////////

const aoPass = cgl.pixelPipeline(
  cgl.fragment `
    precision highp float;

    uniform sampler2D diffuseBuf;
    uniform sampler2D normalBuf;
    uniform sampler2D positionBuf;
    uniform sampler2D depthBuf;

    uniform vec2 screenSize;

    const int NUM_SAMPLES = 30;
    const int NUM_SPIRAL_TURNS = 7;
    const float EPSILON = 0.1;
    const float BIAS = 0.5;
    const float WORLD_SPACE_RADIUS = 20.0; // radius of influence in world space
    const float INTENSITY = 200.0;

    const float M_PI = 3.1415926535897932384626433832795;

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
      
      // Add specular highlights
      vec3 lightDir = normalize(vec3(150.0, 80.0, 50.0) - worldSpaceOrigin);
      vec3 viewDir = normalize(vec3(0.0, 0.0, 0.0) - worldSpaceOrigin);
      float spec = pow(max(dot(viewDir, reflect(-lightDir, normalAtOrigin)), 0.0), 50.0);
      vec3 specular = 0.3 * spec * vec3(1.0, 1.0, 1.0);

      gl_FragColor = vec4((colorAtOrigin + specular) * occlusion, 1.0);
    }
  `
).build(gl);

finalPass.screenSize = [canvas.width, canvas.height];

function draw() {
  // Render geometry pass
  geometryPass.clear();
  geometryPass.useProgram();

  mat4.rotate(teapotTransform, teapotTransform, 0.01, [0.0, 1, 0.0]);
  geometryPass.teapotTransform = teapotTransform;

  // Draw teapot
  geometryPass.vertexPosition = teapot.vertexPositions;
  geometryPass.vertexNormal = teapot.vertexNormals;
  geometryPass.vertexColor = teapot.vertexColor;
  // If you use drawElements() instead of draw(), it will use an element array
  // buffer to index all the other attribute buffers
  geometryPass.drawElements(teapot.indices);

  // Draw ground
  geometryPass.vertexPosition = ground.vertexPositions;
  geometryPass.vertexNormal = ground.vertexNormals;
  geometryPass.vertexColor = ground.vertexColor;
  geometryPass.drawElements(ground.indices);

  // Since we're going to use multiple pipelines, we have to unset buffers.
  geometryPass.cleanup();


  // Render final pass

  finalPass.clear();
  finalPass.useProgram();

  // Buffers from a multi-buffer pipeline are externally readable textures
  finalPass.diffuseBuf = geometryPass.diffuseBuf;
  finalPass.normalBuf = geometryPass.normalBuf;
  finalPass.positionBuf = geometryPass.positionBuf;
  finalPass.depthBuf = geometryPass.depthBuf;

  finalPass.draw();
  finalPass.cleanup();

  requestAnimationFrame(draw);
}

draw();
