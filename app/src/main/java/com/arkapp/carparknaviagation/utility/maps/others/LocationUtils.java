package com.arkapp.carparknaviagation.utility.maps.others;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Looper;

import com.arkapp.carparknaviagation.data.repository.PrefRepository;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

/**
 * Created by Abdul Rehman on 23-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class LocationUtils {
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
                    PrefRepository prefRepository = new PrefRepository(context);
                    prefRepository.setCurrentLocation(new LatLng(location.getLatitude(), location.getLongitude()));
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
}
