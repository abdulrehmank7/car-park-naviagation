package com.arkapp.carparknaviagation.ui.home;

import android.Manifest;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.databinding.FragmentHomeBinding;
import com.arkapp.carparknaviagation.ui.main.MainActivity;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.arkapp.carparknaviagation.ui.main.MainActivity.gpsListener;
import static com.arkapp.carparknaviagation.utility.Constants.GOOGLE_KEY;
import static com.arkapp.carparknaviagation.utility.MapUtils.REQUEST_CHECK_SETTINGS;
import static com.arkapp.carparknaviagation.utility.MapUtils.getGPSSettingTask;
import static com.arkapp.carparknaviagation.utility.MapUtils.getLocationAddress;
import static com.arkapp.carparknaviagation.utility.MapUtils.setCustomCurrentMaker;
import static com.arkapp.carparknaviagation.utility.MapUtils.startLocationUpdates;
import static com.arkapp.carparknaviagation.utility.ViewUtils.hide;
import static com.arkapp.carparknaviagation.utility.ViewUtils.show;
import static com.arkapp.carparknaviagation.utility.ViewUtils.showSnack;
import static com.arkapp.carparknaviagation.utility.ViewUtils.toast;

public class HomeFragment extends Fragment {

    private Map map = null;
    private MapView mapView = null;
    private Place selectedAddress;
    private FragmentHomeBinding binding;
    private AutocompleteSupportFragment autocompleteFragment;
    private MapMarker currentLocationMarker;
    private String selectedAddressName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.mapView);

        show(binding.progressBar);

        //Overriding the gpsListener to get the updates
        gpsListener = this::checkGpsSetting;

        initHereMaps();
        initAutoComplete();
        initClickListeners();
    }

    private void initClickListeners() {
        binding.cvSearchBar.setOnClickListener(view1 -> {
            final View root = autocompleteFragment.getView();
            root.post(() -> root.findViewById(R.id.places_autocomplete_search_input).performClick());
        });

        binding.cvMyLocation.setOnClickListener(view12 -> {
            showSnack(binding.parent, "Fetching Current Location...");
            askRuntimePermission();
        });
        binding.cvSetting.setOnClickListener(view12 -> showSnack(binding.parent, "In development..."));
        binding.cvHistory.setOnClickListener(view12 -> showSnack(binding.parent, "In development..."));
    }

    private void initHereMaps() {
        ApplicationContext applicationContext = new ApplicationContext(requireActivity().getApplicationContext());
        MapEngine.getInstance().init(
                applicationContext,
                error -> {
                    hide(binding.progressBar);
                    if (error == OnEngineInitListener.Error.NONE) {
                        if (map == null) {
                            map = new Map();
                            currentLocationMarker = new MapMarker();

                            map.setMapScheme(Map.Scheme.NORMAL_TRAFFIC_DAY);
                            map.setExtrudedBuildingsVisible(false);
                            map.setLandmarksVisible(false);
                        }
                        mapView.setMap(map);

                        selectedAddressName = getLocationAddress(
                                requireContext(),
                                MainActivity.currentLocation.getLatitude(),
                                MainActivity.currentLocation.getLongitude());
                        binding.tvSearchBar.setText(selectedAddressName);
                        setMarkerOnMap(
                                MainActivity.currentLocation.getLatitude(),
                                MainActivity.currentLocation.getLongitude());

                    } else {
                        Log.e("ERROR", error.getStackTrace());
                    }
                });
    }

    private void setMarkerOnMap(double lat, double log) {
        GeoCoordinate location = new GeoCoordinate(lat, log);
        map.setCenter(location, Map.Animation.NONE);

        //removing the old marker
        map.removeMapObject(currentLocationMarker);

        setCustomCurrentMaker(requireContext(), currentLocationMarker);
        currentLocationMarker.setCoordinate(location);

        map.addMapObject(currentLocationMarker);

        // Set the zoom level to the average between min and max
        map.setZoomLevel(16);
    }

    private void initAutoComplete() {
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), GOOGLE_KEY);
        }

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocompleteFragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS,
                                                          Place.Field.ID,
                                                          Place.Field.LAT_LNG));
        autocompleteFragment.setCountry("sgp");
        autocompleteFragment.setHint("");
        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_button);

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                // Get info about the selected place.
                selectedAddress = place;
                setMarkerOnMap(place.getLatLng().latitude, place.getLatLng().longitude);
                binding.tvSearchBar.setText(selectedAddress.getAddress());
            }


            @Override
            public void onError(@NotNull Status status) {
                Log.e("error", "onError: " + status.getStatusMessage());
                toast(requireContext(), "Oops! something went wrong...");
            }
        });
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

            setMarkerOnMap(
                    MainActivity.currentLocation.getLatitude(),
                    MainActivity.currentLocation.getLongitude());
            selectedAddressName = getLocationAddress(
                    requireContext(),
                    MainActivity.currentLocation.getLatitude(),
                    MainActivity.currentLocation.getLongitude());
            binding.tvSearchBar.setText(selectedAddressName);
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