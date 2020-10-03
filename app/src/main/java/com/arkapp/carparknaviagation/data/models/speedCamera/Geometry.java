
package com.arkapp.carparknaviagation.data.models.speedCamera;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class Geometry {


    private List<Double> coordinates;

    private String type;

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
