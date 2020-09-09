package com.example.skiResorts.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "resorts")
public class Resort {

    @Id
    private int resortId;

    private String name;
    private int foundationYear;
    private int blueSlopes;
    private int redSlopes;
    private int blackSlopes;
    private int chairlifts;
    private int gondolas;
    private int tBars;
    private int platters;
    private int carpets;
    private boolean ifSnowPark;

    public Resort(String name, int foundationYear, int blueSlopes, int redSlopes, int blackSlopes, int chairlifts,
                  int gondolas, int tBars, int platters, int carpets, boolean ifSnowPark) {
        this.name = name;
        this.foundationYear = foundationYear;
        this.blueSlopes = blueSlopes;
        this.redSlopes = redSlopes;
        this.blackSlopes = blackSlopes;
        this.chairlifts = chairlifts;
        this.gondolas = gondolas;
        this.tBars = tBars;
        this.platters = platters;
        this.carpets = carpets;
        this.ifSnowPark = ifSnowPark;
    }
}
