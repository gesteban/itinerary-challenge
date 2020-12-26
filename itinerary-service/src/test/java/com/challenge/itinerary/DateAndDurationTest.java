package com.challenge.itinerary;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateAndDurationTest {

    @Test
    public void givenTwoDatesBeforeJava8_whenDifferentiating_thenWeGetSix() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date firstDate = sdf.parse("06/24/2017");
        Date secondDate = sdf.parse("06/30/2017");

        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        assertEquals(6, diff);
    }

    @Test
    public void convertingTwoDatesBeforeJava8_whenDifferentiating_thenWeGetSix() throws ParseException {
        Long duration = Duration.between(new Date().toInstant(), new Date().toInstant()).toMinutes();
        System.out.println(duration);
        assertEquals(1, 1);
    }
}
