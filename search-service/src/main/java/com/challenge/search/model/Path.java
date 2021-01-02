package com.challenge.search.model;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Path.
 * <br/>
 * Path to follow in order to go from one city to another. It is composed by multiple itineraries.
 */
@Component
public class Path {

    /**
     * Total duration of the path among cities, included the times waiting for the next itinerary.
     */
    @ApiModelProperty(example = "30")
    private Integer totalDuration;
    /**
     * List of the itineraries are part of the path.
     */
    private List<Itinerary> itineraries;

    public Path() {
    }

    public Integer getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
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
