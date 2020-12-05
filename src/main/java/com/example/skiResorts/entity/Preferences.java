package com.example.skiResorts.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Setter
@Getter
@Document(collection = "preferences")
public class Preferences {

    @Id
    private int preferencesId;
    private int blueSlopes;
    private int redSlopes;
    private int blackSlopes;
    private int snowPark;
    private int location;
    public static List<Integer> possibleValues = List.of(1, 2, 3, 4, 5);


    public Preferences(int blueSlopes, int redSlopes, int blackSlopes, int snowPark, int location) {
        this.blueSlopes = blueSlopes;
        this.redSlopes = redSlopes;
        this.blackSlopes = blackSlopes;
        this.snowPark = snowPark;
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Preferences that = (Preferences) o;
        return blueSlopes == that.blueSlopes &&
                redSlopes == that.redSlopes &&
                blackSlopes == that.blackSlopes &&
                snowPark == that.snowPark &&
                location == that.location;
    }

    @Override
    public int hashCode() {
        return Objects.hash(preferencesId, blueSlopes, redSlopes, blackSlopes, snowPark, location);
    }
}
