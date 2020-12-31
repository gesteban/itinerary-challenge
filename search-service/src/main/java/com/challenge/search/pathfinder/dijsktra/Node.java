package com.challenge.search.pathfinder.dijsktra;

import com.challenge.search.model.Itinerary;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Node implements Comparable<Node> {

    private String name;
    private LocalTime arrivalTime;
    private Integer distance = Integer.MAX_VALUE;
    private List<Node> path = new LinkedList<>();
    private Map<Node, Integer> adjacentNodes = new HashMap<>();
    private Itinerary itinerary;

    public Node(String name) {
        this.name = name;
    }

    /**
     * Creates a node that represents the arrival to the destination of the given itinerary.
     *
     * @param itinerary
     */
    public Node(Itinerary itinerary) {
        this.name = itinerary.getDestination();
        this.arrivalTime = itinerary.getArrival();
        this.itinerary = itinerary;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setAdjacentNodes(Map<Node, Integer> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    public List<Node> getPath() {
        return path;
    }

    public void setPath(List<Node> path) {
        this.path = path;
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", distance=" + distance +
                ", arrivalTime=" + arrivalTime +
                '}';
    }

    @Override
    public int compareTo(Node o) {
        return getDistance().compareTo(o.getDistance());
    }

    public String pathToString() {
        return getPath().stream().map(x -> x.toString()).collect(Collectors.joining(" > "));
    }

}