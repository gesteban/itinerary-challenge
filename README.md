# itinerary-challenge

Project that aims to find the best path between two cities using an 
architecture based on micro-services.

## config-service

Configuration service. Provides the configuration needed by other services.

Configurations files are stored in `/config-data` folder.

## eureka-service

Discovery service. Provides circuit breaker pattern (through *Hystrix*) and client 
side load balancing pattern (through *Ribbon*). Provides monitoring of internal requests
at <http://localhost:8099/>.

## gateway-service

API routing service. Provides a unique entry point to our micro-service architecture.

## search-service

Service that perform 

Include

- *Hystrix*. Enhances resilience of distributed systems providing circuit 
  breaker pattern to stop cascading failure. Status can be checked through
  [Hystrix Monitor](http://localhost:8092/hystrix/monitor?stream=http%3A%2F%2Flocalhost%3A8092%2Factuator%2Fhystrix.stream%20), 
  that monitors calls between micro-services.
- *Eureka client*. Client to connect to `eureka-service`.
- 
