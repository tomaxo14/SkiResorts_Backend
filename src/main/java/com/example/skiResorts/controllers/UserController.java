package com.example.skiResorts.controllers;


import com.example.skiResorts.entities.Resort;
import com.example.skiResorts.services.ResortService;
import com.example.skiResorts.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/addFavourite")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addFavourite(Principal principal, @RequestParam int resortId) {
        String login = principal.getName();
        int status = userService.addFavourite(login, resortId);

        switch (status) {
            case UserService.STATUS_OK:
                return ResponseEntity.ok("Dodano ośrodek do ulubionych");
            case UserService.USER_NOT_FOUND:
                return ResponseEntity.badRequest().body("Nie znaleziono użytkownika");
            case UserService.RESORT_NOT_FOUND:
                return ResponseEntity.badRequest().body("Nie znaleziono ośrodka o podanym id");
            case UserService.RESORT_ALREADY_IN_FAVOURITES:
                return ResponseEntity.badRequest().body("Ten ośrodek jest już w ulubionych");
            default:
                return ResponseEntity.badRequest().body("Nie udało się dodać ośrodka do ulubionych");
        }
    }

    @PostMapping("rateResort")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> rateResort(Principal principal, @RequestParam int resortId, @RequestParam int value, @RequestParam(required = false) String message) {
        String login = principal.getName();
        int status = userService.rateResort(login, resortId, value, message);
        switch (status) {
            case UserService.STATUS_OK:
                return ResponseEntity.ok("Wystawiono ocenę");
            case UserService.RATING_CHANGED:
                return ResponseEntity.ok("Zmieniono ocenę");
            case UserService.VALUE_NOT_IN_RANGE:
                return ResponseEntity.badRequest().body("Wartość oceny musi być liczbą całkowitą z przediału <1,5>");
            case UserService.USER_NOT_FOUND:
                return ResponseEntity.badRequest().body("Nie znaleziono użytkownika");
            case UserService.RESORT_NOT_FOUND:
                return ResponseEntity.badRequest().body("Nie znaleziono ośrodka o podanym id");
            default:
                return ResponseEntity.badRequest().body("Nie udało się ocenić ośrodka");
        }
    }
}
