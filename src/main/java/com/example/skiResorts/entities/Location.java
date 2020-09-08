package com.example.skiResorts.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Setter
@Getter
public class Location {

    @Id
    private int locationId;
    private String town;
    private String zipCode;
    private double longitude;
    private double latitude;

    public Location(String town, String zipCode, double longitude, double latitude) {
        this.town = town;
        this.zipCode = zipCode;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
