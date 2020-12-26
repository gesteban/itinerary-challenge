package com.challenge.itinerary.service;

import com.challenge.itinerary.entity.Itinerary;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface ItineraryService {

    public List<Itinerary> listAllItinerary();

    public Itinerary getItinerary(Long id);

    public Itinerary createItinerary(Itinerary itinerary);

    public Itinerary updateItinerary(Itinerary itinerary);

    public Itinerary deleteItinerary(Long id);

    public List<Itinerary> findByOrigin(String origin);

    public List<Itinerary> findByOriginAndDepartureGreaterThan(String origin, Date departure);

}
