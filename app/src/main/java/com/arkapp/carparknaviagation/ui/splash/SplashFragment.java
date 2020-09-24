package com.arkapp.carparknaviagation.ui.splash;

import android.Manifest;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arkapp.carparknaviagation.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import static androidx.navigation.fragment.NavHostFragment.findNavController;
import static com.arkapp.carparknaviagation.ui.main.MainActivity.gpsListener;
import static com.arkapp.carparknaviagation.utility.Constants.SPLASH_TIME;
import static com.arkapp.carparknaviagation.utility.maps.LocationUtils.getGPSSettingTask;
import static com.arkapp.carparknaviagation.utility.maps.LocationUtils.startLocationUpdates;
import static com.arkapp.carparknaviagation.utility.maps.MapUtils.REQUEST_CHECK_SETTINGS;

public class SplashFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        askRuntimePermission();

        //Overriding the gpsListener to get the updates
        gpsListener = this::checkGpsSetting;
    }

    private void loadSplash() {
        Runnable runnable = () -> findNavController(this).navigate(R.id.action_splashFragment_to_homeFragment);
        new Handler().postDelayed(runnable, SPLASH_TIME);
    }

    private void askRuntimePermission() {
        Dexter.withContext(requireContext())
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(
                        new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(
                                    MultiplePermissionsReport multiplePermissionsReport) {
                                if (multiplePermissionsReport.areAllPermissionsGranted())
                                    checkGpsSetting();
                                else
                                    askRuntimePermission();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(
                                    List<PermissionRequest> list,
                                    PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();

                            }
                        })
                .check();
    }

    public void checkGpsSetting() {
        Task<LocationSettingsResponse> task = getGPSSettingTask(requireContext());
        task.addOnSuccessListener(requireActivity(), locationSettingsResponse -> {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            startLocationUpdates(requireContext());
            loadSplash();
        });

        task.addOnFailureListener(requireActivity(), e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }
}