package com.challenge.itinerary.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;

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
        // alternative to SimpleDateFormat conversion
//        LocalTime localTime = LocalTime.parse(node.get("departure").asText());
//        Instant instant = localTime.atDate(LocalDate.MIN).atZone(ZoneId.systemDefault()).toInstant();
//        Date date = Date.from(instant);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        try {
            itinerary.setDeparture(formatter.parse(node.get("departure").asText()));
            itinerary.setArrival(formatter.parse(node.get("arrival").asText()));
        } catch (ParseException ex) {
            throw new IOException(ex);
        }
        itinerary.setDuration(Duration.parse(node.get("duration").asText()));
        itinerary.setDurationInMinutes(node.get("durationInMinutes").asLong());
        itinerary.setStatus(node.get("status").asText());
        itinerary.setCreateAt(Date.from(ZonedDateTime.parse(node.get("createAt").asText()).toInstant()));
        return itinerary;
    }
}
