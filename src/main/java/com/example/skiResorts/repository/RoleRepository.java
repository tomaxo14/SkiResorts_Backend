package com.example.skiResorts.repository;

import com.example.skiResorts.entities.ERole;
import com.example.skiResorts.entities.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
