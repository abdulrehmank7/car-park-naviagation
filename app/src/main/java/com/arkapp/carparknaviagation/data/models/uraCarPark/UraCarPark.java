package com.arkapp.carparknaviagation.data.models.uraCarPark;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdul Rehman on 02-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This is the model class used for Car park data which we get from Ura carpark API.
 */

@Keep
public class UraCarPark {
    @SerializedName("Status")
    private String status;

    @SerializedName("Message")
    private String message;

    @SerializedName("Result")
    private List<UraCarParkAvailability> result = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UraCarParkAvailability> getResult() {
        if (result == null)
            return new ArrayList();
        return result;
    }

    public void setResult(List<UraCarParkAvailability> result) {
        this.result = result;
    }
}
