package com.arkapp.carparknaviagation.data.models.redLightCamera;

import androidx.annotation.Keep;

import java.util.List;

/**
 * Created by Abdul Rehman on 27-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This is the model class used for Red light camera data from Red light api.
 */

@Keep
public class Geometry {

    private String type;
    private List<Float> coordinates = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Float> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Float> coordinates) {
        this.coordinates = coordinates;
    }

}
