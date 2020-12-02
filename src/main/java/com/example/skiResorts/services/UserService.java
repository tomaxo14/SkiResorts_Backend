package com.example.skiResorts.services;

import com.example.skiResorts.entities.*;
import com.example.skiResorts.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    public final static int STATUS_OK = 200;
    public final static int USER_NOT_FOUND = 1;
    public final static int RESORT_NOT_FOUND = 2;
    public final static int RESORT_ALREADY_IN_FAVOURITES = 3;
    public final static int RATING_CHANGED = 4;
    public final static int VALUE_NOT_IN_RANGE = 5;
    public final static int RESORT_NOT_IN_FAVOURITES = 6;
    public final static int PREFERENCES_CHANGED = 7;
    public final static int INCORRECT_PASSWORD = 8;
    public final static int PASSWORDS_NOT_EQUALS = 9;

    private final UserRepository userRepository;
    private final ResortRepository resortRepository;
    private final RatingRepository ratingRepository;
    private final CounterService counterService;
    private final PreferencesRepository preferencesRepository;
    private final LocationRepository locationRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, ResortRepository resortRepository, RatingRepository ratingRepository, CounterService counterService,
                       PreferencesRepository preferencesRepository, LocationRepository locationRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.resortRepository = resortRepository;
        this.ratingRepository = ratingRepository;
        this.counterService = counterService;
        this.preferencesRepository = preferencesRepository;
        this.locationRepository = locationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> getUser(String login) {
        return userRepository.findByLogin(login);
    }
    public int addFavourite(String login, int resortId) {

        Optional<User> userOpt = getUser(login);
        User user;
        if(userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            return USER_NOT_FOUND;
        }

        Optional<Resort> resortOpt = resortRepository.findById(resortId);
        Resort resort;
        if(resortOpt.isPresent()) {
            resort = resortOpt.get();
        } else {
           return RESORT_NOT_FOUND;
        }

        boolean alreadyInFavourites = false;
        for(Resort favResort : user.getFavourites()){
            if(favResort.getResortId()==resortId){
                alreadyInFavourites=true;
                break;
            }
        }
        if (!alreadyInFavourites) {
            user.addFavourite(resort);
            userRepository.save(user);
        } else {
            return RESORT_ALREADY_IN_FAVOURITES;
        }

        return STATUS_OK;
    }

    public int deleteFavourite(String login, int resortId) {

        Optional<User> userOpt = getUser(login);
        User user;
        if(userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            return USER_NOT_FOUND;
        }

        Optional<Resort> resortOpt = resortRepository.findById(resortId);
        Resort resort;
        if(resortOpt.isPresent()) {
            resort = resortOpt.get();
        } else {
            return RESORT_NOT_FOUND;
        }

        boolean alreadyInFavourites = false;
        for(Resort favResort : user.getFavourites()){
            if(favResort.getResortId()==resortId){
                alreadyInFavourites=true;
                break;
            }
        }
        if (!alreadyInFavourites) {
            return RESORT_NOT_IN_FAVOURITES;
        } else {
            user.removeFavourite(resort);
            userRepository.save(user);
        }

        return STATUS_OK;
    }

    public int rateResort(String login, int resortId, int value, String message) {

        if(!Rating.possibleValues.contains(value)) {
            return VALUE_NOT_IN_RANGE;
        }

        Optional<User> userOpt = getUser(login);
        User user;
        if(userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            return USER_NOT_FOUND;
        }

        Optional<Resort> resortOpt = resortRepository.findById(resortId);
        Resort resort;
        if(resortOpt.isPresent()) {
            resort = resortOpt.get();
        } else {
            return RESORT_NOT_FOUND;
        }

        boolean alreadyRated = false;
        Rating oldRating=null;
        int oldValue=0;


        if(user.getRatings()!=null) {
            for (Rating rating : user.getRatings()) {
                if (rating.getResort() == resortId) {
                    alreadyRated = true;
                    oldRating = rating;
                    oldValue = rating.getValue();
                    break;
                }
            }
        }

        if(alreadyRated){
            ratingRepository.delete(oldRating);
            user.getRatings().remove(oldRating);
            if(oldRating.getMessage()!=null) {
                resort.getOpinions().remove(oldRating);
            }
            resort.addToSum(-oldValue);
            resort.decrementRatings();
            resort.updateAvgRating();
        }

        Rating rating = new Rating(resortId, login, value, new Date(), message);
        rating.setRatingId(counterService.getNextId("rating"));
        ratingRepository.save(rating);

        resort.incrementRatings();
        resort.addToSum(value);
        resort.updateAvgRating();
        if (rating.getMessage()!=null) {
            resort.addOpinion(rating);
        }
        resortRepository.save(resort);

        user.addRating(rating);
        userRepository.save(user);

        if(alreadyRated){
            return RATING_CHANGED;
        }

        return STATUS_OK;
    }

    public List<Rating> yourRatings(String login) {
        Optional<User> userOpt = getUser(login);
        User user = userOpt.get();
        Set<Rating> ratings = user.getRatings();
        List<Rating> ratingsWithResortName = new ArrayList<>();
        if(ratings!=null) {
            for (Rating rating : ratings) {
                Optional<Resort> resortOpt = resortRepository.findById(rating.getResort());
                Resort resort = null;
                if (resortOpt.isPresent()) {
                    resort = resortOpt.get();
                }
                rating.setResortName(resort.getName());
                ratingsWithResortName.add(rating);
            }
        }

        return ratingsWithResortName;
    }

    public Set<Resort> yourFavourites(String login) {
        Optional<User> userOpt = getUser(login);
        User user = userOpt.get();

        return  user.getFavourites();
    }

    public List<Resort> getFavouritesWithGeo(String login, double latitude, double longitude) {

        Set<Resort> favourites = yourFavourites(login);
        List<Resort> favouritesWithDistance = new ArrayList<>();
        if (latitude == 0 && longitude == 0) {
            favouritesWithDistance.addAll(favourites);
            return favouritesWithDistance;
        }

        for (Resort resort : favourites) {
            double resortLat = Double.parseDouble(resort.getLocation().getLatitude());
            double resortLong = Double.parseDouble(resort.getLocation().getLongitude());
            double longDiff = longitude - resortLong;
            double distance = Math.sin(degToRad(latitude)) * Math.sin(degToRad(resortLat)) + Math.cos(degToRad(latitude)) * Math.cos(degToRad(resortLat)) * Math.cos(degToRad(longDiff));
            distance = Math.acos(distance);
            distance = radToDeg(distance);
            distance = distance * 60 * 1.1515 * 1.609344;
            resort.setDistance(distance);
            favouritesWithDistance.add(resort);
        }
        return favouritesWithDistance;
    }

    private double degToRad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double radToDeg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public int saveLocation(String login, double latitude, double longitude) {
        Optional<User> userOpt = getUser(login);
        User user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            return USER_NOT_FOUND;
        }
        Location location = new Location(String.valueOf(latitude), String.valueOf(longitude));
        location.setLocationId(counterService.getNextId("location"));
        locationRepository.save(location);
        user.setLocation(location);
        userRepository.save(user);
        return STATUS_OK;
    }

    public int addPreferences(String login, int blue, int red, int black, int snowPark, int location) {

        Optional<User> userOpt = getUser(login);
        User user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            return USER_NOT_FOUND;
        }

        if (!Preferences.possibleValues.contains(blue) || !Preferences.possibleValues.contains(red) || !Preferences.possibleValues.contains(black)
                || !Preferences.possibleValues.contains(snowPark) || !Preferences.possibleValues.contains(location)) {
            return VALUE_NOT_IN_RANGE;
        }

        boolean alreadyHasPreferences = false;
        if (user.getPreferences() != null) {
            alreadyHasPreferences = true;
        }
        Preferences newPreferences = new Preferences(blue, red, black, snowPark, location);
        Preferences oldPreferences = null;
        List<Preferences> allPreferences = preferencesRepository.findAll();
        for(Preferences preferences : allPreferences) {
            if(newPreferences.equals(preferences)) {
                oldPreferences = preferences;
                break;
            }
        }

        if(oldPreferences==null) {
            newPreferences.setPreferencesId(counterService.getNextId("preferences"));
            preferencesRepository.save(newPreferences);
            user.setPreferences(newPreferences);
        } else {
            user.setPreferences(oldPreferences);
        }
        userRepository.save(user);

        if (alreadyHasPreferences) {
            return PREFERENCES_CHANGED;
        }

        return STATUS_OK;
    }

    public Preferences yourPreferences(String login) {
        Optional<User> userOpt = getUser(login);
        User user = userOpt.get();

        return  user.getPreferences();
    }

    public Location yourLocation(String login) {
        Optional<User> userOpt = getUser(login);
        User user = userOpt.get();

        return  user.getLocation();
    }

    public int changePassword(String login, String oldPassword,
                              String newPassword1, String newPassword2) {
        if (newPassword1.equals(newPassword2)) {
            Optional<User> userOpt = getUser(login);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(newPassword1));
                    userRepository.save(user);
                    return STATUS_OK;
                } else {
                    return INCORRECT_PASSWORD;
                }
            }
            return USER_NOT_FOUND;
        }

        return PASSWORDS_NOT_EQUALS;
    }
}
