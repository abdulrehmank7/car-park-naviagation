
package com.arkapp.carparknaviagation.data.models.speedCamera;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class Properties {

    private String altitudeMode;

    private String description;
    @SerializedName("FMEL_UPD_D")
    private String fMELUPDD;
    @SerializedName("Field_1")
    private String field1;
    @SerializedName("INC_CRC")
    private String iNCCRC;
    @SerializedName("LATITUDE")
    private String lATITUDE;
    @SerializedName("LONGITUDE")
    private String lONGITUDE;

    private String name;
    @SerializedName("SHAPE")
    private String sHAPE;
    @SerializedName("X_ADDR")
    private String xADDR;
    @SerializedName("X_Coordinate")
    private String xCoordinate;
    @SerializedName("Y_ADDR")
    private String yADDR;
    @SerializedName("Y_Coordinate")
    private String yCoordinate;

    @SerializedName("Speed")
    private int speed;


    public String getAltitudeMode() {
        return altitudeMode;
    }

    public void setAltitudeMode(String altitudeMode) {
        this.altitudeMode = altitudeMode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFMELUPDD() {
        return fMELUPDD;
    }

    public void setFMELUPDD(String fMELUPDD) {
        this.fMELUPDD = fMELUPDD;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getINCCRC() {
        return iNCCRC;
    }

    public void setINCCRC(String iNCCRC) {
        this.iNCCRC = iNCCRC;
    }

    public String getLATITUDE() {
        return lATITUDE;
    }

    public void setLATITUDE(String lATITUDE) {
        this.lATITUDE = lATITUDE;
    }

    public String getLONGITUDE() {
        return lONGITUDE;
    }

    public void setLONGITUDE(String lONGITUDE) {
        this.lONGITUDE = lONGITUDE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSHAPE() {
        return sHAPE;
    }

    public void setSHAPE(String sHAPE) {
        this.sHAPE = sHAPE;
    }

    public String getXADDR() {
        return xADDR;
    }

    public void setXADDR(String xADDR) {
        this.xADDR = xADDR;
    }

    public String getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(String xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public String getYADDR() {
        return yADDR;
    }

    public void setYADDR(String yADDR) {
        this.yADDR = yADDR;
    }

    public String getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(String yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
