package com.example.skiResorts.repository;

import com.example.skiResorts.entities.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends MongoRepository<Location, Integer> {

    Location save(Location location);
}
