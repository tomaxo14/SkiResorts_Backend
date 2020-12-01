package com.example.skiResorts.controllers;

import com.example.skiResorts.entities.Preferences;
import com.example.skiResorts.entities.Resort;
import com.example.skiResorts.services.ResortService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/resortsWithGeo")
    public ResponseEntity<?> getAllResorts(@RequestParam double latitude, @RequestParam double longitude) {
        return ResponseEntity.ok(resortService.getAllResorts(latitude, longitude));
    }



    @GetMapping("/resortDetails")
    public ResponseEntity<?> getResortDetails(@RequestParam int resortId) {
        return ResponseEntity.ok(resortService.getResortDetails(resortId));
    }


    @PostMapping("/importSkiMaps")
    public ResponseEntity<?> importSkiMaps() throws IOException {
        return ResponseEntity.ok(resortService.importSkiMaps());
    }

    @GetMapping("/preferredResorts")
    public ResponseEntity<?> preferredResorts(@RequestParam int blue, @RequestParam int red, @RequestParam int black, @RequestParam int snowPark, @RequestParam int location,
                                              @RequestParam double userLat, @RequestParam double userLon) {
        return ResponseEntity.ok(resortService.preferredResorts(blue, red, black, snowPark, location, userLat, userLon));
    }

    @PostMapping("/addResort")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addResort(Principal principal, @RequestParam String name, @RequestParam int blue,  @RequestParam int red,  @RequestParam int black,
                                       @RequestParam int chairlifts, @RequestParam int gondolas,  @RequestParam int tBars,  @RequestParam int platters,
                                       @RequestParam int carpets, @RequestParam boolean snowpark,  @RequestParam String country, @RequestParam double latitude,
                                       @RequestParam double longitude,  @RequestParam String website) {
        return ResponseEntity.ok(resortService.addResort(name, blue, red, black, chairlifts, gondolas, tBars, platters, carpets, snowpark, country, latitude, longitude, website));
    }

    @PutMapping("/editResort")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> editResort(Principal principal, @RequestParam int resortId, @RequestParam String name, @RequestParam int blue,  @RequestParam int red,  @RequestParam int black,
                                       @RequestParam int chairlifts, @RequestParam int gondolas,  @RequestParam int tBars,  @RequestParam int platters,
                                       @RequestParam int carpets, @RequestParam boolean snowpark,  @RequestParam String country, @RequestParam double latitude,
                                       @RequestParam double longitude,  @RequestParam String website) {
        return ResponseEntity.ok(resortService.editResort(resortId, name, blue, red, black, chairlifts, gondolas, tBars, platters, carpets, snowpark, country, latitude, longitude, website));
    }

}
