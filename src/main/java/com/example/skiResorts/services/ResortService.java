package com.example.skiResorts.services;

import com.example.skiResorts.entities.Counter;
import com.example.skiResorts.entities.Resort;
import com.example.skiResorts.repository.ResortRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResortService {
    public static final int STATUS_OK = 200;
    public static final int RESORT_NOT_FOUND = 1;

    private final ResortRepository resortRepository;
    private final CounterService counterService;

    public ResortService(ResortRepository resortRepository, CounterService counterService){
        this.resortRepository = resortRepository;
        this.counterService = counterService;
    }

    public int addResort(Resort resort) {

        resort.setResortId(counterService.getNextId("resort"));
        resortRepository.save(resort);
        return  STATUS_OK;
    }

    public List<Resort> getAllResorts() {
        return resortRepository.findAll();
    }

//    public Resort getResort (int resortId) {
//        Optional<Resort> resortOpt = resortRepository.findById(resortId);
//        Resort resort;
//        if (resortOpt.isPresent()){
//            resort = resortOpt.get();
//        } else {
//            return RESORT_NOT_FOUND;
//        }
//        return resortRepository.findById(resortId);
//    }

}
