package com.challenge.search.pathfinder.dijsktra;

import com.challenge.search.client.ItineraryClient;
import com.challenge.search.model.Path;
import com.challenge.search.pathfinder.Strategy;

import java.util.List;

/**
 * Dijkstra-based shortest path strategy implementation of pathfinder problem for itineraries.
 * <br/>
 * This implementation returns the possible paths from two nodes in increasing order of connection number. It
 * doesn't take into account the weights of the edges neither the duration of the itineraries to order the
 * All edges are weighted the same and durations of itineraries are ignored. This means that the first element
 * returned by {@link #getPaths} is the shortest path (in number of connections) between given nodes.
 */
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
