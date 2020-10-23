package com.arkapp.carparknaviagation.utility.maps.eta;

import com.arkapp.carparknaviagation.data.models.myTransportCarPark.MyTransportCarParkAvailability;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCarParkAvailability;

import java.util.ArrayList;

import static com.arkapp.carparknaviagation.utility.Constants.GOOGLE_KEY;

/**
 * Created by Abdul Rehman on 02-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

public class Utils {

    //This method will generate the url to get the Eta from two points.
    //This url will be used to call distance matrix api.
    public static String getEtaUrl(ArrayList<String> allCarParkLatLng, String fromLat,
                                   String fromLng) {
        return "https://maps.googleapis.com/maps/api/distancematrix/json?destinations=" +
                getDistanceMatrixLatLngObj(allCarParkLatLng) + "&" +
                "origins=" + fromLat + "," + fromLng
                + "&mode=driving" + "&" + "key=" + GOOGLE_KEY;
    }

    public static String getDistanceMatrixLatLngObj(ArrayList<String> allCarParkLatLng) {
        if (allCarParkLatLng == null) return "";
        StringBuilder request = new StringBuilder();
        for (String latLng : allCarParkLatLng) {
            request.append(latLng + "|");
        }

        if (request.toString().length() > 0)
            return request.substring(0, request.toString().length() - 1);

        return "";
    }

    //Getting the lat lang of car park to fetch their eta and distance.
    public static ArrayList<String> getUraCarParkLatLng(ArrayList<UraCarParkAvailability> validCarPark) {
        if(validCarPark == null) return new ArrayList<>();
        ArrayList<String> allCarParkLatLng = new ArrayList<>();

        for (UraCarParkAvailability uraCarParkAvailability : validCarPark) {
            allCarParkLatLng.add(uraCarParkAvailability.getLat() + "," + uraCarParkAvailability.getLng());
        }
        return allCarParkLatLng;
    }

    //Getting the lat lang of car park to fetch their eta and distance.
    public static ArrayList<String> getMyTransportCarParkLatLng(ArrayList<MyTransportCarParkAvailability> validCarPark) {
        if(validCarPark == null) return new ArrayList<>();

        ArrayList<String> allCarParkLatLng = new ArrayList<>();

        for (MyTransportCarParkAvailability uraCarParkAvailability : validCarPark) {
            allCarParkLatLng.add(uraCarParkAvailability.getCarParkLat() + "," + uraCarParkAvailability.getCarParkLng());
        }
        return allCarParkLatLng;
    }
}
