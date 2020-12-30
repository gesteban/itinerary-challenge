package com.challenge.search.client;

import com.challenge.search.model.Itinerary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;
import java.util.List;

@FeignClient(name = "itinerary-service", fallback = ItineraryHystrixFallbackFactory.class)
public interface ItineraryClient {

    @GetMapping(value = "/list")
    public ResponseEntity<List<Itinerary>> listItinerary(
            @RequestParam(name = "origin", required = false) String origin,
            @RequestParam(name = "departure", required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime departure);

}
