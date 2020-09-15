package com.example.skiResorts.repository;

import com.example.skiResorts.entities.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends MongoRepository<Rating, Integer> {

    Rating save(Rating rating);
}
