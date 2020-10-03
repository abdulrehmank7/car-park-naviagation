package com.arkapp.carparknaviagation.data.models.eta;

import androidx.annotation.Keep;

import java.io.Serializable;

/**
 * @author Akbar on 26/11/16.
 */
@Keep
public class DurationForEta implements Serializable {
    /*    "text":"48 m",
    "value":48*/
    private String value;
    private String text;

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}
