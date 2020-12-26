package com.challenge.itinerary;

import com.challenge.itinerary.entity.Itinerary;
import com.challenge.itinerary.repository.ItineraryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@DataJpaTest
public class ItineraryRepositoryTest {

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Test
    public void whenFindByOrigin_thenReturnListItinerary(){

        Itinerary itinerary_01 = new Itinerary();
        itinerary_01.setOrigin("city-A");
        itinerary_01.setDestination("city-B");
        itinerary_01.setDeparture(new Date());
        itinerary_01.setArrival(new Date());
        itinerary_01.setStatus("Created");
        itinerary_01.setCreateAt(new Date());
        itineraryRepository.save(itinerary_01);

        List<Itinerary> founds = itineraryRepository.findByOrigin(itinerary_01.getOrigin());

        Assertions.assertThat(founds.size()).isEqualTo(3);
    }

}
