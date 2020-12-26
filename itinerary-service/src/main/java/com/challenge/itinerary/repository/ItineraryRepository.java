package com.challenge.itinerary.repository;

import com.challenge.itinerary.entity.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

    public List<Itinerary> findByOrigin(String origin);
    public List<Itinerary> findByOriginAndDepartureGreaterThan(String origin, Date departure);
}
