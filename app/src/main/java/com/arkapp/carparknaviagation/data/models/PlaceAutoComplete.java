package com.arkapp.carparknaviagation.data.models;

import androidx.annotation.Keep;

/**
 * This is the model class used for Auto complete address data which we get from Google places API.
 */

@Keep
public class PlaceAutoComplete {

    public CharSequence placeId;

    public CharSequence description;

    public CharSequence area;

    public PlaceAutoComplete(CharSequence placeId, CharSequence description, CharSequence area) {
        this.placeId = placeId;
        this.description = description;
        this.area = area;
    }

    @Override
    public String toString() {
        return description.toString();
    }
}
