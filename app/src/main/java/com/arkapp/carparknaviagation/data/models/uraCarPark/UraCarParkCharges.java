package com.arkapp.carparknaviagation.data.models.uraCarPark;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Abdul Rehman on 02-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
@Keep
public class UraCarParkCharges {
    @SerializedName("Status")
    private String status;

    @SerializedName("Message")
    private String message;

    @SerializedName("Result")
    private List<UraCharges> result = null;

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

    public List<UraCharges> getResult() {
        return result;
    }

    public void setResult(List<UraCharges> result) {
        this.result = result;
    }
}
