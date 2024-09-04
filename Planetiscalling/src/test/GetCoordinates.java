package test;

import java.util.Map;

public class GetCoordinates {

    static String address = "Montreal canada";
    
    //https://nominatim.openstreetmap.org/reverse?format=json&lat=48.21946&lon=-102.331009

    public static void main(String[] args) {
        Map<String, Double> coords;
        coords = OpenStreetMapUtils.getInstance().getCoordinates(address);
        System.out.println("latitude :" + coords.get("lat"));
        System.out.println("longitude:" + coords.get("lon"));
    }
}