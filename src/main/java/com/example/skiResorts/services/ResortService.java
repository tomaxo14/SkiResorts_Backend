package com.example.skiResorts.services;

import com.example.skiResorts.entities.Counter;
import com.example.skiResorts.entities.Resort;
import com.example.skiResorts.repository.ResortRepository;
import com.google.gson.*;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.lang.String.*;

@Service
public class ResortService {
    public static final int STATUS_OK = 200;
    public static final int RESORT_NOT_FOUND = 1;

    private final ResortRepository resortRepository;
    private final CounterService counterService;

    public ResortService(ResortRepository resortRepository, CounterService counterService){
        this.resortRepository = resortRepository;
        this.counterService = counterService;
    }

    public int addResort(Resort resort) {

        resort.setResortId(counterService.getNextId("resort"));
        resortRepository.save(resort);
        return  STATUS_OK;
    }

    public List<Resort> getAllResorts() {
        return resortRepository.findAll();
    }

    public int importSkiMaps() throws IOException {

        List<Resort> resorts = getAllResorts();
        List<String> apiNumbers = new ArrayList<>();
        for (Resort resort: resorts) {
            String apiNumber = resort.getApiResortNumber();
            apiNumbers.add(apiNumber);
        }
        URL url = new URL("https://skimap.org/SkiAreas/view/");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responsecode = conn.getResponseCode();

        if(responsecode != 200)
            throw new RuntimeException("HttpResponseCode: " +responsecode);
        else
        {
            for(String number : apiNumbers) {
                URL concreteUrl = new URL(url + number + ".json");
                Scanner sc = new Scanner(concreteUrl.openStream());
                String inline = "";
                while (sc.hasNext()) {
                    inline+=sc.nextLine();
                }
                System.out.println("\nJSON data in string format");
                System.out.println(inline);
                sc.close();
                JsonObject jobj = JsonParser.parseString(inline).getAsJsonObject();
                JsonArray skiMaps = (JsonArray)jobj.get("ski_maps");

                for(int i=0;i<skiMaps.size();i++) {
                    JsonObject objectInSkiMaps = (JsonObject)skiMaps.get(0);
                    JsonObject media = (JsonObject)objectInSkiMaps.get("media");
                    JsonArray sizes = (JsonArray)media.get("sizes");
                    JsonObject concreteSize = (JsonObject)sizes.get(0);
                    JsonElement imageUrl = concreteSize.get("url");
                    String imageUrlString = imageUrl.getAsString();
                    Optional<Resort> resortOpt = resortRepository.findResortByApiResortNumber(number);
                    Resort resort;
                    if (resortOpt.isPresent()){
                        resort = resortOpt.get();
                    } else {
                        return RESORT_NOT_FOUND;
                    }
                    resort.setSkiMap(imageUrlString);
                    resortRepository.save(resort);
                }
            }
        }
        return 0;
    }


//    public Resort getResort (int resortId) {
//        Optional<Resort> resortOpt = resortRepository.findById(resortId);
//        Resort resort;
//        if (resortOpt.isPresent()){
//            resort = resortOpt.get();
//        } else {
//            return RESORT_NOT_FOUND;
//        }
//        return resortRepository.findById(resortId);
//    }

}
