package com.arkapp.carparknaviagation.data.models.uraCarPark;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abdul Rehman on 02-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This is the model class used for token for calling Ura carpark api which we get from Ura carpark token API.
 */

@Keep
public class Token {
    @SerializedName("Status")
    private String status;

    @SerializedName("Message")
    private String message;

    @SerializedName("Result")
    private String result;

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
