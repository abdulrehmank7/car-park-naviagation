package com.arkapp.carparknaviagation.data.models.redLightCamera;

import androidx.annotation.Keep;

/**
 * Created by Abdul Rehman on 27-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This is the model class used for Red light camera data from Red light api.
 */

@Keep
public class Feature {

    private String type;

    private FeatureProperties properties;

    private Geometry geometry;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FeatureProperties getProperties() {
        return properties;
    }

    public void setProperties(FeatureProperties properties) {
        this.properties = properties;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
