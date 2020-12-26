package com.challenge.search.client;

import com.challenge.search.model.Itinerary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class ItineraryHystrixFallbackFactory implements ItineraryClient {

    @Override
    public ResponseEntity<List<Itinerary>> listItinerary(String origin, LocalTime departure) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<Itinerary> getItinerary(Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
