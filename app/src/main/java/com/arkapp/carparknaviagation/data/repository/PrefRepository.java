package com.arkapp.carparknaviagation.data.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * This is the UTILITY class for using shared preferences easily.
 */

public class PrefRepository {
    private final String PREF_CURRENT_LOCATION = "PREF_CURRENT_LOCATION";
    private final String PREF_API_TOKEN = "PREF_API_TOKEN";
    private final String PREF_API_TOKEN_UPDATE_TIME = "PREF_API_TOKEN_UPDATE_TIME";
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

    public LatLng getCurrentLocation() {
        String data = pref.getString(PREF_CURRENT_LOCATION, "");
        if (!TextUtils.isEmpty(data)) {
            return gson.fromJson(data, LatLng.class);
        }
        return null;
    }

    public void setCurrentLocation(LatLng location) {
        editor.putString(PREF_CURRENT_LOCATION, gson.toJson(location));
        editor.commit();
    }

    public String getApiToken() {
        return pref.getString(PREF_API_TOKEN, "");
    }

    public void setApiToken(String token) {
        editor.putString(PREF_API_TOKEN, token);
        editor.commit();
        setApiTokenUpdateTime(new Date());
    }

    public Date getApiTokenUpdateTime() {
        String data = pref.getString(PREF_API_TOKEN_UPDATE_TIME, "");
        if (!TextUtils.isEmpty(data)) {
            return gson.fromJson(data, Date.class);
        }
        return null;
    }

    public void setApiTokenUpdateTime(Date date) {
        editor.putString(PREF_API_TOKEN_UPDATE_TIME, gson.toJson(date));
        editor.commit();
    }
}
