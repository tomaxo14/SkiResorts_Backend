package com.example.skiResorts.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Document(collection = "ratings")
public class Rating {


    @Id
    private int ratingId;

    private int resort;
    private String user;
    private int value;
    private Date date;
    public static List<Integer> possibleValues = List.of(1, 2, 3, 4, 5);


    public Rating(int resort, String user, int value, Date date) {
        this.resort = resort;
        this.user = user;
        this.value = value;
        this.date = date;
    }
}
