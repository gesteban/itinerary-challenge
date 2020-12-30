package com.challenge.search.controller;

import com.challenge.search.model.Path;
import com.challenge.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping(value = "/byLessTime")
    public ResponseEntity<List<Path>> findByLessTime(
            @RequestParam(name = "origin") String origin,
            @RequestParam(name = "destination") String destination) {
        return ResponseEntity.ok(searchService.findByLessTime(origin, destination));
    }

    @GetMapping(value = "/byLessConnections")
    public ResponseEntity<List<Path>> findByLessConnections(
            @RequestParam(name = "origin") String origin,
            @RequestParam(name = "destination") String destination) {
        return ResponseEntity.ok(searchService.findByLessConnections(origin, destination));
    }


}
