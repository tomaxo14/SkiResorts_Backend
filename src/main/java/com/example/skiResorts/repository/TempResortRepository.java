package com.example.skiResorts.repository;


import com.example.skiResorts.entity.TempResort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TempResortRepository extends MongoRepository<TempResort, Integer> {

    List<TempResort> findAll();
}
