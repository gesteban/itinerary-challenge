package com.challenge.search.pathfinder.dijsktra;

import com.challenge.search.client.ItineraryClient;
import com.challenge.search.model.Path;
import com.challenge.search.pathfinder.Strategy;

import java.util.List;

public class DijkstraShortestPathfinder extends DijkstraPathfinder {

    private Strategy strategy;

    public DijkstraShortestPathfinder(ItineraryClient itineraryClient, Strategy strategy) {
        super(itineraryClient);
        this.strategy = strategy;
    }

    @Override
    public List<Path> getPaths(String origin, String destination) {
        return getPaths(calculatePath(origin, destination, strategy));
    }

}
