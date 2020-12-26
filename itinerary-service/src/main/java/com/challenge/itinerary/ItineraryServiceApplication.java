package com.challenge.itinerary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ItineraryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItineraryServiceApplication.class, args);
	}

}
