package com.arkapp.carparknaviagation.ui.carParkList;

import android.text.TextUtils;

import com.arkapp.carparknaviagation.data.models.myTransportCarPark.MyTransportCarParkAvailability;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCharges;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Abdul Rehman on 01-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class Utils {

    public static String getUraChargeString(UraCharges uraCharges) {

        StringBuilder builder = new StringBuilder();
        if (uraCharges != null) {
            if (!TextUtils.isEmpty(uraCharges.getWeekdayRate())) {
                builder.append(String.format(Locale.ENGLISH, "Weekday Rate: $%s/Hr", doubleRate(uraCharges.getWeekdayRate())));
                builder.append("\n");
            }

            if (!TextUtils.isEmpty(uraCharges.getSatdayRate())) {
                builder.append(String.format(Locale.ENGLISH, "Saturday Rate: $%s/Hr", doubleRate(uraCharges.getSatdayRate())));
                builder.append("\n");
            }

            if (!TextUtils.isEmpty(uraCharges.getSunPHRate())) {
                builder.append(String.format(Locale.ENGLISH, "Sunday and Public Holiday Rate: $%s/Hr", doubleRate(uraCharges.getSunPHRate())));
                builder.append("\n");
            }

            if (!TextUtils.isEmpty(uraCharges.getRemarks())) {
                builder.append("Remarks: " + uraCharges.getRemarks());
                builder.append("\n");
            }
        }

        if (uraCharges == null)
            return "Rates Unavailable!";

        return builder.toString();
    }

    public static double getPerHourCharge(UraCharges uraCharges) {
        Calendar cal = Calendar.getInstance();
        boolean isSaturday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
        boolean isSunday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
        double charge = 0;
        if (isSaturday) {
            if (!TextUtils.isEmpty(uraCharges.getSatdayRate())) {
                charge = Double.parseDouble(
                        uraCharges.getSatdayRate()
                                .replace("$", "")
                                .replace(" ", "")
                                .trim());
            }
        } else if (isSunday) {
            if (!TextUtils.isEmpty(uraCharges.getSunPHRate())) {
                charge = Double.parseDouble(
                        uraCharges.getSunPHRate()
                                .replace("$", "")
                                .replace(" ", "")
                                .trim());
            }
        } else {
            if (!TextUtils.isEmpty(uraCharges.getWeekdayRate())) {
                charge = Double.parseDouble(
                        uraCharges.getWeekdayRate()
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

    public static String getMyTransportChargeString(MyTransportCarParkAvailability availability) {

        StringBuilder builder = new StringBuilder();
        if (availability.getCharges() != null) {
            if (!TextUtils.isEmpty(availability.getCharges().getWeekDaysRate1())) {
                builder.append(availability.getCharges().getWeekDaysRate1());
                builder.append("\n\n");
            }
        }

        if (availability.getInformation() != null) {
            if (!TextUtils.isEmpty(availability.getInformation().getCarParkType())) {
                builder.append(String.format(Locale.ENGLISH, "Car Park Type: %s", availability.getInformation().getCarParkType()));
                builder.append("\n\n");
            }

            if (!TextUtils.isEmpty(availability.getInformation().getTypeOfParkingSystem())) {
                builder.append(String.format(Locale.ENGLISH, "Parking System: %s", availability.getInformation().getTypeOfParkingSystem()));
                builder.append("\n\n");
            }

            if (availability.getInformation().getCarParkDecks() != null) {
                builder.append(String.format(Locale.ENGLISH, "Car Park Decks: %s", availability.getInformation().getCarParkDecks()));
                builder.append("\n\n");
            }

            if (!TextUtils.isEmpty(availability.getInformation().getNightParking())) {
                builder.append(String.format(Locale.ENGLISH, "Night Parking: %s", availability.getInformation().getNightParking()));
            }
        }

        if (TextUtils.isEmpty(builder.toString()))
            return "Rates Unavailable!";

        return builder.toString();
    }
}
