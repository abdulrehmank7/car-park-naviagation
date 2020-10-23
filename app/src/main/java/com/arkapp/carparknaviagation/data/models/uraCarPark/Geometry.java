package com.arkapp.carparknaviagation.data.models.uraCarPark;

import androidx.annotation.Keep;

/**
 * Created by Abdul Rehman on 02-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This is the model class used for Car park data which we get from Ura carpark API.
 */

@Keep
public class Geometry {
    private String coordinates;

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
