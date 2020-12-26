package com.challenge.search.service;

import com.challenge.search.model.Path;

public interface SearchService {

    public Path findByLessTime(String origin, String destination);

    public Path findByLessConnections(String origin, String destination);
}
