package com.example.skiResorts.services;

import com.example.skiResorts.entities.Counter;
import com.example.skiResorts.entities.Resort;
import com.example.skiResorts.repository.ResortRepository;
import org.springframework.stereotype.Service;

@Service
public class ResortService {
    public static final int STATUS_OK = 200;

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
}
