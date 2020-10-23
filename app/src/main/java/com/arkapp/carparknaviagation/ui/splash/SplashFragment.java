package com.arkapp.carparknaviagation.ui.splash;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.data.repository.PrefRepository;
import com.arkapp.carparknaviagation.databinding.FragmentSplashBinding;
import com.arkapp.carparknaviagation.ui.main.MainActivity;
import com.arkapp.carparknaviagation.utility.listeners.SplashListener;
import com.arkapp.carparknaviagation.utility.viewModelFactory.SplashViewModelFactory;
import com.arkapp.carparknaviagation.viewModels.SplashViewModel;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.Task;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import static com.arkapp.carparknaviagation.ui.main.MainActivity.gpsListener;
import static com.arkapp.carparknaviagation.utility.Constants.SPLASH_TIME;
import static com.arkapp.carparknaviagation.utility.maps.others.LocationUtils.getGPSSettingTask;
import static com.arkapp.carparknaviagation.utility.maps.others.LocationUtils.startLocationUpdates;
import static com.arkapp.carparknaviagation.utility.maps.others.MapUtils.REQUEST_CHECK_SETTINGS;

/**
 * This fragemt is used to show the splash screen UI when app is opened.
 */
public class SplashFragment extends Fragment implements SplashListener {

    private FragmentSplashBinding binding;
    public SplashViewModel viewModel;

    private ProgressDialog dialog;
    public AndroidXMapFragment mapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater);
        PrefRepository prefRepository = new PrefRepository(requireContext());
        SharedPreferences settingPref = PreferenceManager.getDefaultSharedPreferences(requireContext());

        //Initializing the splash viewmodel
        SplashViewModelFactory factory = new SplashViewModelFactory(prefRepository, settingPref);

        viewModel = new ViewModelProvider(this, factory).get(SplashViewModel.class);
        viewModel.listener = this;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        askRuntimePermission();

        //Overriding the gpsListener to get the updates
        gpsListener = this::checkGpsSetting;
        mapFragment = (AndroidXMapFragment) getChildFragmentManager().findFragmentById(R.id.mapfragment);

        //Checking if the language list is downloaded or not
        if (!viewModel.isFetchLanguagesAvailable()) {
            viewModel.fetchLanguages(mapFragment);
        }
    }

    private void loadSplash() {
        Runnable runnable = () -> ((MainActivity) requireActivity()).openHomeScreen();
        new Handler().postDelayed(runnable, SPLASH_TIME);
    }

    @Override
    public void showProgress() {
        dialog = new ProgressDialog(requireContext());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("Please Wait");
        dialog.setMessage("Downloading voice assistant data...");
        dialog.setIndeterminate(true);
        dialog.show();
    }

    @Override
    public void hideProgress() {
        if (dialog == null || !dialog.isShowing()) return;
        dialog.dismiss();
    }

    private void askRuntimePermission() {
        Dexter.withContext(requireContext())
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
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