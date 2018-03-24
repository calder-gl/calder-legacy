import * as cgl from 'calder-gl';

const canvas = document.getElementById('stage');
const gl = canvas.getContext('webgl');

const shaderPipeline = cgl.pipeline(
  cgl.vertex `
    attribute vec2 vertexPosition;

    void main() {
      gl_Position = vec4(vertexPosition, 0.0, 1.0);
    }
  `,
  cgl.fragment `
    uniform float time;
    uniform int frame;
    uniform vec2 resolution;

    int NUM_SPHERES = {{numSpheres: int}};
    int NUM_BOUNCES = {{numBounces: int}};
    int NUM_SAMPLES = {{numSamples: int}};
    float CAMERA_CYCLE_LENGTH = {{cameraCycleLength: float}}
    float FRAMES_PER_SECOND = {{framesPerSecond: float}}
    float M_PI = {{pi: float}};

    int randCounter = 0;

    float random(vec3 scale, float seed) {
      return fract(sin(dot(gl_FragCoord.xyz + seed, scale)) * 43758.5453 + seed);
    }

    // random cosine-weighted distributed vector
    // from http://www.rorydriscoll.com/2009/01/07/better-sampling/
    vec3 cosineWeightedDirection(float seed, vec3 normal) {
      float u = random(vec3(12.9898, 78.233, 151.7182), seed);
      float v = random(vec3(63.7264, 10.873, 623.6736), seed);
      float r = sqrt(u);
      float angle = 6.283185307179586 * v;
      // compute basis from normal
      vec3 sdir, tdir;
      if (abs(normal.x)<.5) {
        sdir = cross(normal, vec3(1,0,0));
      } else {
        sdir = cross(normal, vec3(0,1,0));
      }
      tdir = cross(normal, sdir);
      return r*cos(angle)*sdir + r*sin(angle)*tdir + sqrt(1.-u)*normal;
    }

    // random normalized vector
    vec3 uniformlyRandomDirection(float seed) {
      float u = random(vec3(12.9898, 78.233, 151.7182), seed);
      float v = random(vec3(63.7264, 10.873, 623.6736), seed);
      float z = 1.0 - 2.0 * u;
      float r = sqrt(1.0 - z * z);
      float angle = 6.283185307179586 * v;
      return vec3(r * cos(angle), r * sin(angle), z);
    }

    vec3 randomHemisphere(int numSample, int i, vec3 normal) {
      vec3 randVector = uniformlyRandomDirection(float(randCounter));
      randCounter++;
      randVector.z = abs(randVector.z);

      vec3 up = vec3(0.0, 0.0, 1.0);
      float dotted = dot(up, normal);
      float crossed = length(cross(up, normal));
      mat3 rotation = mat3(
          dotted, -crossed, 0,
          crossed, dotted, 0,
          0, 0, 1);
      return rotation * randVector;
    }

    // Returns the distance to the sphere's surface if there is a ray-sphere
    // intersection, or -1 otherwise
    float intersection(vec3 rayOrigin, vec3 rayDirection, vec3 sphereLocation, float sphereRadius) {
      vec3 toCenter = rayOrigin - sphereLocation;
      float a = dot(rayDirection, rayDirection);
      float b = dot(toCenter, rayDirection) * 2.0;
      float c = dot(toCenter, toCenter) - sphereRadius * sphereRadius;
      float descriminant = b*b - 4.0*a*c;

      if (approx(descriminant, 0.0)) {
        return -b / (2.0*a);
      } else if (descriminant < 0.0) {
        return -1.0;
      } else {
        float t = (-b - sqrt(descriminant)) / (2.0*a);
        float t2 = (-b + sqrt(descriminant)) / (2.0*a);

        if (t >= 0.0 && t2 >= 0.0) {
          return min(t, t2);
        } else if (t >= 0.0) {
          return t;
        } else if (t2 >= 0.0) {
          return t2;
        } else {
          return -1.0;
        }
      }
    }

    vec3 normalAt(vec3 position, vec3 sphereLocation) {
      return normalize(position - sphereLocation);
    }

    void main() {
      vec3 sphereLocations[NUM_SPHERES];
      vec3 sphereColors[NUM_SPHERES];
      float sphereRadii[NUM_SPHERES];

      ///////////////////////////////////////////////
      // Important change: colour can be represented
      // by a hex code
      ///////////////////////////////////////////////
      sphereLocations[0] = vec3(-1.0, 10.0, 0.0);
      sphereColors[0] = #E66666;
      sphereRadii[0] = 2.0;

      sphereLocations[1] = vec3(2.0, 7.0, -1.0);
      sphereColors[1] = #6666E6;
      sphereRadii[1] = 1.0;

      sphereLocations[2] = vec3(2.0, 10.0, -102.0);
      sphereColors[2] = #E6E6E6;
      sphereRadii[2] = 100.0;

      vec3 background = #FFF;

      vec3 cameraLocationStart = vec3(0, 0, 0);
      vec3 cameraLocationEnd = vec3(1, 3, 1);
      // Slide back and forth between camera locations
      vec3 cameraLocation = mix(cameraLocationStart, cameraLocationEnd, sin(float(frame) / (CAMERA_CYCLE_LENGTH * FRAMES_PER_SECOND)));
      vec3 cameraTarget = vec3(0.0, 10.0, 0.0);
      vec3 cameraUp = vec3(0, 0, 1);
      float cameraDistance = 0.3;

      float cameraWidth = 0.5;
      float cameraHeight = cameraWidth * (float(resolution.y) / float(resolution.x));

      // Normalized pixel coordinates (from 0 to 1)
      vec2 uv = (gl_FragCoord.xy + vec2(1.0, 1.0))/(resolution.xy);

      vec3 cameraDirection = normalize(cameraTarget - cameraLocation);
      vec3 x = normalize(cross(cameraUp, cameraDirection)) * cameraWidth;
      vec3 y = normalize(cross(cameraDirection, x)) * cameraHeight;

      vec3 cameraSW = cameraLocation + cameraDistance * cameraDirection - 0.5 * x - 0.5 * y;

      // Average NUM_SAMPLES renders
      vec3 avgColor = vec3(0, 0, 0);
      for (int numSample = 0; numSample < NUM_SAMPLES; numSample++) {

        // Start a ray at the camera, and bounce it around the scene at most
        // NUM_BOUNCES times
        vec3 color = vec3(1.0, 1.0, 1.0);
        vec3 origin = cameraLocation;
        vec3 direction = cameraSW + (x * uv.x) + (y * uv.y) - cameraLocation;

        for (int bounce = 0; bounce < NUM_BOUNCES; bounce++) {

          // Find the first sphere the ray hits
          ////////////////////////////////////////////////////
          // Important change: introduces an 'index' type
          // that can only be assigned to a loop variable
          // and must have a null check, allowing an index
          // to be saved and used outside a loop safely
          ////////////////////////////////////////////////////
          index closestIndex = NONE;
          float closest = -1.0;
          for (int i = 0; i < NUM_SPHERES; i++) {
            float intersectionDistance = intersection(
                origin,
                direction,
                sphereLocations[i],
                sphereRadii[i]);
            if (intersectionDistance > 0.0 &&
                (closestIndex == NONE || intersectionDistance < closest)) {
              closest = intersectionDistance;
              closestIndex = i;
            }
          }

          // Handle no collisions
          if (closestIndex == NONE) {
            color *= background;
            break;

          } else {
            // Bounce the ray and change the color
            color *= sphereColors[closestIndex];
            origin += direction * (closest - 0.001);
            direction = randomHemisphere(
              numSample,
              bounce,
              normalAt(origin, sphereLocations[closestIndex]));
          }
        }
        avgColor += color / float(NUM_SAMPLES);
      }

      gl_FragColor = vec4(avgColor, 1.0);
    } 
  `
).fill({
  numSpheres: cgl.int `3`,
  numBounces: cgl.int `10`,
  numSamples: cgl.int `10`,
  pi: cgl.float `3.1415926535897932384626433832795`,
  cameraCycleLength: cgl.float `2`,
  framesPerSecond: cgl.float `60`,
}).build(gl);

let frame = 0;
const startTime = (new Date()).getTime();
function draw() {
  // Uses default clear settings, maybe allow passing an object
  // with overrides
  shaderPipeline.clear();
  shaderPipeline.useProgram();

  // The shaderPipeline should be able to handle flattening
  shaderPipeline.vertexPosition = [
    [-1, -1],
    [1, -1],
    [-1, 1],
    [1, 1]];
  shaderPipeline.time = (new Date()).getTime() - startTime;
  shaderPipeline.frame = frame++;
  shaderPipeline.resolution = [stage.width, stage.height];
  shaderPipeline.draw(4); // is there a better way to get # vertices?

  requestAnimationFrame(draw);
}
draw();
