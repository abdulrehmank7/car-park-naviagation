package com.arkapp.carparknaviagation.data.models.myTransportCarPark;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Abdul Rehman on 28-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This is the model class used for Car park data which we get from MyTransport API.
 */
@Keep
public class MyTransportCarPark {

    @SerializedName("odata.metadata")
    private String odataMetadata;

    @SerializedName("value")
    private List<MyTransportCarParkAvailability> myTransportCarParkAvailability = null;

    public String getOdataMetadata() {
        return odataMetadata;
    }

    public void setOdataMetadata(String odataMetadata) {
        this.odataMetadata = odataMetadata;
    }

    public List<MyTransportCarParkAvailability> getMyTransportCarParkAvailability() {
        return myTransportCarParkAvailability;
    }

    public void setMyTransportCarParkAvailability(
            List<MyTransportCarParkAvailability> myTransportCarParkAvailability) {
        this.myTransportCarParkAvailability = myTransportCarParkAvailability;
    }
}
