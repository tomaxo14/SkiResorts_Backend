package com.example.skiResorts.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "roles")
public class Role {

    @Id
    private int roleId;

    private ERole name;


    public Role(ERole name) {
        this.name = name;
    }
}


