package com.challenge.search.pathfinder;

import com.challenge.search.client.ItineraryClient;

/**
 * Abstract factory class for pathfinder problem resolution using a given strategy.
 */
public abstract class PathfinderFactory {

    public abstract Pathfinder getPathfinder(ItineraryClient itineraryClient, Strategy strategy);

}
