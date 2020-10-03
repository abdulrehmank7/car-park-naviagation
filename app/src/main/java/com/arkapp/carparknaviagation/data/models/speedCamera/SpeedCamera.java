
package com.arkapp.carparknaviagation.data.models.speedCamera;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class SpeedCamera {

    private Crs crs;

    private List<Feature> features;

    private String name;

    private String type;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
