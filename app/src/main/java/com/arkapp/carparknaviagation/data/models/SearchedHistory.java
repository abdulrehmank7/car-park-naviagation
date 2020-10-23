package com.arkapp.carparknaviagation.data.models;

import androidx.annotation.Keep;


/**
 * This is the model class used for History address data which we get from Google places API.
 */
@Keep
public class SearchedHistory {

    public String placeId;

    public String description;

    public String area;

    public SearchedHistory(String placeId, String description, String area) {
        this.placeId = placeId;
        this.description = description;
        this.area = area;
    }
}
