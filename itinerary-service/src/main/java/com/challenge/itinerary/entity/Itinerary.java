package com.challenge.itinerary.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "tbl_itineraries")
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String origin;
    private String destination;
    @Temporal(TemporalType.TIME)
    private Date departure;
    @Temporal(TemporalType.TIME)
    private Date arrival;
    @Transient
    private Duration duration;
    @Transient
    private Long durationInMinutes;
    private String status;
    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    public Itinerary() {
    }

    public Itinerary(Long id, String origin, String destination, Date departure, Date arrival, String status, Date createAt) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.departure = departure;
        this.arrival = arrival;
        this.status = status;
        this.createAt = createAt;
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

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public Duration getDuration() {
        long diffInMilliseconds = Math.abs(getArrival().getTime() - getDeparture().getTime());
        return Duration.ofMillis(diffInMilliseconds);
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Long getDurationInMinutes() {
        long diffInMilliseconds = Math.abs(getArrival().getTime() - getDeparture().getTime());
        return TimeUnit.MINUTES.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Itinerary itinerary = (Itinerary) o;
        return id.equals(itinerary.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
