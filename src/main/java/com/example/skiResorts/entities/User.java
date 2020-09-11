package com.example.skiResorts.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Document(collection = "users")
public class User {

    @Id
    private String login;

    private String name;
    private String surname;
    private String password;
    private String email;

    @DBRef
    private Set<Role> roles = new HashSet<>();

    private Set<Resort> favourites = new HashSet<>();
    private Location location;

    public User(){

    }

    public User(String login, String name, String surname, String password, String email) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.email = email;
    }

    public void addFavourite(Resort favourite) {
        if (favourites == null) {
            favourites= new HashSet<>();
        }
        favourites.add(favourite);
    }
}
