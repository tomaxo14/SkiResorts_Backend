package com.example.skiResorts.services;

import com.example.skiResorts.entities.Rating;
import com.example.skiResorts.entities.Resort;
import com.example.skiResorts.entities.User;
import com.example.skiResorts.repository.RatingRepository;
import com.example.skiResorts.repository.ResortRepository;
import com.example.skiResorts.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    public final static int STATUS_OK = 200;
    public final static int USER_NOT_FOUND = 1;
    public final static int RESORT_NOT_FOUND = 2;
    public final static int RESORT_ALREADY_IN_FAVOURITES = 3;
    public final static int RATING_CHANGED = 4;
    public final static int VALUE_NOT_IN_RANGE = 5;
    public final static int RESORT_NOT_IN_FAVOURITES = 6;

    private final UserRepository userRepository;
    private final ResortRepository resortRepository;
    private final RatingRepository ratingRepository;
    private final CounterService counterService;


    public UserService(UserRepository userRepository, ResortRepository resortRepository, RatingRepository ratingRepository, CounterService counterService) {
        this.userRepository = userRepository;
        this.resortRepository = resortRepository;
        this.ratingRepository = ratingRepository;
        this.counterService = counterService;
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

}
