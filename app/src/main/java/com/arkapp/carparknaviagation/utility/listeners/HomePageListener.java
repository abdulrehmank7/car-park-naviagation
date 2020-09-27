package com.arkapp.carparknaviagation.utility.listeners;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Abdul Rehman on 27-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public interface HomePageListener {
    void setCurrentLocationMarker(MarkerOptions markerOptions);

    void setRedLightCamera(ArrayList<MarkerOptions> redLightMarkers);
}
