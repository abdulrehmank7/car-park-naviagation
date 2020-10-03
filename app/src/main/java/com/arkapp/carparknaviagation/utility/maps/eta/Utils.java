package com.arkapp.carparknaviagation.utility.maps.eta;

import com.arkapp.carparknaviagation.data.models.carPark.CarParkAvailability;

import java.util.ArrayList;

import static com.arkapp.carparknaviagation.utility.Constants.GOOGLE_KEY;

/**
 * Created by Abdul Rehman on 02-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

public class Utils {

    public static String getEtaUrl(ArrayList<String> allCarParkLatLng, String fromLat,
                                   String fromLng) {
        return "https://maps.googleapis.com/maps/api/distancematrix/json?destinations=" +
                getDistanceMatrixLatLngObj(allCarParkLatLng) + "&" +
                "origins=" + fromLat + "," + fromLng
                + "&mode=driving" + "&" + "key=" + GOOGLE_KEY;
    }

    public static String getDistanceMatrixLatLngObj(ArrayList<String> allCarParkLatLng) {
        StringBuilder request = new StringBuilder();
        for (String latLng : allCarParkLatLng) {
            request.append(latLng + "|");
        }

        if (request.toString().length() > 0)
            return request.substring(0, request.toString().length() - 1);

        return "";
    }

    public static ArrayList<String> getCarParkLatLng(ArrayList<CarParkAvailability> validCarPark) {
        ArrayList<String> allCarParkLatLng = new ArrayList<>();

        for (CarParkAvailability carParkAvailability : validCarPark) {
            allCarParkLatLng.add(carParkAvailability.getLat() + "," + carParkAvailability.getLng());
        }
        return allCarParkLatLng;
    }
}
