package com.example.skiResorts.service;

import com.example.skiResorts.entity.*;
import com.example.skiResorts.repository.LocationRepository;
import com.example.skiResorts.repository.ResortRepository;
import com.example.skiResorts.repository.TempResortRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TempResortService {

    private final TempResortRepository tempResortRepository;
    private final ResortRepository resortRepository;
    private final LocationRepository locationRepository;
    private final CounterService counterService;

    public TempResortService(TempResortRepository tempResortRepository, ResortRepository resortRepository, LocationRepository locationRepository,
                             CounterService counterService) {
        this.tempResortRepository = tempResortRepository;
        this.resortRepository = resortRepository;
        this.locationRepository = locationRepository;
        this.counterService = counterService;
    }

    public List<TempResort> getAllTempResorts() {

        List<TempResort> tempResorts = tempResortRepository.findAll();

        for (TempResort tempResort: tempResorts){
            Location location = new Location(tempResort.getSkiArea().getGeo_lat(),
                    tempResort.getSkiArea().getGeo_lng(),tempResort.getRegion()[0].getName());
            Resort resort = new Resort(tempResort.getSkiArea().getNumber(), tempResort.getSkiArea().getName(),
                    tempResort.getSkiArea().getOfficial_website(), location);
            location.setLocationId(counterService.getNextId("location"));
            locationRepository.save(location);
            resort.setResortId(counterService.getNextId("resort"));
            resortRepository.save(resort);
            System.out.println("Zapisano resort: " + resort.getName());

        }

        return tempResorts;
    }
}
