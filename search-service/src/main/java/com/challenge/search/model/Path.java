package com.challenge.search.model;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class Path {

    private String name;
    private List<Itinerary> itineraries;

    public Path() {
    }

    public Path(String name) {
        this.name = name;
    }

    public String getName() {
        return "pedro";
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Itinerary> getItineraries() {
        return itineraries;
    }

    public void setItineraries(List<Itinerary> itineraries) {
        this.itineraries = itineraries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Objects.equals(itineraries, path.itineraries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itineraries);
    }
}
