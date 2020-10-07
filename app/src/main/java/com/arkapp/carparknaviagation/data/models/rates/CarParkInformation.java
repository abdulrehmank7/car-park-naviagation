package com.arkapp.carparknaviagation.data.models.rates;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abdul Rehman on 01-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
@Keep
public class CarParkInformation {

    @SerializedName("car_park_no")
    private String carParkNo;

    @SerializedName("address")
    private String address;

    @SerializedName("x_coord")
    private Float xCoord;

    @SerializedName("y_coord")
    private Float yCoord;

    @SerializedName("car_park_type")
    private String carParkType;

    @SerializedName("type_of_parking_system")
    private String typeOfParkingSystem;

    @SerializedName("short_term_parking")
    private String shortTermParking;

    @SerializedName("free_parking")
    private String freeParking;

    @SerializedName("night_parking")
    private String nightParking;

    @SerializedName("car_park_decks")
    private Integer carParkDecks;

    @SerializedName("gantry_height")
    private Double gantryHeight;

    @SerializedName("car_park_basement")
    private String carParkBasement;

    public String getCarParkNo() {
        return carParkNo;
    }

    public void setCarParkNo(String carParkNo) {
        this.carParkNo = carParkNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getXCoord() {
        return xCoord;
    }

    public void setXCoord(Float xCoord) {
        this.xCoord = xCoord;
    }

    public Float getYCoord() {
        return yCoord;
    }

    public void setYCoord(Float yCoord) {
        this.yCoord = yCoord;
    }

    public String getCarParkType() {
        return carParkType;
    }

    public void setCarParkType(String carParkType) {
        this.carParkType = carParkType;
    }

    public String getTypeOfParkingSystem() {
        return typeOfParkingSystem;
    }

    public void setTypeOfParkingSystem(String typeOfParkingSystem) {
        this.typeOfParkingSystem = typeOfParkingSystem;
    }

    public String getShortTermParking() {
        return shortTermParking;
    }

    public void setShortTermParking(String shortTermParking) {
        this.shortTermParking = shortTermParking;
    }

    public String getFreeParking() {
        return freeParking;
    }

    public void setFreeParking(String freeParking) {
        this.freeParking = freeParking;
    }

    public String getNightParking() {
        return nightParking;
    }

    public void setNightParking(String nightParking) {
        this.nightParking = nightParking;
    }

    public Integer getCarParkDecks() {
        return carParkDecks;
    }

    public void setCarParkDecks(Integer carParkDecks) {
        this.carParkDecks = carParkDecks;
    }

    public Double getGantryHeight() {
        return gantryHeight;
    }

    public void setGantryHeight(Double gantryHeight) {
        this.gantryHeight = gantryHeight;
    }

    public String getCarParkBasement() {
        return carParkBasement;
    }

    public void setCarParkBasement(String carParkBasement) {
        this.carParkBasement = carParkBasement;
    }
}
