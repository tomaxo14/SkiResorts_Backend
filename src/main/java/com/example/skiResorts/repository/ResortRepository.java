package com.example.skiResorts.repository;

import com.example.skiResorts.entities.Resort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResortRepository extends MongoRepository<Resort, Integer> {

    Resort save(Resort resort);
    List<Resort> findAll();
    Optional<Resort> findById(Integer resortId);
    Optional<Resort> findResortByApiResortNumber(String apiId);

}
