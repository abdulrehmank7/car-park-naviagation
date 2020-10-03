package com.arkapp.carparknaviagation.data.models.carParking;

import androidx.annotation.Keep;

import com.arkapp.carparknaviagation.data.models.rates.CarParkCharges;
import com.arkapp.carparknaviagation.data.models.rates.CarParkInformation;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abdul Rehman on 28-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
@Keep
public class Value {

    @SerializedName("CarParkID")
    private String carParkID;

    @SerializedName("Area")
    private String area;

    @SerializedName("Development")
    private String development;

    @SerializedName("Location")
    private String location;

    @SerializedName("AvailableLots")
    private Integer availableLots;

    @SerializedName("LotType")
    private String lotType;

    @SerializedName("Agency")
    private String agency;

    private double carParkLat;

    private double carParkLng;

    private double distanceFromDestination;

    private double distanceFromOrigin;

    private Integer sequencePriority;

    private CarParkInformation information;

    private CarParkCharges charges;

    public String getCarParkID() {
        return carParkID;
    }

    public void setCarParkID(String carParkID) {
        this.carParkID = carParkID;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDevelopment() {
        return development;
    }

    public void setDevelopment(String development) {
        this.development = development;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getAvailableLots() {
        return availableLots;
    }

    public void setAvailableLots(Integer availableLots) {
        this.availableLots = availableLots;
    }

    public String getLotType() {
        return lotType;
    }

    public void setLotType(String lotType) {
        this.lotType = lotType;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public double getCarParkLat() {
        return carParkLat;
    }

    public void setCarParkLat(double carParkLat) {
        this.carParkLat = carParkLat;
    }

    public double getCarParkLng() {
        return carParkLng;
    }

    public void setCarParkLng(double carParkLng) {
        this.carParkLng = carParkLng;
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

    public CarParkInformation getInformation() {
        return information;
    }

    public void setInformation(
            CarParkInformation information) {
        this.information = information;
    }

    public CarParkCharges getCharges() {
        return charges;
    }

    public void setCharges(CarParkCharges charges) {
        this.charges = charges;
    }
}
