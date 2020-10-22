package com.arkapp.carparknaviagation.data.models;

import androidx.annotation.Keep;


@Keep
public class VoicePackageDetails {

    public String localizedLanguage;

    public String marcCode;

    public float downloadSize;

    public String name;

    public String gender;

    public VoicePackageDetails(String localizedLanguage,
                               String marcCode,
                               float downloadSize,
                               String name,
                               String gender) {
        this.localizedLanguage = localizedLanguage;
        this.marcCode = marcCode;
        this.downloadSize = downloadSize;
        this.name = name;
        this.gender = gender;
    }

    public VoicePackageDetails() { }

    public String getLocalizedLanguage() {
        return localizedLanguage;
    }

    public void setLocalizedLanguage(String localizedLanguage) {
        this.localizedLanguage = localizedLanguage;
    }

    public String getMarcCode() {
        return marcCode;
    }

    public void setMarcCode(String marcCode) {
        this.marcCode = marcCode;
    }

    public float getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(float downloadSize) {
        this.downloadSize = downloadSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
