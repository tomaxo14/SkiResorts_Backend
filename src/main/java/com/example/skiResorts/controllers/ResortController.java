package com.example.skiResorts.controllers;

import com.example.skiResorts.entities.Resort;
import com.example.skiResorts.services.ResortService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.Principal;

@Controller
@CrossOrigin
public class ResortController {

    private final ResortService resortService;

    public ResortController(ResortService resortService) {
        this.resortService = resortService;
    }

//    @PostMapping("/addResort")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public ResponseEntity<?> addResort(Principal principal) {
//
//        Resort resort = new Resort("Białka Tatrzańska", 1990, 10, 8, 3, 4, 1, 5, 3, 1, false);
//        return ResponseEntity.ok(resortService.addResort(resort));
//    }

    @GetMapping("/resorts")
    public ResponseEntity<?> getAllResorts() {
        return ResponseEntity.ok(resortService.getAllResorts());
    }

//    @GetMapping("/resort")
//    public ResponseEntity<?> getResort(@RequestParam int resortId) {
//        return ResponseEntity.ok(resortService.getResort(resortId));
//    }


    @PostMapping("/importSkiMaps")
    public ResponseEntity<?> importSkiMaps() throws IOException {
        return ResponseEntity.ok(resortService.importSkiMaps());
    }

}
