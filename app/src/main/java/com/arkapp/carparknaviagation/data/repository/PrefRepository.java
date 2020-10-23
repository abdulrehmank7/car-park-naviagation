package com.arkapp.carparknaviagation.data.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.arkapp.carparknaviagation.data.models.SearchedHistory;
import com.arkapp.carparknaviagation.data.models.VoicePackageDetails;
import com.arkapp.carparknaviagation.data.models.redLightCamera.Feature;
import com.arkapp.carparknaviagation.data.models.speedCamera.SpeedFeature;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.arkapp.carparknaviagation.utility.Constants.MAXIMUM_DESTINATION_HISTORY_SIZE;

/**
 * This is the UTILITY class for using shared preferences easily.
 */

public class PrefRepository {

    // All the constant name of shared preferences
    private final String PREF_CURRENT_LOCATION = "PREF_CURRENT_LOCATION";
    private final String PREF_API_TOKEN = "PREF_API_TOKEN";
    private final String PREF_API_TOKEN_UPDATE_TIME = "PREF_API_TOKEN_UPDATE_TIME";
    private final String PREF_NAVIGATION_START_LAT = "PREF_NAVIGATION_START_LAT";
    private final String PREF_NAVIGATION_START_LNG = "PREF_NAVIGATION_START_LNG";
    private final String PREF_NAVIGATION_END_LAT = "PREF_NAVIGATION_END_LAT";
    private final String PREF_NAVIGATION_END_LNG = "PREF_NAVIGATION_END_LNG";
    private final String PREF_CURRENT_ROUTE_RED_LIGHT_CAMERA = "PREF_CURRENT_ROUTE_RED_LIGHT_CAMERA";
    private final String PREF_CURRENT_ROUTE_SPEED_CAMERA = "PREF_CURRENT_ROUTE_SPEED_CAMERA";
    private final String PREF_DESTINATION_HISTORY = "PREF_DESTINATION_HISTORY";
    private final String PREF_VOICE_PACKAGES = "PREF_VOICE_PACKAGES";


    private SharedPreferences pref;
    private Editor editor;
    private Gson gson;

    @SuppressLint("CommitPrefEdits")
    public PrefRepository(@NotNull Context context) {
        this.pref = context.getSharedPreferences("NAVIGATION_MAIN_PREF", 0);
        this.editor = pref.edit();
        this.gson = new Gson();
    }

    public void clearData() {
        editor.clear();
        editor.commit();
    }

    //Retrieving current location latitude and longitude from shared preferences.
    public LatLng getCurrentLocation() {
        String data = pref.getString(PREF_CURRENT_LOCATION, "");
        if (!TextUtils.isEmpty(data)) {
            return gson.fromJson(data, LatLng.class);
        }
        return null;
    }

    //Storing current location latitude and longitude in shared preferences.
    public void setCurrentLocation(LatLng location) {
        editor.putString(PREF_CURRENT_LOCATION, gson.toJson(location));
        editor.commit();
    }

    //Retrieving URA token to call URA apis.
    public String getApiToken() {
        return pref.getString(PREF_API_TOKEN, "");
    }

    //Storing URA token to use later.
    public void setApiToken(String token) {
        editor.putString(PREF_API_TOKEN, token);
        editor.commit();
        setApiTokenUpdateTime(new Date());
    }

    //Retrieving URA token refresh time from shared preferences. URA Token will work
    // only 6 hours after that we need to refresh the token.
    public Date getApiTokenUpdateTime() {
        String data = pref.getString(PREF_API_TOKEN_UPDATE_TIME, "");
        if (!TextUtils.isEmpty(data)) {
            return gson.fromJson(data, Date.class);
        }
        return null;
    }

    //Storing URA token refresh time when we fetch URA token.
    public void setApiTokenUpdateTime(Date date) {
        editor.putString(PREF_API_TOKEN_UPDATE_TIME, gson.toJson(date));
        editor.commit();
    }

    //Retrieving origin location latitude from shared preferences.
    //For starting navigation.
    public float getNavigationStartLat() {
        return pref.getFloat(PREF_NAVIGATION_START_LAT, 0f);
    }

    //Storing origin location latitude.
    public void setNavigationStartLat(float value) {
        editor.putFloat(PREF_NAVIGATION_START_LAT, value);
        editor.commit();
    }

    //Retrieving origin location longitude from shared preferences.
    //For starting navigation.
    public float getNavigationStartLng() {
        return pref.getFloat(PREF_NAVIGATION_START_LNG, 0f);
    }

