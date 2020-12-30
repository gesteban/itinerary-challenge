package com.challenge.search.client;

import com.challenge.search.model.Itinerary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItineraryHystrixFallbackFactory implements ItineraryClient {

    @Override
    public ResponseEntity<List<Itinerary>> listItinerary(String origin, LocalTime departure) {
        List<Itinerary> itineraries = new ArrayList<>();
        Itinerary itinerary = new Itinerary();
        itinerary.setId(1L);
        itinerary.setOrigin("city-A");
        itinerary.setDestination("city-E");
        itinerary.setDeparture(LocalTime.MIN);
        itinerary.setArrival(LocalTime.MAX);
        itinerary.setDurationInMinutes(1439L);
        itineraries.add(itinerary);
        return ResponseEntity.ok(itineraries);
    }

}
