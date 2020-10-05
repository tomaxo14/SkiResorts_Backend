package com.example.skiResorts.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

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
    private String message;
    public static List<Integer> possibleValues = List.of(1, 2, 3, 4, 5);


    public Rating(int resort, String user, int value, Date date, String message) {
        this.resort = resort;
        this.user = user;
        this.value = value;
        this.date = date;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return ratingId == rating.ratingId &&
                resort == rating.resort &&
                value == rating.value &&
                user.equals(rating.user) &&
                date.equals(rating.date) &&
                Objects.equals(message, rating.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ratingId, resort, user, value, date, message);
    }
}
