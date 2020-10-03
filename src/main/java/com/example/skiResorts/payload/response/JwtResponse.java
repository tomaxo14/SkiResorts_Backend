package com.example.skiResorts.payload.response;

import com.example.skiResorts.entities.Rating;
import com.example.skiResorts.entities.Resort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String login;
    private String email;
    private String name;
    private String surname;
    private List<String> roles;
    private Set<Resort> favourites;
    private Set<Rating> ratings;

    public JwtResponse(String token, String login, String email, String name, String surname, List<String> roles,
                       Set<Resort> favourites, Set<Rating> ratings) {
        this.token = token;
        this.login = login;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.roles = roles;
        this.favourites = favourites;
        this.ratings = ratings;
    }
}
