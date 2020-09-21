package com.arkapp.carparknaviagation.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.VectorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;

import androidx.core.content.ContextCompat;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.ui.main.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.mapping.MapMarker;

import java.util.List;
import java.util.Locale;

/**
 * Created by Abdul Rehman on 21-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class MapUtils {

    public static final int REQUEST_CHECK_SETTINGS = 554;

    public static void setCustomCurrentMaker(Context context, MapMarker marker) {

        //used to set the custom marker image on current location marker
        try {
            Image currentMarkerImage = new Image();
            Bitmap icon = getBitmap((VectorDrawable) ContextCompat.getDrawable(context, R.drawable.ic_current_marker));
            currentMarkerImage.setBitmap(icon);
            marker.setAnchorPoint(new PointF(currentMarkerImage.getWidth() / 2, currentMarkerImage.getHeight()));

            marker.setIcon(currentMarkerImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        //get the bitmap image from the vector drawable file.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                                            vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public static LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    public static Task<LocationSettingsResponse> getGPSSettingTask(Context context) {
        LocationRequest locationRequest = createLocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(context);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        return task;
    }

    @SuppressLint("MissingPermission")
    public static void startLocationUpdates(Context context) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    MainActivity.currentLocation = location;
                    stopLocationUpdates(fusedLocationClient, this);
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(
                createLocationRequest(),
                locationCallback,
                Looper.getMainLooper());
    }

    public static void stopLocationUpdates(
            FusedLocationProviderClient fusedLocationClient,
            LocationCallback locationCallback) {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    public static String getLocationAddress(Context context, double lat, double log) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());

            addresses = geocoder.getFromLocation(lat, log, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            return address;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
