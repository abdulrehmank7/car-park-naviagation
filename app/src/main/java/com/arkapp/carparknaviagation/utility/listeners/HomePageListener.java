package com.arkapp.carparknaviagation.utility.listeners;

import androidx.lifecycle.MutableLiveData;

import com.arkapp.carparknaviagation.data.models.eta.Eta;
import com.arkapp.carparknaviagation.data.models.redLightCamera.Feature;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by Abdul Rehman on 27-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public interface HomePageListener {
    void setCurrentLocationMarker(MarkerOptions markerOptions);

    void setRouteCameras(List<Feature> redLightMarkers, List<Feature> routeSpeedCameras);

    void setCarParking();

    void showCarParkList();

    void setUraCarParkEtaFromOrigin(MutableLiveData<Eta> carParkEtaFromOrigin);

    void setUraCarParkEtaFromDestination(MutableLiveData<Eta> carParkEtaFromDestination);

    void setRoute(PolylineOptions polyLineOptions);

    void startNavigation();

    void setMyTransportCarParkEtaFromOrigin(MutableLiveData<Eta> carParkEtaFromOrigin);

    void setMyTransportCarParkEtaFromDestination(MutableLiveData<Eta> carParkEtaFromDestination);
}
