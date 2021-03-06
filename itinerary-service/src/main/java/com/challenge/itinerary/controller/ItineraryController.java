package com.challenge.itinerary.controller;

import com.challenge.itinerary.entity.Itinerary;
import com.challenge.itinerary.exceptions.ErrorMessage;
import com.challenge.itinerary.service.ItineraryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.xml.bind.DataBindingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/")
public class ItineraryController {

    @Autowired
    private ItineraryService itineraryService;

    @GetMapping(value = "/list")
    @ApiOperation(
            value = "${swagger.descriptions.list.value}",
            notes = "${swagger.descriptions.list.notes}")
    public ResponseEntity<List<Itinerary>> listItinerary(
            @RequestParam(name = "origin", required = false)
            @ApiParam(value = "${swagger.descriptions.origin.value}") String origin,
            @RequestParam(name = "departure", required = false) @DateTimeFormat(pattern = "HH:mm")
            @ApiParam(value = "${swagger.descriptions.departure.value}") Date departure) {
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
    @ApiOperation(value = "${swagger.descriptions.item-get.value}")
    public ResponseEntity<Itinerary> getItinerary(@PathVariable("id") Long id) {
        Itinerary product = itineraryService.getItinerary(id);
        if (product == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping(value = "/item", consumes = {"application/json"})
    @ApiOperation(
            value = "${swagger.descriptions.item-post.value}",
            notes = "${swagger.descriptions.item-post.notes}")
    public ResponseEntity<Itinerary> createItinerary(@Valid @RequestBody Itinerary itinerary,
                                                     BindingResult result) throws Exception {
        if (result.hasErrors()) {
            throw new DataBindingException(ErrorMessage.formatMessage(result), null);
        }
        Itinerary itineraryCreated = itineraryService.createItinerary(itinerary);
        return ResponseEntity.status(HttpStatus.CREATED).body(itineraryCreated);
    }

    @PutMapping(value = "/item/{id}")
    @ApiOperation(value = "${swagger.descriptions.item-put.value}")
    public ResponseEntity<Itinerary> updateItinerary(@PathVariable("id") Long id, @RequestBody Itinerary itinerary,
                                                     BindingResult result) throws Exception {
        if (result.hasErrors()) {
            throw new DataBindingException(ErrorMessage.formatMessage(result), null);
        }
        itinerary.setId(id);
        Itinerary itineraryDB = itineraryService.updateItinerary(itinerary);
        if (itineraryDB == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(itineraryDB);
    }

    @DeleteMapping(value = "/item/{id}")
    @ApiOperation(value = "${swagger.descriptions.item-delete.value}")
    public ResponseEntity<Itinerary> deleteItinerary(@PathVariable("id") Long id) {
        Itinerary itineraryDeleted = itineraryService.deleteItinerary(id);
        if (itineraryDeleted == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(itineraryDeleted);
    }


}
