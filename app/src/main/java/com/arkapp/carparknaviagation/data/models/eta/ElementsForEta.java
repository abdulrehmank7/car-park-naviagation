package com.arkapp.carparknaviagation.data.models.eta;

import androidx.annotation.Keep;

import java.io.Serializable;

/**
 * This is the model class used for ETA data which we get from google distance matrix api.
 */
@Keep
public class ElementsForEta implements Serializable {
    /**
     * Sample data
     *
     * "distance":{
     * "text":"48 m",
     * "value":48
     * },
     * "duration":{
     * "text":"1 min",
     * "value":25
     */
    private DurationForEta duration;
    private DurationForEta distance;
    private String status;

    public DurationForEta getDistance() {
        return distance;
    }

    public String getStatus() {
        return status;
    }

    public DurationForEta getDuration() {
        return duration;
    }
}
