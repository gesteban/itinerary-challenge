package com.challenge.itinerary.service;

import com.challenge.itinerary.entity.Itinerary;
import com.challenge.itinerary.repository.ItineraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ItineraryServiceImpl implements ItineraryService {

    private ItineraryRepository itineraryRepository;

    @Autowired
    public ItineraryServiceImpl(ItineraryRepository itineraryRepository) {
        this.itineraryRepository = itineraryRepository;
    }

    @Override
    public List<Itinerary> listAllItinerary() {
        return itineraryRepository.findAll();
    }

    @Override
    public Itinerary getItinerary(Long id) {
        return itineraryRepository.findById(id).orElse(null);
    }

    @Override
    public Itinerary createItinerary(Itinerary itinerary) {
        itinerary.setStatus("CREATED");
        itinerary.setCreateAt(new Date());
        return itineraryRepository.save(itinerary);
    }

    @Override
    public Itinerary updateItinerary(Itinerary itinerary) {
        Itinerary itineraryDB = getItinerary(itinerary.getId());
        if (itineraryDB == null) {
            return null;
        }
        itineraryDB.setOrigin(itinerary.getOrigin());
        itineraryDB.setDestination(itinerary.getDestination());
        itineraryDB.setDeparture(itinerary.getDeparture());
        itineraryDB.setArrival(itinerary.getArrival());
        return itineraryRepository.save(itineraryDB);
    }

    @Override
    public Itinerary deleteItinerary(Long id) {
        Itinerary itineraryDB = getItinerary(id);
        if (itineraryDB == null) {
            return null;
        }
        itineraryDB.setStatus("DELETED");
        return itineraryRepository.save(itineraryDB);
    }

    @Override
    public List<Itinerary> findByOrigin(String origin) {
        return itineraryRepository.findByOrigin(origin);
    }

    @Override
    public List<Itinerary> findByOriginAndDepartureGreaterThan(String origin, Date departure) {
        return itineraryRepository.findByOriginAndDepartureGreaterThan(origin, departure);
    }

    @Override
    public List<Itinerary> findByDepartureGreaterThan(Date departure) {
        return itineraryRepository.findByDepartureGreaterThan(departure);
    }

}
