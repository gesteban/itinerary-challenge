package com.challenge.search.pathfinder;

import com.challenge.search.client.ItineraryClient;
import com.challenge.search.model.Path;

import java.util.List;

public abstract class Pathfinder {

    public ItineraryClient itineraryClient;

    public Pathfinder(ItineraryClient itineraryClient) {
        this.itineraryClient = itineraryClient;
    }

    public abstract List<Path> getPaths(String origin, String destination);

}
