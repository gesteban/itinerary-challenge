package com.challenge.search.pathfinder;

import com.challenge.search.client.ItineraryClient;

/**
 * Abstract factory class for Pathfinder
 */
public abstract class PathfinderFactory {

    public abstract Pathfinder getPathfinder(ItineraryClient itineraryClient, Strategy strategy);

}
