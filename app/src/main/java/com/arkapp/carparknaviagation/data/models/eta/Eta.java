package com.arkapp.carparknaviagation.data.models.eta;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is the model class used for ETA data which we get from google distance matrix api.
 */
@Keep
public class Eta implements Serializable {
    /**
     * Sample data
     *
     * "destination_addresses":[],
     * "origin_addresses":[],
     * ""rows":[
     * {
     * "elements":[]
     * }
     * ],,
     * "status":"OK"
     */
    private String status;
    private ArrayList<ElementsClassForEta> rows;

    public String getStatus() {
        return status;
    }

    public ArrayList<ElementsClassForEta> getRows() {
        return rows;
    }
}
