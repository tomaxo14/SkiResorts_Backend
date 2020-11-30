package com.example.skiResorts.services;

import com.example.skiResorts.entities.Preferences;
import com.example.skiResorts.entities.Rating;
import com.example.skiResorts.entities.Resort;
import com.example.skiResorts.entities.User;
import com.example.skiResorts.repository.PreferencesRepository;
import com.example.skiResorts.repository.RatingRepository;
import com.example.skiResorts.repository.ResortRepository;
import com.example.skiResorts.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final ResortRepository resortRepository;
    private final RatingRepository ratingRepository;
    private final CounterService counterService;
    private final PreferencesRepository preferencesRepository;


    public UserService(UserRepository userRepository, ResortRepository resortRepository, RatingRepository ratingRepository, CounterService counterService,
                       PreferencesRepository preferencesRepository) {
        this.userRepository = userRepository;
        this.resortRepository = resortRepository;
        this.ratingRepository = ratingRepository;
        this.counterService = counterService;
        this.preferencesRepository = preferencesRepository;
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

    public Set<Rating> yourRatings(String login) {
        Optional<User> userOpt = getUser(login);
        User user = userOpt.get();

        return user.getRatings();
    }

    public Set<Resort> yourFavourites(String login) {
        Optional<User> userOpt = getUser(login);
        User user = userOpt.get();

        return  user.getFavourites();
    }

    public List<Resort>getFavouritesWithGeo(String login, double latitude, double longitude) {

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

}
