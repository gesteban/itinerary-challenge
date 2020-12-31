package com.challenge.search.pathfinder.dijsktra;

import com.challenge.search.client.ItineraryClient;
import com.challenge.search.exceptions.PathfinderException;
import com.challenge.search.model.Itinerary;
import com.challenge.search.model.Path;
import com.challenge.search.pathfinder.Pathfinder;
import com.challenge.search.pathfinder.Strategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Base class for Dijkstra-based implementations of pathfinder strategies.
 */
public abstract class DijkstraPathfinder extends Pathfinder {

    Logger logger = LoggerFactory.getLogger(DijkstraFastestPathfinder.class);

    public DijkstraPathfinder(ItineraryClient itineraryClient) {
        super(itineraryClient);
    }

    public static List<Path> getPaths(List<Node> nodes) {
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

    protected static Node getLowestDistanceNode(Set<Node> unvisitedNodes) {
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

    protected static void calculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getPath());
            shortestPath.add(sourceNode);
            evaluationNode.setPath(shortestPath);
        }
    }

    public List<Node> calculatePath(String originName, String destinationName, Strategy strategy) {

        // set initial node conditions
        Node origin = new Node(originName);
        origin.setDistance(0);

        // init dijkstra model
        Set<Node> visitedNodes = new HashSet<>();
        Set<Node> unvisitedNodes = new HashSet<>();
        Set<Node> endNodes = new HashSet<>();
        unvisitedNodes.add(origin);

        int i = 1;
        while (unvisitedNodes.size() != 0) {
            // select node and open it
            Node currentNode = getLowestDistanceNode(unvisitedNodes);
            unvisitedNodes.remove(currentNode);
            logger.debug(String.format("LOOP[%02d] Current node: %s", i, currentNode));
            logger.debug(String.format("LOOP[%02d] Current path: [ %s ]", i, currentNode.pathToString()));
            // retrieve itineraries from current node
            logger.debug(String.format("LOOP[%02d] Fetching itineraries from '%s' departing after %s", i,
                    currentNode.getName(), currentNode.getArrivalTime()));
            ResponseEntity<List<Itinerary>> response = itineraryClient.listItinerary(
                    currentNode.getName(), currentNode.getArrivalTime());
            // filling itinerary list in case HttpStatus returned is 204 NO CONTENT
            List<Itinerary> itinerariesFromCurrentNode = new ArrayList<>();
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                itinerariesFromCurrentNode = response.getBody();
            }
            logger.debug(String.format("LOOP[%02d] Itineraries retrieved: [ %s ]", i,
                    itinerariesFromCurrentNode.stream().map(x -> x.getId().toString())
                            .collect(Collectors.joining(", "))));
            // calculate adjacent nodes, each node contains the proposed next city and the weight of the edge
            // between the current city and the one represented by the node
            logger.debug(String.format("LOOP[%02d] Transforming itineraries to adjacent nodes", i));
            currentNode.setAdjacentNodes(itinerariesFromCurrentNode.stream().collect(
                    Collectors.toMap(
                            x -> new Node(x),
                            y -> {
                                switch(strategy) {
                                    case SHORTEST:
                                        return 1;
                                    case FASTEST:
                                        int weight = y.getDurationInMinutes().intValue();
                                        if (currentNode.getArrivalTime() != null) {
                                            weight += MINUTES.between(currentNode.getArrivalTime(), y.getDeparture());
                                        }
                                        return weight;
                                    default:
                                        throw new PathfinderException("strategy not implemented in Dijkstra");
                                }
                            })));
            logger.debug(String.format("LOOP[%02d] Adjacent nodes: [ %s ]", i,
                    currentNode.getAdjacentNodes().entrySet().stream().map(
                            x -> new StringBuilder()
                                    .append("{id=")
                                    .append(x.getKey().getItinerary().getId())
                                    .append(", weight=")
                                    .append(x.getValue())
                                    .append("}")
                    ).collect(Collectors.joining(", "))));
            // loop through adjacent nodes to decide if are to be visited or not
            logger.debug(String.format("LOOP[%02d] Moving adjacent nodes to visited/unvisited nodes", i));
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
            logger.debug(String.format("LOOP[%02d] Visited nodes: [ %s ]", i,
                    visitedNodes.stream().map(x -> x.toString()).collect(Collectors.joining(", "))));
            logger.debug(String.format("LOOP[%02d] Unvisited nodes: [ %s ]", i,
                    unvisitedNodes.stream().map(x -> x.toString()).collect(Collectors.joining(", "))));
            logger.debug(String.format("LOOP[%02d] End nodes: [ %s ]", i,
                    endNodes.stream().map(x -> x.toString()).collect(Collectors.joining(", "))));
            i++;
        }
        return endNodes.stream().sorted().collect(Collectors.toList());
    }
}
