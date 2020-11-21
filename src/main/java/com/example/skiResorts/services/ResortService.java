package com.example.skiResorts.services;

import com.example.skiResorts.entities.Counter;
import com.example.skiResorts.entities.Resort;
import com.example.skiResorts.repository.ResortRepository;
import com.google.gson.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
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

    public List<Resort> getAllResorts(double latitude, double longitude) {

        List<Resort> resorts = resortRepository.findAll();
        List<Resort> resortsWithDistance = new ArrayList<>();
        if(latitude==0 && longitude==0) {
            Collections.sort(resorts, Comparator.comparing(p -> -p.getNumberOfRatings()));
            return resorts;
        }

        for(Resort resort: resorts) {
            double resortLat = Double.parseDouble(resort.getLocation().getLatitude());
            double resortLong = Double.parseDouble(resort.getLocation().getLongitude());
            double longDiff = longitude - resortLong;
            double distance = Math.sin(degToRad(latitude)) * Math.sin(degToRad(resortLat)) + Math.cos(degToRad(latitude)) * Math.cos(degToRad(resortLat)) * Math.cos(degToRad(longDiff));
            distance = Math.acos(distance);
            distance = radToDeg(distance);
            distance = distance * 60 * 1.1515 * 1.609344;
            resort.setDistance(distance);
            resortsWithDistance.add(resort);
        }
        Collections.sort(resortsWithDistance, Comparator.comparing(p -> -p.getNumberOfRatings()));
        return resortsWithDistance;
    }

    private double degToRad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double radToDeg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public List<Resort> getAllResorts() {
        List<Resort> resorts = resortRepository.findAll();
        Collections.sort(resorts, Comparator.comparing(p -> -p.getNumberOfRatings()));
        return resorts;
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


    public Optional<Resort> getResortDetails (int resortId) {
        return resortRepository.findById(resortId);
    }

    public List<Pair<Resort, Integer>> preferredResorts(int blue, int red, int black, int snowPark, int location, double userLat, double userLon) {

        List<Resort> resorts = getAllResorts();
        
        List<Pair<Resort, Integer>> resortsWithRatings = new ArrayList<>();
        int blueR;
        int redR;
        int blackR;
        boolean snowParkR;
        for (Resort resort: resorts) {
            blueR = resort.getBlueSlopes();
            redR = resort.getRedSlopes();
            blackR = resort.getBlackSlopes();
            snowParkR = resort.isIfSnowPark();
            int points = 0;
            points += calculateSlopesPoints(blue, blueR);
            points += calculateSlopesPoints(red, redR);
            points += calculateSlopesPoints(black, blackR);
            points += calculateSnowPark(snowPark, snowParkR);
            points +=20; //location
            Pair<Resort, Integer> pair = Pair.of(resort, points);
            resortsWithRatings.add(pair);
        }
        Collections.sort(resortsWithRatings, Comparator.comparing(p -> -p.getSecond()));

        return resortsWithRatings;
    }

    private int calculateSlopesPoints (int preferredSlopes, int resortSlopes) {

        switch (preferredSlopes){
            case 1:
                return 20;
            case 2:
                if(resortSlopes>=1){
                    return 20;
                } else {
                    return 15;
                }
            case 3:
                if(resortSlopes>=3 ){
                    return 20;
                } else if(resortSlopes>=1) {
                    return 10;
                } else {
                    return 5;
                }
            case 4:
                if(resortSlopes>=5) {
                    return 20;
                } else if(resortSlopes>=3) {
                    return 10;
                } else if(resortSlopes>=1) {
                    return 5;
                } else {
                    return 0;
                }
            case 5:
                if(resortSlopes>=10) {
                    return 20;
                } else if(resortSlopes>=5) {
                    return 10;
                } else if(resortSlopes>=3) {
                    return 5;
                } else {
                    return 0;
                }
            default:
                return 0;
        }
    }

    private int calculateSnowPark (int preferredSnowPark, boolean ifSnowPark) {

        if(ifSnowPark) {
            return 20;
        }
        switch(preferredSnowPark) {
            case 1:
                return 20;
            case 2:
                return 15;
            case 3:
                return 10;
            case 4:
                return 5;
            default:
                return 0;
        }
    }

}
