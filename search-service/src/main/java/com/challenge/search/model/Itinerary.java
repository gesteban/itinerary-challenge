package com.challenge.search.model;

import io.swagger.annotations.ApiModelProperty;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZonedDateTime;

/**
 * Represents a movement from two cities.
 */
public class Itinerary implements Comparable<Itinerary> {

    private Long id;
    @ApiModelProperty(example = "city-A")
    private String origin;
    @ApiModelProperty(example = "city-C")
    private String destination;
    @ApiModelProperty(dataType = "java.lang.String", example = "16:35")
    private LocalTime departure;
    @ApiModelProperty(dataType = "java.lang.String", example = "17:05")
    private LocalTime arrival;
    @ApiModelProperty(dataType = "java.lang.String", example = "PT30M")
    private Duration duration;
    @ApiModelProperty(example = "30")
    private Long durationInMinutes;
    @ApiModelProperty(example = "CREATED")
    private String status;
    private ZonedDateTime createAt;

    public Itinerary() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalTime departure) {
        this.departure = departure;
    }

    public LocalTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalTime arrival) {
        this.arrival = arrival;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Long getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Long durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }

    @Override
    public String toString() {
        return "Itinerary{" +
                "id=" + id +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departure=" + departure +
                ", arrival=" + arrival +
                ", duration=" + duration +
                ", durationInMinutes=" + durationInMinutes +
                '}';
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    @Override
    public int compareTo(Itinerary o) {
        return getDurationInMinutes().compareTo(o.getDurationInMinutes());
    }
}
