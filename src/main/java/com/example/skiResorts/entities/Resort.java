package com.example.skiResorts.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    private Set<Rating> opinions;
    private double distance;


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

    public void addOpinion(Rating rating) {
        if(opinions == null) {
            opinions = new HashSet<>();
        }
        opinions.add(rating);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resort resort = (Resort) o;
        return resortId == resort.resortId &&
                foundationYear == resort.foundationYear &&
                blueSlopes == resort.blueSlopes &&
                redSlopes == resort.redSlopes &&
                blackSlopes == resort.blackSlopes &&
                chairlifts == resort.chairlifts &&
                gondolas == resort.gondolas &&
                tBars == resort.tBars &&
                platters == resort.platters &&
                carpets == resort.carpets &&
                ifSnowPark == resort.ifSnowPark &&
                numberOfRatings == resort.numberOfRatings &&
                sumOfRatings == resort.sumOfRatings &&
                Double.compare(resort.avgRating, avgRating) == 0 &&
                Objects.equals(apiResortNumber, resort.apiResortNumber) &&
                Objects.equals(name, resort.name) &&
                Objects.equals(website, resort.website) &&
                Objects.equals(location, resort.location) &&
                Objects.equals(skiMap, resort.skiMap) &&
                Objects.equals(opinions, resort.opinions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resortId, apiResortNumber, name, foundationYear, blueSlopes, redSlopes, blackSlopes, chairlifts, gondolas, tBars, platters, carpets, ifSnowPark, numberOfRatings, sumOfRatings, website, location, avgRating, skiMap, opinions);
    }
}
