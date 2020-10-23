package com.arkapp.carparknaviagation.utility;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Abdul Rehman on 19-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

//This file contains all the constant values used in app.
public class Constants {


    public static final String GOOGLE_KEY = "AIzaSyDR3RAZWLaeXBIRpzRIq8f9GOuQrauCeXU";

    public static final long SPLASH_TIME = 1000;
    public static final String CAR_PARK_API_KEY = "H0uaowJPSQ+T7/AuiCZnRA==";
    public static final String CAR_PARK_API_ACCESS_KEY = "a7e89bc3-ac45-4311-a61c-6804124e133b";

    public static final int API_TOKEN_REFRESH_HOUR = 4;

    public static final String CAR_PARK_API_1_BASE_URL = "http://datamall2.mytransport.sg/ltaodataservice/";
    public static final String CAR_PARK_API_2_BASE_URL = "https://www.ura.gov.sg/uraDataService/";
    public static final String GOOGLE_API_BASE_URL = "https://maps.googleapis.com/maps/api/";

    public static final String CHARGES_OUTSIDE_CENTRAL_AREA = "$1.20 per hour";
    public static final String CHARGES_WITHIN_CENTRAL_AREA = "$2.40 per hour\n(7:00am to 5:00pm, Monday to Saturday)\n\n1.20 per hour\n(Other hours)";

    public static final ArrayList<String> CENTRAL_AREA = new ArrayList<>(Arrays.asList(
            "Albert Centre",
            "Bras Basah Complex",
            "Tekka Centre",
            "Cheng Yan Court",
            "The Pinnacle Duxton",
            "Upper Cross Street",
            "Kreta Ayer Road",
            "Park Crescent",
            "Sago Lane",
            "Selegie Road",
            "Tanjong Pagar Plaza",
            "Upper Cross Street",
            "Waterloo Centre"));

    public final static String SPLASH_FRAGMENT = "SPLASH_FRAGMENT";
    public final static String HOME_FRAGMENT = "HOME_FRAGMENT";
    public final static String SETTING_FRAGMENT = "SETTING_FRAGMENT";


    public final static String SETTING_RED_LIGHT_CAMERA_KEY = "SETTING_RED_LIGHT_CAMERA_KEY";
    public final static String SETTING_SPEED_LIMIT_CAMERA_KEY = "SETTING_SPEED_LIMIT_CAMERA_KEY";
    public final static String SETTING_CAR_PARK_KEY = "SETTING_CAR_PARK_KEY";
    public final static String SETTING_LANGUAGE_KEY = "SETTING_LANGUAGE_KEY";
    public final static String SETTING_CARPARK_LOT_KEY = "SETTING_CARPARK_LOT_KEY";
    public final static String SETTING_SIMULATION_KEY = "SETTING_SIMULATION_KEY";
    public final static String SETTING_SIMULATION_SPEED_KEY = "SETTING_SIMULATION_SPEED_KEY";


    public final static int MINIMUM_CARPARK_COUNT = 1;
    public final static int MAXIMUM_CARPARK_COUNT = 50;
    public final static int DEFAULT_CARPARK_COUNT = 10;

    public final static int MINIMUM_SIMULATION_SPEED = 5;
    public final static int MAXIMUM_SIMULATION_SPEED = 100;
    public final static int DEFAULT_SIMULATION_SPEED = 20;

    public final static int MAXIMUM_DESTINATION_HISTORY_SIZE = 5;
}
