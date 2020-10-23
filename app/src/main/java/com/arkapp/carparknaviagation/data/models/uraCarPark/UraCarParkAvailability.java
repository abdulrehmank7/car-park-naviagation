package com.arkapp.carparknaviagation.data.models.uraCarPark;

import androidx.annotation.Keep;

import com.arkapp.carparknaviagation.data.models.eta.ElementsForEta;

import java.util.List;

/**
 * Created by Abdul Rehman on 02-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This is the model class used for Car park data which we get from Ura carpark API.
 */

@Keep
public class UraCarParkAvailability {

    private String carparkNo;

    private String lotsAvailable;

    private String lotType;

    private List<Geometry> geometries = null;

    private UraCharges charges;

    private double lat;

    private double lng;

    private double distanceFromDestination;

    private double distanceFromOrigin;

    private Integer sequencePriority;

    private ElementsForEta etaDistanceFromOrigin;

    private ElementsForEta etaDistanceFromDestination;

    public String getCarparkNo() {
        return carparkNo;
    }

    public void setCarparkNo(String carparkNo) {
        this.carparkNo = carparkNo;
    }

    public String getLotsAvailable() {
        return lotsAvailable;
    }

    public void setLotsAvailable(String lotsAvailable) {
        this.lotsAvailable = lotsAvailable;
    }

    public String getLotType() {
        return lotType;
    }

    public void setLotType(String lotType) {
        this.lotType = lotType;
    }

    public List<Geometry> getGeometries() {
        return geometries;
    }

    public void setGeometries(List<Geometry> geometries) {
        this.geometries = geometries;
    }

    public UraCharges getCharges() {
        return charges;
    }

    public void setCharges(UraCharges charges) {
        this.charges = charges;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
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

    public Integer getSequencePriority() {
        return sequencePriority;
    }

    public void setSequencePriority(Integer sequencePriority) {
        this.sequencePriority = sequencePriority;
    }

    public ElementsForEta getEtaDistanceFromOrigin() {
        return etaDistanceFromOrigin;
    }

    public void setEtaDistanceFromOrigin(
            ElementsForEta etaDistanceFromOrigin) {
        this.etaDistanceFromOrigin = etaDistanceFromOrigin;
    }

    public ElementsForEta getEtaDistanceFromDestination() {
        return etaDistanceFromDestination;
    }

    public void setEtaDistanceFromDestination(
            ElementsForEta etaDistanceFromDestination) {
        this.etaDistanceFromDestination = etaDistanceFromDestination;
    }
}
