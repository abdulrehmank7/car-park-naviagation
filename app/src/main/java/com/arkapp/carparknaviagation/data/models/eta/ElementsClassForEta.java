package com.arkapp.carparknaviagation.data.models.eta;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is the model class used for ETA data which we get from google distance matrix api.
 */
@Keep
public class ElementsClassForEta implements Serializable {
    /**
     * Sample data
     *
     * "elements":[
     * {
     * "distance":{
     * "text":"48 m",
     * "value":48
     * },
     * "duration":{
     * "text":"1 min",
     * "value":25
     * },
     * "status":"OK"
     * }
     * ]
     * }
     */
    private ArrayList<ElementsForEta> elements;

    public ArrayList<ElementsForEta> getElements() {
        return elements;
    }
}
