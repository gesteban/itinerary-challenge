package com.challenge.search.pathfinder;

/**
 * Strategy to be used for pathfinder problem resolution.
 */
public enum Strategy {
    /**
     * Fastest route between two locations in time.
     */
    FASTEST("strategy.fastest"),
    /**
     * Shortest route between two locations in number of connections.
     */
    SHORTEST("strategy.shortest"),
    FASTEST_IGNORE_WAITING_TIMES("strategy.shortestIgnoreWaitingTimes"),
    CHEAPEST("strategy.cheapest");

    private String name;

    Strategy(String name) {
        this.name = name;
    }
}