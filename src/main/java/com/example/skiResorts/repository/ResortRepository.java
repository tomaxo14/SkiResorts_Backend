package com.example.skiResorts.repository;

import com.example.skiResorts.entities.Resort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResortRepository extends MongoRepository<Resort, Integer> {

    Resort save(Resort resort);

}
