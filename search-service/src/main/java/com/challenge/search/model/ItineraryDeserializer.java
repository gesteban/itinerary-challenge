package com.challenge.search.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public class ItineraryDeserializer extends StdDeserializer<Itinerary> {

    public ItineraryDeserializer() {
        this(null);
    }

    public ItineraryDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Itinerary deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        Itinerary itinerary = new Itinerary();
        itinerary.setId(node.get("id").asLong());
        itinerary.setOrigin(node.get("origin").asText());
        itinerary.setDestination(node.get("destination").asText());
        itinerary.setDeparture(LocalTime.parse(node.get("departure").asText()));
        itinerary.setArrival(LocalTime.parse(node.get("arrival").asText()));
        itinerary.setDuration(Duration.parse(node.get("duration").asText()));
        itinerary.setDurationInMinutes(node.get("durationInMinutes").asLong());
        itinerary.setStatus(node.get("status").asText());
        itinerary.setCreateAt(ZonedDateTime.parse(node.get("createAt").asText()));
        return itinerary;
    }
}
