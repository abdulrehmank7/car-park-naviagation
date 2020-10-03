package com.arkapp.carparknaviagation.data.models.speedCamera;

import androidx.annotation.Keep;

@Keep
public class Crs {

    private Properties properties;

    private String type;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
