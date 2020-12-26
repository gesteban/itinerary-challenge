package com.challenge.search.service;

import com.challenge.search.model.Path;

import java.util.List;

public interface SearchService {

    public List<Path> findByLessTime(String origin, String destination);

    public List<Path> findByLessConnections(String origin, String destination);
}
