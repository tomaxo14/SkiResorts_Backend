package com.example.skiResorts.repository;

import com.example.skiResorts.entities.Preferences;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferencesRepository extends MongoRepository<Preferences, Integer> {

    Preferences save(Preferences preferences);
    List<Preferences> findAll();
}
