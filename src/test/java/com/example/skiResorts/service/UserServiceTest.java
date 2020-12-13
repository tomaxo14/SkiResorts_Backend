package com.example.skiResorts.service;
import com.example.skiResorts.entity.Location;
import com.example.skiResorts.entity.Rating;
import com.example.skiResorts.entity.Resort;
import com.example.skiResorts.entity.User;
import com.example.skiResorts.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Mock private UserService userService;
    @Mock private UserRepository userRepository;
    @Mock private ResortRepository resortRepository;
    @Mock private RatingRepository ratingRepository;
    @Mock private CounterService counterService;
    @Mock private PreferencesRepository preferencesRepository;
    @Mock private LocationRepository locationRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, resortRepository, ratingRepository, counterService, preferencesRepository, locationRepository,
                passwordEncoder);
        Resort resort = new Resort("Zieleniec", 2, 4, 6, 1, 3,
                5, 5, 1, true, "zieleniec.pl", new Location("51.52", "20.2825", "Poland"));
        resort.setResortId(1);
        User user = new User("test", "imie", "nazwisko", "haslo", "email@email.pl");
        HashSet<Resort> favourites = new HashSet<>();
        Resort favResort = new Resort("Białka Tatrzańska", 10, 8, 3, 4, 1,
                5, 3, 1, false, "białka.pl", new Location("50.3", "18.5454", "Poland"));
        favResort.setResortId(2);
        favourites.add(favResort);
        user.setFavourites(favourites);
        Rating rating = new Rating(1, "test", 4, new Date(), "");
        HashSet<Rating> ratings = new HashSet<>();
        ratings.add(rating);
        user.setRatings(ratings);
        Optional<User> userOpt = Optional.of(user);
        Optional<Resort> resortOpt = Optional.of(resort);
        Optional<Resort> favResortOpt = Optional.of(favResort);
        when(userService.getUser("test")).thenReturn(userOpt);
        when(resortRepository.findById(1)).thenReturn(resortOpt);
        when(resortRepository.findById(2)).thenReturn(favResortOpt);
    }

    @Test
    void addFavouriteTest() {
        int status = userService.addFavourite("test", 1);
        assertEquals(status, 200);
    }

    @Test
    void addFavouriteAlreadyInFavouritesTest() {
        int status = userService.addFavourite("test", 2);
        assertEquals(status, 3);
    }

    @Test
    void deleteFavouriteTest() {
        int status = userService.deleteFavourite("test", 2);
        assertEquals(status, 200);
    }

    @Test
    void deleteFavouriteNotInFavouritesTest() {
        int status = userService.deleteFavourite("test", 1);
        assertEquals(status, 6);
    }

    @Test
    void yourFavouritesTest() {
        Set<Resort> favourites = userService.yourFavourites("test");
        assertEquals(favourites.size(), 1);
    }

    @Test
    void yourRatingsTest() {
        List<Rating> ratings = userService.yourRatings("test");
        assertEquals(ratings.size(), 1);
    }
}
