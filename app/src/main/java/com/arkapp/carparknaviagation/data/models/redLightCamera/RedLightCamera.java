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
public class RedLightCamera {

    private String type;
    private String name;
    private Crs crs;
    private List<Feature> features = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Crs getCrs() {
        return crs;
    }

    public void setCrs(Crs crs) {
        this.crs = crs;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}
