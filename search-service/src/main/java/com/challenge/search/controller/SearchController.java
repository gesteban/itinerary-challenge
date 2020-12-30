package com.challenge.search.controller;

import com.challenge.search.model.Path;
import com.challenge.search.service.SearchService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping(value = "/byLessTime")
    @ApiOperation(
            value = "${swagger.descriptions.byLessTime.value}",
            notes = "${swagger.descriptions.byLessTime.notes}")
    public ResponseEntity<List<Path>> findByLessTime(
            @RequestParam(name = "origin") String origin,
            @RequestParam(name = "destination") String destination) {
        return ResponseEntity.ok(searchService.findByLessTime(origin, destination));
    }

    @GetMapping(value = "/byLessConnections")
    @ApiOperation(
            value = "${swagger.descriptions.byLessConnections.value}",
            notes = "${swagger.descriptions.byLessConnections.notes}")
    public ResponseEntity<List<Path>> findByLessConnections(
            @RequestParam(name = "origin") String origin,
            @RequestParam(name = "destination") String destination) {
        return ResponseEntity.ok(searchService.findByLessConnections(origin, destination));
    }


}
