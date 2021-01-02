package com.challenge.search.client;

import com.challenge.search.model.Itinerary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Itinerary client Hystrix fallback.
 * <br/>
 * Defines the default response Hystrix is going to provide in case the itinerary service is
 * not available or the circuit is open.
 */
@Component
public class ItineraryHystrixFallback implements ItineraryClient {

    @Override
    public ResponseEntity<List<Itinerary>> listItinerary(String origin, LocalTime departure) {
        List<Itinerary> itineraries = new ArrayList<>();
        Itinerary itinerary = new Itinerary();
        itinerary.setId(1L);
        itinerary.setOrigin("city_A");
        itinerary.setDestination("city_C");
        itinerary.setDeparture(LocalTime.MIN);
        itinerary.setArrival(LocalTime.MAX);
        itinerary.setDurationInMinutes(1439L);
        itinerary.setStatus("HYSTRIX_CREATED");
        itineraries.add(itinerary);
        return ResponseEntity.ok(itineraries);
    }

}
