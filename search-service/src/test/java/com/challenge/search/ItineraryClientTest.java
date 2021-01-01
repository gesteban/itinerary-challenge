package com.challenge.search;

import com.challenge.search.client.ItineraryClient;
import com.challenge.search.model.Itinerary;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest(classes = ItineraryClientTest.FeignConfig.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItineraryClientTest {

    @Configuration
    static class RibbonConfig {

        @LocalServerPort
        int port;

        @Bean
        public ServerList<Server> serverList() {
            return new StaticServerList<>(new Server("127.0.0.1", port), new Server("127.0.0.1", port));
        }
    }

    @EnableFeignClients(clients = ItineraryClient.class)
    @RestController
    @Configuration
    @EnableAutoConfiguration
    @RibbonClient(name = "itinerary-service", configuration = ItineraryClientTest.RibbonConfig.class)
    static class FeignConfig {

        @GetMapping(value = "/list")
        public ResponseEntity<List<Itinerary>> qwe(String origin, LocalTime departure) {

            List<Itinerary> itineraries = setupItineraries();

            List<Itinerary> filteredItineraries = itineraries.stream()
                    .filter(x -> {
                        if (origin == null & departure == null) {
                            return true;
                        } else if (origin == null) {
                            return x.getDeparture().isAfter(departure);
                        } else if (departure == null) {
                            return x.getOrigin().equals(origin);
                        } else {
                            return x.getOrigin().equals(origin) && x.getDeparture().isAfter(departure);
                        }
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filteredItineraries);
        }

    }

    @Autowired
    ItineraryClient itineraryClient;

    private static List<Itinerary> setupItineraries() {
        List<Itinerary> itineraries = new ArrayList<>();
        Itinerary itinerary = new Itinerary();
        itinerary.setId(1L);
        itinerary.setOrigin("origin");
        itinerary.setDestination("destination");
        itinerary.setDeparture(LocalTime.parse("13:10"));
        itinerary.setArrival(LocalTime.parse("14:10"));
        itinerary.setStatus("CREATED");
        itinerary.setCreateAt(ZonedDateTime.now());
        itineraries.add(itinerary);
        return itineraries;
    }

    @Test
    public void whenSearchAllItineraries_returnMockedList() {
        List<Itinerary> itineraries = this.itineraryClient.listItinerary(null, null).getBody();
        Assertions.assertThat(itineraries).size().isEqualTo(1);
    }

}