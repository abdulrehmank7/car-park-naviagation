package com.arkapp.carparknaviagation.data.models.redLightCamera;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abdul Rehman on 27-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
@Keep
public class FeatureProperties {


    @SerializedName("Name")
    private String name;
    private String description;
    private String altitudeMode;
    @SerializedName("X_ADDR")
    private String xADDR;
    @SerializedName("Y_ADDR")
    private String yADDR;
    @SerializedName("FMEL_UPD_D")
    private String fMELUPDD;
    @SerializedName("LONGITUDE")
    private String lONGITUDE;
    @SerializedName("LATITUDE")
    private String lATITUDE;
    @SerializedName("SHAPE")
    private String sHAPE;
    @SerializedName("ADDRESSSTREETNAME")
    private String aDDRESSSTREETNAME;
    @SerializedName("INC_CRC")
    private String iNCCRC;
    @SerializedName("Field_1")
    private String field1;
    @SerializedName("ID")
    private String iD;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAltitudeMode() {
        return altitudeMode;
    }

    public void setAltitudeMode(String altitudeMode) {
        this.altitudeMode = altitudeMode;
    }

    public String getXADDR() {
        return xADDR;
    }

    public void setXADDR(String xADDR) {
        this.xADDR = xADDR;
    }

    public String getYADDR() {
        return yADDR;
    }

    public void setYADDR(String yADDR) {
        this.yADDR = yADDR;
    }

    public String getFMELUPDD() {
        return fMELUPDD;
    }

    public void setFMELUPDD(String fMELUPDD) {
        this.fMELUPDD = fMELUPDD;
    }

    public String getLONGITUDE() {
        return lONGITUDE;
    }

    public void setLONGITUDE(String lONGITUDE) {
        this.lONGITUDE = lONGITUDE;
    }

    public String getLATITUDE() {
        return lATITUDE;
    }

    public void setLATITUDE(String lATITUDE) {
        this.lATITUDE = lATITUDE;
    }

    public String getSHAPE() {
        return sHAPE;
    }

    public void setSHAPE(String sHAPE) {
        this.sHAPE = sHAPE;
    }

    public String getADDRESSSTREETNAME() {
        return aDDRESSSTREETNAME;
    }

    public void setADDRESSSTREETNAME(String aDDRESSSTREETNAME) {
        this.aDDRESSSTREETNAME = aDDRESSSTREETNAME;
    }

    public String getINCCRC() {
        return iNCCRC;
    }

    public void setINCCRC(String iNCCRC) {
        this.iNCCRC = iNCCRC;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getID() {
        return iD;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

}
