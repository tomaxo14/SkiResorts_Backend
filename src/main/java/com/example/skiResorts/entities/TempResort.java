package com.example.skiResorts.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.ObjectError;

import java.util.Set;


@NoArgsConstructor
@Getter
@Setter
@Document(collection = "tempResorts")
public class TempResort {

    @Id
    private ObjectId id;
    private TempSkiArea SkiArea;
    private TempRegion[] Region;

    public TempResort(TempSkiArea skiArea, TempRegion[] region) {
        this.SkiArea = skiArea;
        this.Region = region;
    }
}
