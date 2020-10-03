package com.arkapp.carparknaviagation.data.models.carParking;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Abdul Rehman on 28-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
@Keep
public class CarParking {
    @SerializedName("odata.metadata")
    private String odataMetadata;
    private List<Value> value = null;

    public String getOdataMetadata() {
        return odataMetadata;
    }

    public void setOdataMetadata(String odataMetadata) {
        this.odataMetadata = odataMetadata;
    }

    public List<Value> getValue() {
        return value;
    }

    public void setValue(List<Value> value) {
        this.value = value;
    }
}
