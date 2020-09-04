package com.example.skiResorts.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    public JwtResponse(String token, String login, String email, String name, String surname, List<String> roles) {
        this.token = token;
        this.login = login;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.roles = roles;
    }
}
