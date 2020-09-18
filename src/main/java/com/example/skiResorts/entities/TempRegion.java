package com.example.skiResorts.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TempRegion {


    private String name;
    private String id;

    public TempRegion(String name) {
        this.name = name;
    }
}
