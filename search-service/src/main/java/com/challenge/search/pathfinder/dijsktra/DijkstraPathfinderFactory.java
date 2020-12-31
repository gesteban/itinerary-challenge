package com.challenge.search.pathfinder.dijsktra;

import com.challenge.search.client.ItineraryClient;
import com.challenge.search.exceptions.PathfinderException;
import com.challenge.search.pathfinder.PathfinderFactory;
import com.challenge.search.pathfinder.Strategy;

public class DijkstraPathfinderFactory extends PathfinderFactory {

    @Override
    public DijkstraPathfinder getPathfinder(ItineraryClient itineraryClient, Strategy strategy) throws RuntimeException {
        switch (strategy) {
            case SHORTEST:
                return new DijkstraShortestPathfinder(itineraryClient, strategy);
            case FASTEST:
                return new DijkstraFastestPathfinder(itineraryClient, strategy);
            default:
                throw new PathfinderException("no Dijkstra implementation available of this strategy");
        }
    }
}
