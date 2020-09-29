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

    private String apiResortNumber;
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
    private int numberOfRatings;
    private int sumOfRatings;
    private String website;
    private Location location;
    private double avgRating;
    private String skiMap;


    public Resort(String apiResortNumber, String name, String website, Location location) {
        this.apiResortNumber = apiResortNumber;
        this.name = name;
        this.website = website;
        this.location = location;
    }

//    public Resort(String name, int foundationYear, int blueSlopes, int redSlopes, int blackSlopes, int chairlifts,
//                  int gondolas, int tBars, int platters, int carpets, boolean ifSnowPark) {
//        this.name = name;
//        this.foundationYear = foundationYear;
//        this.blueSlopes = blueSlopes;
//        this.redSlopes = redSlopes;
//        this.blackSlopes = blackSlopes;
//        this.chairlifts = chairlifts;
//        this.gondolas = gondolas;
//        this.tBars = tBars;
//        this.platters = platters;
//        this.carpets = carpets;
//        this.ifSnowPark = ifSnowPark;
//        this.numberOfRatings = 0;
//        this.sumOfRatings = 0;
//    }

    public void incrementRatings() {
        numberOfRatings++;
    }

    public void decrementRatings() {
        numberOfRatings--;
    }


    public void addToSum(int newestValue){
        sumOfRatings+=newestValue;
    }

    public void updateAvgRating () {
        avgRating = (double)sumOfRatings/numberOfRatings;
    }
}
