package com.challenge.search.service;

import com.challenge.search.client.ItineraryClient;
import com.challenge.search.model.Itinerary;
import com.challenge.search.model.Path;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class SearchServiceImpl implements SearchService {

    private Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    private ItineraryClient itineraryClient;

    @Override
    public List<Path> findByLessTime(String origin, String destination) {
        List<Node> nodes = calculateShortestPathInTime(origin, destination);
        List<Path> paths = new ArrayList<>();
        for (Node node : nodes) {
            List<Itinerary> itineraries = new ArrayList<>();
            itineraries.addAll(node.getPath().stream().map(x -> x.getItinerary()).collect(Collectors.toList()));
            itineraries.add(node.getItinerary());
            itineraries.remove(null);
            Path path = new Path();
            path.setTotalDuration(node.getDistance());
            path.setItineraries(itineraries);
            paths.add(path);
        }
        return paths;
    }

    @Override
    public List<Path> findByLessConnections(String origin, String destination) {
        List<Node> nodes = calculateShortestPathInConnections(origin, destination);
        List<Path> paths = new ArrayList<>();
        for (Node node : nodes) {
            List<Itinerary> itineraries = new ArrayList<>();
            itineraries.addAll(node.getPath().stream().map(x -> x.getItinerary()).collect(Collectors.toList()));
            itineraries.add(node.getItinerary());
            itineraries.remove(null);
            Path path = new Path();
            path.setTotalDuration(node.getDistance());
            path.setItineraries(itineraries);
            paths.add(path);
        }
        return paths;
    }

    /**
     * Class used to perform searches among {@link Itinerary itineraries}.
     * <br/>
     * A node represents the arrival to some city (specified by {@link #name}) at a given time
     * (specified by {@link #arrivalTime}). The node also contains the path followed to the city. Since to reach
     * a city there are multiple paths, a city can be represented by one or more nodes.
     */
    private class Node implements Comparable<Node> {

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
    }

    /**
     * Adaptation of Dijkstra's algorithm to search for the shortest path (in time) from two locations using
     * itinerary-service. The shortest path is calculated regardless of departure time.
     *
     * @param originName      name of the origin city
     * @param destinationName name of the destination city
     * @return a set of nodes that contains the possible paths from origin city to destination city
     */
    public List<Node> calculateShortestPathInTime(String originName, String destinationName) {

        // set initial node conditions
        Node origin = new Node(originName);
        origin.setDistance(0);

        // init dijkstra model
        Set<Node> visitedNodes = new HashSet<>();
        Set<Node> unvisitedNodes = new HashSet<>();
        Set<Node> endNodes = new HashSet<>();
        unvisitedNodes.add(origin);

        while (unvisitedNodes.size() != 0) {
            // select node and open it
            Node currentNode = getLowestDistanceNode(unvisitedNodes);
            unvisitedNodes.remove(currentNode);
            logger.debug("--- START LOOP");
            logger.debug("--- CURRENT NODE = " + currentNode);
            currentNode.getPath().stream().forEach(x -> logger.debug(x.toString()));
            // retrieve itineraries from current node
            ResponseEntity<List<Itinerary>> response = itineraryClient.listItinerary(
                    currentNode.getName(), currentNode.getArrivalTime());
            // filling itinerary list in case HttpStatus returned is 204 NO CONTENT
            List<Itinerary> itinerariesFromCurrentNode = new ArrayList<>();
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                itinerariesFromCurrentNode = response.getBody();
            }
            logger.debug("--- ITINERARIES FROM CURRENT NODE");
            itinerariesFromCurrentNode.stream().forEach(System.out::println);
            // calculate adjacent nodes, each node contains the proposed next city and the weight of the edge
            // between the current city and the one represented by the node
            currentNode.setAdjacentNodes(itinerariesFromCurrentNode.stream().collect(
                    Collectors.toMap(
                            x -> new Node(x),
                            y -> {
                                int weight = y.getDurationInMinutes().intValue();
                                if (currentNode.getArrivalTime() != null) {
                                    weight += MINUTES.between(currentNode.getArrivalTime(), y.getDeparture());
                                }
                                return weight;
                            })));
            logger.debug("--- ADJACENT NODES OF CURRENT NODES");
            currentNode.getAdjacentNodes().forEach((x, y) -> {
                logger.debug(x + " | edgeWeight=" + y);
            });
            // loop through adjacent nodes to decide if are to be visited or not
            for (Map.Entry<Node, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                // if adjacent node city is already visited in the path of the current node, ignore it
                if (!currentNode.getPath().stream().anyMatch(x -> x.getName().equals(adjacentNode.getName()))) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    // if adjacent node is destination, add to the end nodes instead of the unvisited nodes
                    if (adjacentNode.getName().equals(destinationName)) {
                        endNodes.add(adjacentNode);
                    } else {
                        unvisitedNodes.add(adjacentNode);
                    }
                }
            }
            visitedNodes.add(currentNode);
            logger.debug("--- VISITED NODES");
            visitedNodes.stream().forEach(System.out::println);
            logger.debug("--- UNVISITED NODES");
            unvisitedNodes.stream().forEach(System.out::println);
            logger.debug("--- END NODES");
            endNodes.stream().forEach(System.out::println);
        }
        logger.debug("--- RESULT");
        endNodes.stream().forEach(x -> {
            logger.debug(x.toString() + " > "+ x.getPath().stream().map(y -> y.toString()).collect(Collectors.joining(">")));
        });
        return endNodes.stream().sorted().collect(Collectors.toList());
    }

    public List<Node> calculateShortestPathInConnections(String originName, String destinationName) {

        // set initial node conditions
        Node origin = new Node(originName);
        origin.setDistance(0);

        // init dijkstra model
        Set<Node> visitedNodes = new HashSet<>();
        Set<Node> unvisitedNodes = new HashSet<>();
        Set<Node> endNodes = new HashSet<>();
        unvisitedNodes.add(origin);

        while (unvisitedNodes.size() != 0) {
            // select node and open it
            Node currentNode = getLowestDistanceNode(unvisitedNodes);
            unvisitedNodes.remove(currentNode);
            System.out.println("--- START LOOP");
            System.out.println("--- CURRENT NODE = " + currentNode);
            currentNode.getPath().stream().forEach(System.out::println);
            // retrieve itineraries from current node
            ResponseEntity<List<Itinerary>> response = itineraryClient.listItinerary(
                    currentNode.getName(), currentNode.getArrivalTime());
            // filling itinerary list in case HttpStatus returned is 204 NO CONTENT
            List<Itinerary> itinerariesFromCurrentNode = new ArrayList<>();
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                itinerariesFromCurrentNode = response.getBody();
            }
            System.out.println("--- ITINERARIES FROM CURRENT NODE");
            itinerariesFromCurrentNode.stream().forEach(System.out::println);
            // calculate adjacent nodes, each node contains the proposed next city and the weight of the edge
            // between the current city and the one represented by the node
            currentNode.setAdjacentNodes(itinerariesFromCurrentNode.stream().collect(
                    Collectors.toMap( x -> new Node(x), y -> 1)));
            System.out.println("--- ADJACENT NODES OF CURRENT NODES");
            currentNode.getAdjacentNodes().forEach((x, y) -> {
                System.out.print(x);
                System.out.println(" | edgeWeight=" + y);
            });
            // loop through adjacent nodes to decide if are to be visited or not
            for (Map.Entry<Node, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                // if adjacent node city is already visited in the path of the current node, ignore it
                if (!currentNode.getPath().stream().anyMatch(x -> x.getName().equals(adjacentNode.getName()))) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    // if adjacent node is destination, add to the end nodes instead of the unvisited nodes
                    if (adjacentNode.getName().equals(destinationName)) {
                        endNodes.add(adjacentNode);
                    } else {
                        unvisitedNodes.add(adjacentNode);
                    }
                }
            }
            visitedNodes.add(currentNode);
            System.out.println("--- VISITED NODES");
            visitedNodes.stream().forEach(System.out::println);
            System.out.println("--- UNVISITED NODES");
            unvisitedNodes.stream().forEach(System.out::println);
            System.out.println("--- END NODES");
            endNodes.stream().forEach(System.out::println);
        }
        System.out.println("--- RESULT");
        endNodes.stream().forEach(x -> {
            System.out.println(x);
            System.out.print("  ");
            x.getPath().stream().forEach(System.out::print);
            System.out.println();
        });
        return endNodes.stream().sorted().collect(Collectors.toList());
    }

    private static Node getLowestDistanceNode(Set<Node> unvisitedNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node : unvisitedNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private static void calculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getPath());
            shortestPath.add(sourceNode);
            evaluationNode.setPath(shortestPath);
        }
    }

}
