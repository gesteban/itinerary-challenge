package com.challenge.itinerary.controller;

import com.challenge.itinerary.entity.Itinerary;
import com.challenge.itinerary.service.ItineraryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ExampleProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/")
public class ItineraryController {

    @Autowired
    private ItineraryService itineraryService;

    @GetMapping(value = "/list")
    public ResponseEntity<List<Itinerary>> listItinerary(
            @RequestParam(name = "origin", required = false)
            @ApiParam(value = "name of the origin city, e.g. city-A") String origin,
            @RequestParam(name = "departure", required = false) @DateTimeFormat(pattern = "HH:mm")
            @ApiParam(value = "maximum time of departure, e.g. 10:00") Date departure) {
        List<Itinerary> itineraries = new ArrayList<>();
        if (origin == null && departure == null) {
            itineraries = itineraryService.listAllItinerary();
        } else if (departure == null) {
            itineraries = itineraryService.findByOrigin(origin);
        } else if (origin == null) {
            itineraries = itineraryService.findByDepartureGreaterThan(departure);
        } else {
            itineraries = itineraryService.findByOriginAndDepartureGreaterThan(origin, departure);
        }
        if (itineraries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(itineraries);
    }

    @GetMapping(value = "/item/{id}")
    public ResponseEntity<Itinerary> getItinerary(@PathVariable("id") Long id) {
        Itinerary product = itineraryService.getItinerary(id);
        if (product == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping(value = "/item", consumes = {"application/json"})
    public ResponseEntity<Itinerary> createItinerary(@Valid @RequestBody Itinerary itinerary, BindingResult result) {
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, formatMessage(result));
        }
        Itinerary itineraryCreated = itineraryService.createItinerary(itinerary);
        return ResponseEntity.status(HttpStatus.CREATED).body(itineraryCreated);
    }

    @PutMapping(value = "/item/{id}")
    public ResponseEntity<Itinerary> updateItinerary(@PathVariable("id") Long id, @RequestBody Itinerary itinerary) {
        itinerary.setId(id);
        Itinerary itineraryDB = itineraryService.updateItinerary(itinerary);
        if (itineraryDB == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(itineraryDB);
    }

    @DeleteMapping(value = "/item/{id}")
    public ResponseEntity<Itinerary> deleteItinerary(@PathVariable("id") Long id) {
        Itinerary itineraryDeleted = itineraryService.deleteItinerary(id);
        if (itineraryDeleted == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(itineraryDeleted);
    }

    private static String formatMessage(BindingResult result) {
        List<Map<String, String>> errors = result.getFieldErrors().stream()
                .map(err -> {
                    Map<String, String> error = new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());
        ErrorMessage errorMessage = new ErrorMessage("01", errors);
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

}
