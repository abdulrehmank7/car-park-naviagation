package com.arkapp.carparknaviagation.utility;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Abdul Rehman on 19-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class Constants {


    public static final String GOOGLE_KEY = "AIzaSyDR3RAZWLaeXBIRpzRIq8f9GOuQrauCeXU";

    //    public static final String GOOGLE_KEY = "AIzaSyDwCCLaZyQuRFlbDR98m2imUkT3PrkIvp4";
    public static final long SPLASH_TIME = 1500;
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
}
