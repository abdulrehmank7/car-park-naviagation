package com.arkapp.carparknaviagation.data.models;

import androidx.annotation.Keep;


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
