package com.arkapp.carparknaviagation.data.models;

import androidx.annotation.Keep;

import com.arkapp.carparknaviagation.data.models.eta.ElementsForEta;

/**
 * Created by Abdul Rehman on 27-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This is the model class used for Sorting carpark data in the sorting algo.
 */

@Keep
public class CustomCarPark {

    private String id;

    private double distanceFromDestination;

    private double distanceFromOrigin;

    private Integer availableLots;

    private ElementsForEta etaDistanceFromOrigin;

    private Integer sequencePriority;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getDistanceFromDestination() {
        return distanceFromDestination;
    }

    public void setDistanceFromDestination(double distanceFromDestination) {
        this.distanceFromDestination = distanceFromDestination;
    }

    public double getDistanceFromOrigin() {
        return distanceFromOrigin;
    }

    public void setDistanceFromOrigin(double distanceFromOrigin) {
        this.distanceFromOrigin = distanceFromOrigin;
    }

    public Integer getAvailableLots() {
        return availableLots;
    }

    public void setAvailableLots(Integer availableLots) {
        this.availableLots = availableLots;
    }

    public ElementsForEta getEtaDistanceFromOrigin() {
        return etaDistanceFromOrigin;
    }

    public void setEtaDistanceFromOrigin(
            ElementsForEta etaDistanceFromOrigin) {
        this.etaDistanceFromOrigin = etaDistanceFromOrigin;
    }

    public Integer getSequencePriority() {
        return sequencePriority;
    }

    public void setSequencePriority(Integer sequencePriority) {
        this.sequencePriority = sequencePriority;
    }
}
