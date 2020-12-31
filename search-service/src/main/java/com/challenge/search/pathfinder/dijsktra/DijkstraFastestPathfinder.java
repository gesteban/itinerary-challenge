package com.challenge.search.pathfinder.dijsktra;

import com.challenge.search.client.ItineraryClient;
import com.challenge.search.model.Path;
import com.challenge.search.pathfinder.Strategy;

import java.util.List;

/**
 * Dijkstra-based shortest path strategy implementation of pathfinder problem for itineraries.
 * <br/>
 * This implementation takes into account the weights of the edges and the duration of the itineraries to order
 * the possible paths. The first element returned by {@link #getPaths} is the fastest path (in time) between
 * given nodes.
 */
public class DijkstraFastestPathfinder extends DijkstraPathfinder {

    private Strategy strategy;

    public DijkstraFastestPathfinder(ItineraryClient itineraryClient, Strategy strategy) {
        super(itineraryClient);
        this.strategy = strategy;
    }

    @Override
    public List<Path> getPaths(String origin, String destination) {
        return getPaths(calculatePath(origin, destination, strategy));
    }


}