package com.arkapp.carparknaviagation.data.models.redLightCamera;

import androidx.annotation.Keep;

/**
 * Created by Abdul Rehman on 27-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This is the model class used for Red light camera data from Red light api.
 */

@Keep
public class Properties {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
