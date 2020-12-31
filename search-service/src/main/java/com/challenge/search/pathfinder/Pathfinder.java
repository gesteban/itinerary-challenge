package com.challenge.search.pathfinder;

import com.challenge.search.client.ItineraryClient;
import com.challenge.search.model.Path;

import java.util.List;

/**
 * Abstract class representing a pathfinder problem resolution using {@link com.challenge.search.model.Itinerary}
 * class as edges.
 */
public abstract class Pathfinder {

    public ItineraryClient itineraryClient;

    public Pathfinder(ItineraryClient itineraryClient) {
        this.itineraryClient = itineraryClient;
    }

    public abstract List<Path> getPaths(String origin, String destination);

}
