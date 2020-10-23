package com.arkapp.carparknaviagation.data.models.rates;

/**
 * Created by Abdul Rehman on 01-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This is the model class used for Car park charges data.
 */

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class CarParkCharges {
    @SerializedName("CarPark")
    private String carPark;

    @SerializedName("Category")
    private String category;

    @SerializedName("WeekDays_Rate_1")
    private String weekDaysRate1;

    @SerializedName("WeekDays_Rate_2")
    private String weekDaysRate2;

    @SerializedName("Saturday_Rate")
    private String saturdayRate;

    @SerializedName("Sunday_PublicHoliday_Rate")
    private String sundayPublicHolidayRate;

    public String getCarPark() {
        return carPark;
    }

    public void setCarPark(String carPark) {
        this.carPark = carPark;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWeekDaysRate1() {
        return weekDaysRate1;
    }

    public void setWeekDaysRate1(String weekDaysRate1) {
        this.weekDaysRate1 = weekDaysRate1;
    }

    public String getWeekDaysRate2() {
        return weekDaysRate2;
    }

    public void setWeekDaysRate2(String weekDaysRate2) {
        this.weekDaysRate2 = weekDaysRate2;
    }

    public String getSaturdayRate() {
        return saturdayRate;
    }

    public void setSaturdayRate(String saturdayRate) {
        this.saturdayRate = saturdayRate;
    }

    public String getSundayPublicHolidayRate() {
        return sundayPublicHolidayRate;
    }

    public void setSundayPublicHolidayRate(String sundayPublicHolidayRate) {
        this.sundayPublicHolidayRate = sundayPublicHolidayRate;
    }
}
