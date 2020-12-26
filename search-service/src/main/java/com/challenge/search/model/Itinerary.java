package com.challenge.search.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@JsonDeserialize(using = ItineraryDeserializer.class)
public class Itinerary implements Comparable<Itinerary> {

    private Long id;
    private String origin;
    private String destination;
    private LocalTime departure;
    private LocalTime arrival;
    private Duration duration;
    private Long durationInMinutes;
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
