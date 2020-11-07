package com.example.skiResorts.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Objects;
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

    private Set<Resort> favourites;
    private Set<Rating> ratings;
    private Location location;
    private Preferences preferences;

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
            favourites = new HashSet<>();
        }
        favourites.add(favourite);
    }

    public void removeFavourite(Resort resort) {
        favourites.removeIf(favourite -> favourite.getResortId() == resort.getResortId());
    }

    public void addRating(Rating rating) {
        if (ratings == null) {
            ratings = new HashSet<>();
        }
        ratings.add(rating);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(roles, user.roles) &&
                Objects.equals(favourites, user.favourites) &&
                Objects.equals(ratings, user.ratings) &&
                Objects.equals(location, user.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, name, surname, password, email, roles, favourites, ratings, location);
    }
}
