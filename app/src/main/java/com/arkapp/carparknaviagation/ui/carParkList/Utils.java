package com.arkapp.carparknaviagation.ui.carParkList;

import android.text.TextUtils;

import com.arkapp.carparknaviagation.data.models.carPark.CarPark;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Abdul Rehman on 01-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class Utils {

    public static String getChargeString(CarPark carPark) {

        StringBuilder builder = new StringBuilder();
        if (carPark != null) {
            if (!TextUtils.isEmpty(carPark.getWeekdayRate())) {
                builder.append(String.format(Locale.ENGLISH, "Weekday Rate: $%s/Hr", doubleRate(carPark.getWeekdayRate())));
                builder.append("\n");
            }

            if (!TextUtils.isEmpty(carPark.getSatdayRate())) {
                builder.append(String.format(Locale.ENGLISH, "Saturday Rate: $%s/Hr", doubleRate(carPark.getSatdayRate())));
                builder.append("\n");
            }

            if (!TextUtils.isEmpty(carPark.getSunPHRate())) {
                builder.append(String.format(Locale.ENGLISH, "Sunday and Public Holiday Rate: $%s/Hr", doubleRate(carPark.getSunPHRate())));
                builder.append("\n");
            }

            if (!TextUtils.isEmpty(carPark.getRemarks())) {
                builder.append("Remarks: " + carPark.getRemarks());
                builder.append("\n");
            }
        }

        if (carPark == null)
            return "Rates Unavailable!";

        return builder.toString();
    }

    public static double getPerHourCharge(CarPark carPark) {
        Calendar cal = Calendar.getInstance();
        boolean isSaturday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
        boolean isSunday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
        double charge = 0;
        if (isSaturday) {
            if (!TextUtils.isEmpty(carPark.getSatdayRate())) {
                charge = Double.parseDouble(
                        carPark.getSatdayRate()
                                .replace("$", "")
                                .replace(" ", "")
                                .trim());
            }
        } else if (isSunday) {
            if (!TextUtils.isEmpty(carPark.getSunPHRate())) {
                charge = Double.parseDouble(
                        carPark.getSunPHRate()
                                .replace("$", "")
                                .replace(" ", "")
                                .trim());
            }
        } else {
            if (!TextUtils.isEmpty(carPark.getWeekdayRate())) {
                charge = Double.parseDouble(
                        carPark.getWeekdayRate()
                                .replace("$", "")
                                .replace(" ", "")
                                .trim());
            }
        }

        return charge * 2;
    }

    public static double doubleRate(String rate) {
        return Double.parseDouble(
                rate
                        .replace("$", "")
                        .replace(" ", "")
                        .trim()) * 2;
    }
}
