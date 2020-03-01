# hyperbeam
A (toy) ray tracer in Java

## Build

### JAR

`mvn clean package`

### Docker

`docker build`

## Run

### Java

`java -jar ./hyperbeam-single-machine/hyperbeam-single-machine.jar`

### Kubernetes

`kubectl apply -f ./hyperbeam-single-machine/deployment.yaml`

## Goals
- A better CLI
- Default scene files as CLI options
- Instrumentation
  - Better metrics
  - Comprehensive logging
- Refactoring
  - More idiomatic Java
  - Better variable names
  - `Shape` is a catch-all in `Scene` - that should be a better abstraction
- External scene files (OBJ? PLY? STL? Something else?)
  - Will need to add generic triangle meshes
  - ANTLR parsing
- Moving images
  - Dynamic camera
- Distributed cluster (Akka? gRPC? HTTP? SQS?)

## Resources

- [Ray Tracing in a Weekend](http://www.realtimerendering.com/raytracing/Ray%20Tracing%20in%20a%20Weekend.pdf)
- [Ray Tracing: The Next Week](http://www.realtimerendering.com/raytracing/Ray%20Tracing_%20The%20Next%20Week.pdf)
