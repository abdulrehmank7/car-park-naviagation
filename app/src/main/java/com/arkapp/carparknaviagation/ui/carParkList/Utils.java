package com.arkapp.carparknaviagation.ui.carParkList;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;

import androidx.annotation.Nullable;

import com.arkapp.carparknaviagation.data.models.myTransportCarPark.MyTransportCarParkAvailability;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCharges;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Abdul Rehman on 01-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class Utils {

    /**
     * This method will generate the custom String which will be shown
     * in the carpark list. This string will contain the carpark charge and
     * other details if available. This is used for URA carparks.
     */
    public static SpannableStringBuilder getUraChargeString(UraCharges uraCharges) {

        SpannableStringBuilder str = new SpannableStringBuilder("");

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);


        if (uraCharges != null) {

            switch (day) {
                case Calendar.SUNDAY:
                    // Current day is Sunday
                    if (!TextUtils.isEmpty(uraCharges.getSunPHRate())) {
                        str.append(String.format(Locale.ENGLISH, "Sunday & Public Holiday Rate: $%s/Hr", doubleRate(uraCharges.getSunPHRate())));
                        str.setSpan(new StyleSpan(Typeface.BOLD), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        str.append("\n");
                    }

                    if (!TextUtils.isEmpty(uraCharges.getWeekdayRate())) {
                        str.append(String.format(Locale.ENGLISH, "Weekday Rate: $%s/Hr", doubleRate(uraCharges.getWeekdayRate())));
                        str.append("\n");
                    }

                    if (!TextUtils.isEmpty(uraCharges.getSatdayRate())) {
                        str.append(String.format(Locale.ENGLISH, "Saturday Rate: $%s/Hr", doubleRate(uraCharges.getSatdayRate())));
                        str.append("\n");
                    }

                    break;
                case Calendar.SATURDAY:
                    // Current day is SATURDAY

                    if (!TextUtils.isEmpty(uraCharges.getSatdayRate())) {
                        str.append(String.format(Locale.ENGLISH, "Saturday Rate: $%s/Hr", doubleRate(uraCharges.getSatdayRate())));
                        str.setSpan(new StyleSpan(Typeface.BOLD), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        str.append("\n");
                    }

                    if (!TextUtils.isEmpty(uraCharges.getSunPHRate())) {
                        str.append(String.format(Locale.ENGLISH, "Sunday & Public Holiday Rate: $%s/Hr", doubleRate(uraCharges.getSunPHRate())));
                        str.append("\n");
                    }

                    if (!TextUtils.isEmpty(uraCharges.getWeekdayRate())) {
                        str.append(String.format(Locale.ENGLISH, "Weekday Rate: $%s/Hr", doubleRate(uraCharges.getWeekdayRate())));
                        str.append("\n");
                    }

                    break;
                default:
                    if (!TextUtils.isEmpty(uraCharges.getWeekdayRate())) {
                        str.append(String.format(Locale.ENGLISH, "Weekday Rate: $%s/Hr", doubleRate(uraCharges.getWeekdayRate())));
                        str.setSpan(new StyleSpan(Typeface.BOLD), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        str.append("\n");
                    }
                    if (!TextUtils.isEmpty(uraCharges.getSatdayRate())) {
                        str.append(String.format(Locale.ENGLISH, "Saturday Rate: $%s/Hr", doubleRate(uraCharges.getSatdayRate())));
                        str.append("\n");
                    }

                    if (!TextUtils.isEmpty(uraCharges.getSunPHRate())) {
                        str.append(String.format(Locale.ENGLISH, "Sunday & Public Holiday Rate: $%s/Hr", doubleRate(uraCharges.getSunPHRate())));
                        str.append("\n");
                    }

            }


            if (!TextUtils.isEmpty(uraCharges.getRemarks())) {
                str.append("Remarks: " + uraCharges.getRemarks());
                str.append("\n");
            }
        }

        if (uraCharges == null) {
            str.append("Rates Unavailable!");
            return str;
        }

        return str;
    }

    //This method will get the per hour charge for the Ura carparks
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

    //This method will double the carpark charge.
    public static double doubleRate(String rate) {
        if (TextUtils.isEmpty(rate)) return 0.0;
        return Double.parseDouble(
                rate
                        .replace("$", "")
                        .replace(" ", "")
                        .trim()) * 2;
    }

    /**
     * This method will generate the custom String which will be shown
     * in the carpark list. This string will contain the carpark charge and
     * other details if available. This is used for MyTransport carparks.
     */
    public static String getMyTransportChargeString(@Nullable MyTransportCarParkAvailability availability) {

        StringBuilder builder = new StringBuilder();
        if (availability.getCharges() != null) {
            if (!TextUtils.isEmpty(availability.getCharges().getWeekDaysRate1())) {
                builder.append(availability.getCharges().getWeekDaysRate1());
                builder.append("\n");
            }
        }

        if (availability.getInformation() != null) {
            if (!TextUtils.isEmpty(availability.getInformation().getCarParkType())) {
                builder.append(String.format(Locale.ENGLISH, "Car Park Type: %s", availability.getInformation().getCarParkType()));
                builder.append("\n");
            }

            if (!TextUtils.isEmpty(availability.getInformation().getTypeOfParkingSystem())) {
                builder.append(String.format(Locale.ENGLISH, "Parking System: %s", availability.getInformation().getTypeOfParkingSystem()));
                builder.append("\n");
            }

            if (!TextUtils.isEmpty(availability.getInformation().getFreeParking())) {
                builder.append(String.format(Locale.ENGLISH, "Free Parking: %s", availability.getInformation().getFreeParking()));
                builder.append("\n");
            }

            if (!TextUtils.isEmpty(availability.getInformation().getShortTermParking())) {
                builder.append(String.format(Locale.ENGLISH, "Short Term Parking: %s", availability.getInformation().getShortTermParking()));
            }
        }

        if (TextUtils.isEmpty(builder.toString()))
            return "Rates Unavailable!";

        return builder.toString();
    }
}
