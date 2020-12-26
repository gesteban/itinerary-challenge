# itinerary-challenge

Project that aims to find the best path between two cities using an 
  architecture based on micro-services.

## Services included

### config-service

Configuration service. Provides the configuration needed by other services.

Configurations files are stored in `/config-data` folder.

### registry-service

Eureka Server.

### gateway-service

API routing service. Provides a unique entry point to our micro-service architecture.

### itinerary-service

Itinerary service. Provides an API to manage itineraries, allowing CRUD operations.

### search-service

Search service that consumes `itinerary-service`. This service performs searches among 
  itineraries to find the best path between two given cities. It provides two different calls:
- `findByLessTime` provides all the paths between two cities, ordered by time expended 
  (independent of the departure time).
- `findByLessSteps` provides all the paths between two cities, ordered by number of itineraries
  done.

## Frameworks and libraries used

### Spring Boot Actuator

Actuator is mainly used to expose operational information about 
  the running application — health, metrics, info, dump, env, etc. It uses HTTP endpoints 
  or JMX beans to enable us to interact with it.

This component is a dependence of Hystrix and is necessary to deploy the monitoring
  tool `admin-service`.

### Eureka Server

Also known as Discovery Server, Eureka is a lookup server. All the microservices in the
  cluster register themselves to this server.

It is used to provide circuit breaker pattern (through *Hystrix*) and client side load 
  balancing pattern (through *Ribbon*). Eureka also provides  information about registered 
  instances at <http://localhost:8099/>.

### Hystrix

Enhances resilience of distributed systems providing circuit breaker pattern, monitoring calls 
  between micro-services to prevent cascading failures.

Hystrix is used as security component as it automatically opens circuit when reached a threshold. 
  Status can be checked through
  [Hystrix Monitor](http://localhost:8092/hystrix/monitor?stream=http%3A%2F%2Flocalhost%3A8092%2Factuator%2Fhystrix.stream%20).





