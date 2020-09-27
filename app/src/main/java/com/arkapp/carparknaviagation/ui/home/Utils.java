package com.arkapp.carparknaviagation.ui.home;

import android.content.Context;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import static com.arkapp.carparknaviagation.utility.maps.others.MapUtils.getCustomMaker;

/**
 * Created by Abdul Rehman on 27-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class Utils {

    public static void clearSearchTextListener(TextWatcher textWatcher, EditText editText) {
        editText.removeTextChangedListener(textWatcher);
    }

    public static void initSearchTextListener(TextWatcher textWatcher, EditText editText) {
        editText.addTextChangedListener(textWatcher);
    }

    public static Marker setLocationMarkerOnMap(double lat,
                                                double log,
                                                Marker marker,
                                                int iconDrawable,
                                                GoogleMap map,
                                                Context context) {
        //removing the old marker
        if (marker != null)
            marker.remove();

        Marker newMarker = map.addMarker(getCustomMaker(context, lat, log, iconDrawable));

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, log), 17));

        return newMarker;
    }
}
