package com.example.skiResorts.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
public class TempSkiArea {

    private String id;
    private String name;
    private String official_website;
    private String geo_lat;
    private String geo_lng;
    private String top_elevation;
    private String bottom_elevation;
    private String vertical_drop;
    private String operating_status;
    private boolean has_downhill;
    private boolean has_nordic;

    public TempSkiArea(String name, String official_website, String geo_lat, String geo_lng, String top_elevation, String bottom_elevation, String vertical_drop, String operating_status, boolean has_downhill, boolean has_nordic) {

        this.name = name;
        this.official_website = official_website;
        this.geo_lat = geo_lat;
        this.geo_lng = geo_lng;
        this.top_elevation = top_elevation;
        this.bottom_elevation = bottom_elevation;
        this.vertical_drop = vertical_drop;
        this.operating_status = operating_status;
        this.has_downhill = has_downhill;
        this.has_nordic = has_nordic;
    }
}
