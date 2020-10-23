package com.arkapp.carparknaviagation.data.models.eta;

import androidx.annotation.Keep;

import java.io.Serializable;

/**
 * This is the model class used for ETA data which we get from google distance matrix api.
 */
@Keep
public class DurationForEta implements Serializable {
    /**
     * Sample data
     *
     * "text":"48 m",
     * "value":48
     */
    private String value;
    private String text;

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}
