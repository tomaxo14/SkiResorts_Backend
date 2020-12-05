package com.example.skiResorts.controller;


import com.example.skiResorts.service.TempResortService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin
public class TempResortController {

    private final TempResortService tempResortService;

    public TempResortController(TempResortService tempResortService) {
        this.tempResortService = tempResortService;
    }

    @GetMapping("/tempResorts")
    public ResponseEntity<?> getAllTempResorts() {
        return ResponseEntity.ok(tempResortService.getAllTempResorts());
    }

}
