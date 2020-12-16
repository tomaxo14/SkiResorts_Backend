package com.example.skiResorts.service;

import com.example.skiResorts.entity.Location;
import com.example.skiResorts.entity.Resort;
import com.example.skiResorts.repository.LocationRepository;
import com.example.skiResorts.repository.ResortRepository;
import com.example.skiResorts.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ResortServiceTest {

    @Mock private ResortService resortService;
    @Mock private ResortRepository resortRepository;
    @Mock private CounterService counterService;
    @Mock private LocationRepository locationRepository;
    @Mock private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        resortService = new ResortService(resortRepository, counterService, locationRepository, userRepository);
        List<Resort> resorts = new ArrayList<Resort>();
        resorts.add(new Resort("Białka Tatrzańska", 10, 8, 3,
                4, 1, 5, 3, 1, false,
                "białka.pl", new Location("50.3", "18.5454", "Poland")));

        resorts.add(new Resort("Zieleniec", 2, 4, 6, 1,
                3, 5, 5, 1, true, "zieleniec.pl",
                new Location("51.52", "20.2825", "Poland")));
        resorts.get(0).setNumberOfRatings(1);
        resorts.get(1).setNumberOfRatings(2);
        when(resortRepository.findAll()).thenReturn(resorts);
    }

    @Test
    void preferredResortsTest() {

        List<Pair<Resort, Integer>> resortsWithPoints = resortService.preferredResorts(3,1,
                2, 4,4, 52.0, 19.0);
        for(Pair<Resort, Integer> pair: resortsWithPoints) {
            assertTrue(pair.getSecond() <= 100);
        }
    }
}
