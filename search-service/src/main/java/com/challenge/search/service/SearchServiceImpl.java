package com.challenge.search.service;

import com.challenge.search.client.ItineraryClient;
import com.challenge.search.model.Path;
import com.challenge.search.pathfinder.Pathfinder;
import com.challenge.search.pathfinder.PathfinderFactory;
import com.challenge.search.pathfinder.Strategy;
import com.challenge.search.pathfinder.dijsktra.DijkstraPathfinderFactory;
import com.challenge.search.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Search service implementation.
 */
@Service
public class SearchServiceImpl implements SearchService {

    Logger logger = LoggerFactory.getLogger(SearchService.class);
    PathfinderFactory factory = new DijkstraPathfinderFactory();

    @Autowired
    private ItineraryClient itineraryClient;

    @Override
    public List<Path> findByLessTime(String origin, String destination) {
        Pathfinder pathfinder = factory.getPathfinder(itineraryClient, Strategy.FASTEST);
        return pathfinder.getPaths(origin, destination);
    }

    @Override
    public List<Path> findByLessConnections(String origin, String destination) {
        Pathfinder pathfinder = factory.getPathfinder(itineraryClient, Strategy.SHORTEST);
        return pathfinder.getPaths(origin, destination);
    }

}
