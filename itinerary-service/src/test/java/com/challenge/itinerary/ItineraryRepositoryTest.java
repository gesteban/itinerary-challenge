package com.challenge.itinerary;

import com.challenge.itinerary.entity.Itinerary;
import com.challenge.itinerary.repository.ItineraryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class ItineraryRepositoryTest {

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Test
    public void whenFindByOrigin_thenReturnListItinerary(){

        Itinerary itinerary_01 = new Itinerary();
        itinerary_01.setOrigin("city_A");
        itinerary_01.setDestination("city_B");
        itinerary_01.setDeparture(new Date());
        itinerary_01.setArrival(new Date());
        itinerary_01.setStatus("Created");
        itinerary_01.setCreateAt(new Date());
        itineraryRepository.save(itinerary_01);

        List<Itinerary> founds = itineraryRepository.findByOrigin(itinerary_01.getOrigin());
        Assertions.assertThat(founds.size()).isEqualTo(4);
    }

    @Test
    public void whenFindByOriginAndDepartureGreaterThan_thenReturnListItinerary(){

        LocalTime lt = LocalTime.NOON;
        Instant instant = lt.atDate(LocalDate.of(1700, 1, 1)).
                atZone(ZoneId.systemDefault()).toInstant();
        Date noon = Date.from(instant);

        Itinerary itinerary_01 = new Itinerary();
        itinerary_01.setOrigin("city_A");
        itinerary_01.setDestination("city_B");
        itinerary_01.setDeparture(noon);
        itinerary_01.setArrival(new Date());
        itinerary_01.setStatus("Created");
        itinerary_01.setCreateAt(new Date());
        itineraryRepository.save(itinerary_01);

        List<Itinerary> founds = itineraryRepository.findByOriginAndDepartureGreaterThan(itinerary_01.getOrigin(), noon);
        Assertions.assertThat(founds.size()).isEqualTo(2);
    }

    @Test
    public void whenFindByDepartureGreaterThan_thenReturnListItinerary(){

        LocalTime lt = LocalTime.of(15,00);
        Instant instant = lt.atDate(LocalDate.of(1700, 1, 1)).
                atZone(ZoneId.systemDefault()).toInstant();
        Date fifteen = Date.from(instant);

        lt = LocalTime.NOON;
        instant = lt.atDate(LocalDate.of(1700, 1, 1)).
                atZone(ZoneId.systemDefault()).toInstant();
        Date noon = Date.from(instant);

        Itinerary itinerary_01 = new Itinerary();
        itinerary_01.setOrigin("city_A");
        itinerary_01.setDestination("city_B");
        itinerary_01.setDeparture(fifteen);
        itinerary_01.setArrival(new Date());
        itinerary_01.setStatus("Created");
        itinerary_01.setCreateAt(new Date());
        itineraryRepository.save(itinerary_01);

        List<Itinerary> founds = itineraryRepository.findByDepartureGreaterThan(noon);
        Assertions.assertThat(founds.size()).isEqualTo(8);
    }

}
