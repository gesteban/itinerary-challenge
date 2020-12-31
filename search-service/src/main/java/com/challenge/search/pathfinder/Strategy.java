package com.challenge.search.pathfinder;

public enum Strategy {
    FASTEST("strategy.fastest"),
    SHORTEST("strategy.shortest"),
    CHEAPEST("strategy.cheapest");

    private String name;

    Strategy(String name) {
        this.name = name;
    }
}