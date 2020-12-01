package com.example.skiResorts.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Setter
@Getter
@Document(collection = "locations")
public class Location {

    @Id
    private int locationId;
    private String town;
    private String zipCode;
    private String longitude;
    private String latitude;
    private String country;

    public Location(String latitude, String longitude,  String country) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
    }

    public Location(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(String town, String zipCode, String longitude, String latitude) {
        this.town = town;
        this.zipCode = zipCode;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
