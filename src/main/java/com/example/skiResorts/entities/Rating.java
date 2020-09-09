package com.example.skiResorts.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Date;  // można zmienić na util

@Getter
@Setter
@Document(collection = "ratings")
public class Rating {

    @Id
    private int ratingId;

    private int resort;
    private int user;
    private int value;
    private Date date;

    public Rating(int resort, int user, int value, Date date) {
        this.resort = resort;
        this.user = user;
        this.value = value;
        this.date = date;
    }
}
