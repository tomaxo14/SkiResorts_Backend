package com.example.skiResorts.services;

import com.example.skiResorts.entities.Resort;
import com.example.skiResorts.entities.User;
import com.example.skiResorts.repository.ResortRepository;
import com.example.skiResorts.repository.UserRepository;
import org.springframework.stereotype.Service;

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

    private final UserRepository userRepository;
    private final ResortRepository resortRepository;


    public UserService(UserRepository userRepository, ResortRepository resortRepository) {
        this.userRepository = userRepository;
        this.resortRepository = resortRepository;
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
}
