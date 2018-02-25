# Calder Vision Statement

## Goals of Initial Release

- Replace raw GLSL code in JavaScript with a compiled, typesafe version
  - Replace string splicing with essentially a safe template engine
  - Static type-checking at compile time
  - No runtime errors
- Abstract away the glue between JavaScript and the GPU
  - Buffers for passing variables to shaders should be handled automatically
  - Provide a nicer, more declarative API for standard GL calls
- Simplified abstractions for common or complex operations:
  - Buffer binding
  - Landom number generation
  - Possibly array access
  - Possibly compute shaders
  - Possibly a better color type
  - Shader pipeline builder functions
- Open sourced. Fully permissible MIT licence

## Long-term Goals

- Use calder-gl as a teaching tool
  - Abstract away everything but concepts required for experimenting with graphics
  - Maybe have an examples directory showing graphics concepts
- Have a documentation website
  - Complete with a bunch of code examples
  - Include a live-code editor where users may write calder code, compile it to GLSL, and run it in a previewer div
