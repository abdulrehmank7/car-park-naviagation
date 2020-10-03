package com.arkapp.carparknaviagation.data.models.eta;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Akbar on 26/11/16.
 */
@Keep
public class Eta implements Serializable {
    /*    "destination_addresses":[],
    "origin_addresses":[],
    ""rows":[

    {
        "elements":[]
    }
    ],,
    "status":"OK"*/
    private String status;
    private ArrayList<ElementsClassForEta> rows;

    public String getStatus() {
        return status;
    }

    public ArrayList<ElementsClassForEta> getRows() {
        return rows;
    }
}
