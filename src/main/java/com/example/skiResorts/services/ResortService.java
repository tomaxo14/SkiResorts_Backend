package com.example.skiResorts.services;

import com.example.skiResorts.entities.Resort;
import com.example.skiResorts.repository.ResortRepository;
import org.springframework.stereotype.Service;

@Service
public class ResortService {
    public static final int STATUS_OK = 200;
    private final ResortRepository resortRepository;

    public ResortService(ResortRepository resortRepository){
        this.resortRepository = resortRepository;
    }

    public int addResort(Resort resort) {

        resortRepository.save(resort);
        return  STATUS_OK;
    }
}
