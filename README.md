Project that aims to find the best path between two cities using an 
  architecture based on microservices using Spring Cloud and Docker.

## 1 Services included

### 1.1 registry-service

Eureka Server. A centralized register of available microservices. Used by several 
  components that enhance security and resilience.

Uses:
- Eureka Server

### 1.2 config-service

Configuration service. Provides the configuration needed by other services.

Configurations files are stored in `/config-data` folder.

Uses:
- Spring Cloud Config Server

### 1.3 admin-service

Spring Boot Admin Server. Provides a dashboard to query the data exposed by Spring Boot
Actuator

Uses:
- Spring Boot Admin Server

### 1.4 gateway-service

API routing service. Provides a unique entry point to our microservice architecture.

Uses:
- Spring Cloud Config Client
- Eureka Client
- Spring Security
- Spring Cloud Gateway

### 1.5 itinerary-service

Itinerary service. Provides an API to manage itineraries, allowing CRUD operations.

Uses:
- Spring Cloud Config Client
- Eureka Client
- Spring Boot Actuator
- Spring Boot Admin Client

### 1.6 search-service

Search service that consumes `itinerary-service`. This service performs searches among 
  itineraries to find the best path between two given cities. It provides two different calls:
- `findByLessTime` provides all the paths between two cities, ordered by time expended 
  (independent of the departure time).
- `findByLessSteps` provides all the paths between two cities, ordered by number of 
  itineraries done.

Uses:
- Spring Cloud Config Client
- Eureka Client
- Spring Boot Actuator
- Spring Boot Admin Client

## 2. Frameworks and libraries used

### 2.1 Spring Cloud Config

Spring Cloud Config provides server-side and client-side support for externalized 
  configuration in a distributed system. The server exposes an HTTP resource-based API 
  for external configuration that clients consume, centralizing management of external 
  properties for applications across all environments.

This project uses Spring Cloud Config to centralize and manage configurations trough 
  `config-service`.

### 2.2 Spring Boot Actuator

Actuator is mainly used to expose operational information about 
  the running application — health, metrics, info, dump, env, etc. It uses HTTP endpoints 
  or JMX beans to enable us to interact with it.

This component is a dependence of Hystrix and is necessary to deploy the monitoring
  tool `admin-service`. It exposes operational information in JSON format at:
- <http://localhost:8091/actuator> from `itinerary-service`
- <http://localhost:8092/actuator> from `search-service`

### 2.3 Spring Cloud Netflix

Project that allows Spring Boot applications to enable and configure common patterns. 

Spring Cloud Netflix is used to enable the following patterns in this application:
- Service Discovery (Eureka)
- Circuit Breaker (Hystrix)
- Client Side Load Balancing (Ribbon)

#### 2.3.1 Eureka

Also known as Discovery Server, Eureka is a lookup server. All the microservices in the
  cluster register themselves to this server.

It is used as dependency to provide circuit breaker pattern (through Hystrix) and 
  client side load balancing pattern (through Ribbon). Eureka also provides information 
  about registered instances at <http://localhost:8099/>.

#### 2.3.2 Hystrix

Enhances resilience of distributed systems providing circuit breaker pattern, monitoring
  calls between microservices to prevent cascading failures.

Hystrix is used as security component as it automatically opens circuit when reached a 
  threshold. Status can be checked through
  [Hystrix Monitor](http://localhost:8092/hystrix/monitor?stream=http%3A%2F%2Flocalhost%3A8092%2Factuator%2Fhystrix.stream%20).

### 2.4 Spring Boot Admin

Web application used for managing and monitoring Spring Boot applications. Each 
  application is considered as a client and registers to the admin server.

Used to monitor actuators endpoints in a simpler way.

### 2.5 Spring Cloud Sleuth

Spring Cloud Sleuth provides Spring Boot auto-configuration for distributed tracing.

### 2.6 Spring Security

Spring Security is a framework that focuses on providing both authentication and 
  authorization to Java applications.

It is used as a security component providing the following security aspects:

- Authorization. Usage of `SecurityWebFilterChain` and `UserDetails` of
  `org.springframework.security` to implement an authorization-based list of
  roles and permissions to access api services. Sample users provided through code.
- Authentication. Though Basic Auth was implemented as authentication method, it is
  obviously not an adequate solution to production environments (where https/OAuth2
  is more suitable).
- Apart from that, CSRF is disabled to ease testing and as per [Spring Security 
  Documentation](https://docs.spring.io/autorepo/docs/spring-security/4.0.0.RELEASE/reference/html/csrf.html#when-to-use-csrf-protection)

### 2.7 Spring Cloud Gateway



## 3 Other considerations

### Assumptions

The project takes the following assumptions in regard to itineraries and paths:

- The itineraries occur in the same time zone; data of the arrival and departure times
  does not take into account time zones.
- The itineraries stored are daily itineraries, i.e. they repeat day after day;
  services make use of simple `HH:mm` times to refer to arrival and departure times.
- When searching the best path between two cities, the proposed path will start and end
  at the same day; i.e. every path has to end before `23:59`.

### Security considerations

- Gateway. Assuming we are not managing internal threats, Spring Cloud Gateway acts as an
  entry point to all our external calls. This centralizes and eases security configuration
  within our microservices ecosystem.
  
- Hiding services. Using docker-compose configuration we can disable port visibility 
  outside of docker environment. This allows us to expose internal microservices ports 
  to keep application running without making them available to the world.
