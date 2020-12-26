package com.challenge.search.service;

import com.challenge.search.client.ItineraryClient;
import com.challenge.search.model.Itinerary;
import com.challenge.search.model.Path;
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

    @Autowired
    private ItineraryClient itineraryClient;

    @Override
    public Path findByLessTime(String origin, String destination) {


        Set<Node> nodes = calculateShortestPathFromSource(origin, destination);
        System.out.println("--- RESULT");
        nodes.stream().forEach(x -> {
            System.out.println(x);
            System.out.print("  ");
            x.getPath().stream().forEach(System.out::print);
            System.out.println();
        });


        Path path = new Path();
        path.setName("pedro");
//        path.setItineraries(itineraryClient.listItinerary(origin, LocalTime.now()).getBody());
//        ArrayList<Path> list = new ArrayList<>();
//        list.add(path);
        return path;
    }

    @Override
    public Path findByLessConnections(String origin, String destination) {
        return new Path();
    }

    /**
     * Class used to perform searches among {@link Itinerary itineraries}.
     * <br/>
     * A node represents the arrival to some city (specified by {@link #name}) at a given time
     * (specified by {@link #arrivalTime}). The node also contains the path followed to the city. Since to reach
     * a city there are multiple paths, a city can be represented by one or more nodes.
     */
    public class Node {

        private String name;
        private LocalTime arrivalTime;
        private Integer distance = Integer.MAX_VALUE;
        private List<Node> path = new LinkedList<>();
        private Map<Node, Integer> adjacentNodes = new HashMap<>();

        public Node(String name, LocalTime arrivalTime) {
            this.name = name;
            this.arrivalTime = arrivalTime;
        }

        /**
         * Creates a node
         * @param itinerary
         */
        public Node(Itinerary itinerary) {
            this.name = itinerary.getDestination();
            this.arrivalTime = itinerary.getArrival();
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

        @Override
        public String toString() {
            return "Node{" +
                    "name='" + name + '\'' +
                    ", distance=" + distance +
                    ", arrivalTime=" + arrivalTime +
                    '}';
        }
    }

    public class Graph {

        private Set<Node> nodes = new HashSet<>();

        public void addNode(Node nodeA) {
            nodes.add(nodeA);
        }
    }

    /**
     * Adaptation of Dijkstra's algorithm to search for the shortest path (in time) from two locations using
     * itinerary-service. The shortest path is calculated regardless of departure time.
     *
     * @param originName name of the origin city
     * @param destinationName name of the destination city
     * @return a set of nodes that contains the possible path from origin city to destination city
     */
    public Set<Node> calculateShortestPathFromSource(String originName, String destinationName) {

        // set initial node conditions
        Node origin = new Node(originName, null); // special null arrivalTime value for origin node
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
                    Collectors.toMap(
                            x -> new Node(x.getDestination(), x.getArrival()),
                            y -> {
                                int weight = y.getDurationInMinutes().intValue();
                                if (currentNode.getArrivalTime() != null) {
                                    weight += MINUTES.between(currentNode.getArrivalTime(), y.getDeparture());
                                }
                                return weight;
                            })));
            System.out.println("--- ADJACENT NODES OF CURRENT NODES");
            currentNode.getAdjacentNodes().forEach((x, y) -> {
                System.out.print(x); System.out.println(" | edgeWeight=" + y);
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
        return endNodes;
    }

    private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node : unsettledNodes) {
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