    //Storing origin location longitude.
    public void setNavigationStartLng(float value) {
        editor.putFloat(PREF_NAVIGATION_START_LNG, value);
        editor.commit();
    }

    //Retrieving destination location latitude from shared preferences.
    //For starting navigation.
    public float getNavigationEndLat() {
        return pref.getFloat(PREF_NAVIGATION_END_LAT, 0f);
    }

    //Storing destination location latitude.
    public void setNavigationEndLat(float value) {
        editor.putFloat(PREF_NAVIGATION_END_LAT, value);
        editor.commit();
    }

    //Retrieving destination location longitude from shared preferences.
    //For starting navigation.
    public float getNavigationEndLng() {
        return pref.getFloat(PREF_NAVIGATION_END_LNG, 0f);
    }

    //Storing destination location longitude.
    public void setNavigationEndLng(float value) {
        editor.putFloat(PREF_NAVIGATION_END_LNG, value);
        editor.commit();
    }

    //Retrieving route red light camera details to show in the navigation screen.
    public List<Feature> getCurrentRouteRedLightCamera() {

        String data = pref.getString(PREF_CURRENT_ROUTE_RED_LIGHT_CAMERA, "");

        if (!TextUtils.isEmpty(data)) {
            Type token = new TypeToken<List<Feature>>() {
            }.getType();
            return gson.fromJson(data, token);
        }
        return null;
    }

    //Storing route red light camera details to show in the navigation screen.
    public void setCurrentRouteRedLightCamera(List<Feature> redLightMarkers) {
        editor.putString(PREF_CURRENT_ROUTE_RED_LIGHT_CAMERA, gson.toJson(redLightMarkers));
        editor.commit();
    }

    //Retrieving route speed camera details to show in the navigation screen.
    public List<SpeedFeature> getCurrentRouteSpeedCamera() {
        String data = pref.getString(PREF_CURRENT_ROUTE_SPEED_CAMERA, "");

        if (!TextUtils.isEmpty(data)) {
            Type token = new TypeToken<List<SpeedFeature>>() {
            }.getType();
            return gson.fromJson(data, token);
        }
        return null;
    }

    //Storing route speed camera details to show in the navigation screen.
    public void setCurrentRouteSpeedCamera(List<SpeedFeature> speedCamera) {
        editor.putString(PREF_CURRENT_ROUTE_SPEED_CAMERA, gson.toJson(speedCamera));
        editor.commit();
    }

    //Retrieving destination history list. This is the address
    //which used has searched in past.
    public ArrayList<SearchedHistory> getDestinationHistory() {
        String data = pref.getString(PREF_DESTINATION_HISTORY, "");

        if (!TextUtils.isEmpty(data)) {
            Type token = new TypeToken<ArrayList<SearchedHistory>>() {
            }.getType();
            return gson.fromJson(data, token);
        }
        return new ArrayList<>();
    }

    //Storing destination history list. This is the address
    //will be saved when user search the address in the home screen.
    public void setDestinationHistory(SearchedHistory destination) {
        ArrayList<SearchedHistory> currentHistory = getDestinationHistory();

        int existingIndex = -1;
        for (int x = 0; x < currentHistory.size(); x++) {
            if (currentHistory.get(x).placeId.equals(destination.placeId))
                existingIndex = x;
        }

        if (existingIndex != -1) currentHistory.remove(existingIndex);

        if (currentHistory.size() == MAXIMUM_DESTINATION_HISTORY_SIZE) {
            currentHistory.remove(MAXIMUM_DESTINATION_HISTORY_SIZE - 1);
        }
        currentHistory.add(0, destination);
        editor.putString(PREF_DESTINATION_HISTORY, gson.toJson(currentHistory));
        editor.commit();
    }

    //Retrieving voice packages list of the device.
    //This will be used to show the list in the setting screen.
    public List<VoicePackageDetails> getVoicePackages() {
        String data = pref.getString(PREF_VOICE_PACKAGES, "");

        if (!TextUtils.isEmpty(data)) {
            Type token = new TypeToken<List<VoicePackageDetails>>() {
            }.getType();
            return gson.fromJson(data, token);
        }
        return null;
    }
    //Storing voice packages list of the device.
    //This will be stored when the app is opened for the first time.
    public void setVoicePackages(List<VoicePackageDetails> voicePackageDetails) {
        editor.putString(PREF_VOICE_PACKAGES, gson.toJson(voicePackageDetails));
        editor.commit();
    }

}
