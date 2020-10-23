package com.arkapp.carparknaviagation.data.models.uraCarPark;

import androidx.annotation.Keep;

import java.util.List;

/**
 * Created by Abdul Rehman on 02-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This is the model class used for Car park data which we get from Ura carpark API.
 */

@Keep
public class UraCharges {
    private String weekdayMin;

    private String weekdayRate;

    private String ppCode;

    private String parkingSystem;

    private String ppName;

    private String vehCat;

    private String satdayMin;

    private String satdayRate;

    private String sunPHMin;

    private String sunPHRate;

    private String remarks;

    private List<Geometry> geometries = null;

    private String startTime;

    private Integer parkCapacity;

    private String endTime;

    public String getWeekdayMin() {
        return weekdayMin;
    }

    public void setWeekdayMin(String weekdayMin) {
        this.weekdayMin = weekdayMin;
    }

    public String getWeekdayRate() {
        return weekdayRate;
    }

    public void setWeekdayRate(String weekdayRate) {
        this.weekdayRate = weekdayRate;
    }

    public String getPpCode() {
        return ppCode;
    }

    public void setPpCode(String ppCode) {
        this.ppCode = ppCode;
    }

    public String getParkingSystem() {
        return parkingSystem;
    }

    public void setParkingSystem(String parkingSystem) {
        this.parkingSystem = parkingSystem;
    }

    public String getPpName() {
        return ppName;
    }

    public void setPpName(String ppName) {
        this.ppName = ppName;
    }

    public String getVehCat() {
        return vehCat;
    }

    public void setVehCat(String vehCat) {
        this.vehCat = vehCat;
    }

    public String getSatdayMin() {
        return satdayMin;
    }

    public void setSatdayMin(String satdayMin) {
        this.satdayMin = satdayMin;
    }

    public String getSatdayRate() {
        return satdayRate;
    }

    public void setSatdayRate(String satdayRate) {
        this.satdayRate = satdayRate;
    }

    public String getSunPHMin() {
        return sunPHMin;
    }

    public void setSunPHMin(String sunPHMin) {
        this.sunPHMin = sunPHMin;
    }

    public String getSunPHRate() {
        return sunPHRate;
    }

    public void setSunPHRate(String sunPHRate) {
        this.sunPHRate = sunPHRate;
    }

    public List<Geometry> getGeometries() {
        return geometries;
    }

    public void setGeometries(List<Geometry> geometries) {
        this.geometries = geometries;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getParkCapacity() {
        return parkCapacity;
    }

    public void setParkCapacity(Integer parkCapacity) {
        this.parkCapacity = parkCapacity;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
