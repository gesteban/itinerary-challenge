package com.challenge.search;

import com.challenge.search.client.ItineraryClient;
import com.challenge.search.model.Itinerary;
import com.challenge.search.model.Path;
import com.challenge.search.pathfinder.Pathfinder;
import com.challenge.search.pathfinder.PathfinderFactory;
import com.challenge.search.pathfinder.Strategy;
import com.challenge.search.pathfinder.dijsktra.DijkstraPathfinderFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PathfinderTest {

    @Component
    public class ItineraryClientMock implements ItineraryClient {
        @Override
        public ResponseEntity<List<Itinerary>> listItinerary(String origin, LocalTime departure) {
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

    private static List<Itinerary> setupItineraries() {
        List<Itinerary> itineraries = new ArrayList<>();
        itineraries.add(createItinerary(1L,"city_A", "city_B", "13:10", "14:00"));
        itineraries.add(createItinerary(2L, "city_A", "city_B", "16:00", "16:30"));
        itineraries.add(createItinerary(3L, "city_B", "city_C", "14:00", "14:30"));
        itineraries.add(createItinerary(4L, "city_B", "city_C", "18:00", "20:00"));
        itineraries.add(createItinerary(5L, "city_A", "city_D", "08:00", "09:30"));
        itineraries.add(createItinerary(6L, "city_B", "city_D", "11:00", "11:30"));
        itineraries.add(createItinerary(7L, "city_D", "city_C", "14:00", "15:00"));
        itineraries.add(createItinerary(8L, "city_C", "city_D", "14:00", "15:00"));
        itineraries.add(createItinerary(9L, "city_C", "city_E", "12:00", "13:00"));
        itineraries.add(createItinerary(10L, "city_D", "city_E", "12:00", "15:00"));
        itineraries.add(createItinerary(11L, "city_E", "city_D", "15:30", "16:30"));
        return itineraries;
    }

    private static Itinerary createItinerary(long id, String origin, String destination, String departure, String arrival) {
        Itinerary itinerary = new Itinerary();
        itinerary.setId(id);
        itinerary.setOrigin(origin);
        itinerary.setDestination(destination);
        LocalTime departureTime = LocalTime.parse(departure);
        LocalTime arrivalTime = LocalTime.parse(arrival);
        itinerary.setDeparture(departureTime);
        itinerary.setArrival(arrivalTime);
        itinerary.setDurationInMinutes(departureTime.until(arrivalTime, ChronoUnit.MINUTES));
        itinerary.setStatus("Created");
        itinerary.setCreateAt(ZonedDateTime.now());
        return itinerary;
    }

    @Test
    public void whenSearchingPaths_thenReturnResults() {
        PathfinderFactory factory = new DijkstraPathfinderFactory();
        Pathfinder pathfinder = factory.getPathfinder(new ItineraryClientMock(), Strategy.FASTEST);
        List<Path> pathList = pathfinder.getPaths("city_A", "city_C");
        Assertions.assertThat(pathList).size().isEqualTo(3);
    }

}
