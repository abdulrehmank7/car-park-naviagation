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
import com.arkapp.carparknaviagation.utility.maps.route.GetRouteTask;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.arkapp.carparknaviagation.ui.main.MainActivity.currentLocation;
import static com.arkapp.carparknaviagation.ui.main.MainActivity.gpsListener;
import static com.arkapp.carparknaviagation.utility.Constants.GOOGLE_KEY;
import static com.arkapp.carparknaviagation.utility.ViewUtils.hide;
import static com.arkapp.carparknaviagation.utility.ViewUtils.printLog;
import static com.arkapp.carparknaviagation.utility.ViewUtils.show;
import static com.arkapp.carparknaviagation.utility.ViewUtils.showSnack;
import static com.arkapp.carparknaviagation.utility.ViewUtils.toast;
import static com.arkapp.carparknaviagation.utility.maps.LocationUtils.getGPSSettingTask;
import static com.arkapp.carparknaviagation.utility.maps.LocationUtils.startLocationUpdates;
import static com.arkapp.carparknaviagation.utility.maps.MapUtils.REQUEST_CHECK_SETTINGS;
import static com.arkapp.carparknaviagation.utility.maps.MapUtils.addCustomCurrentMaker;
import static com.arkapp.carparknaviagation.utility.maps.MapUtils.fitRouteInScreen;
import static com.arkapp.carparknaviagation.utility.maps.MapUtils.getLocationAddress;
import static com.arkapp.carparknaviagation.utility.maps.MapUtils.getMapsApiDirectionsFromUrl;

public class HomeFragment extends Fragment {

    public static Polyline polylineFinal = null;
    private FragmentHomeBinding binding;
    private GoogleMap map = null;
    private Place selectedAddress;
    private AutocompleteSupportFragment autocompleteFragment;
    private SupportMapFragment mapView = null;
    private Marker currentLocationMarker;
    private String selectedAddressName;
    private Marker dropLocationMarker;


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

        show(binding.progressBar);

        //Overriding the gpsListener to get the updates
        gpsListener = this::checkGpsSetting;

        initMaps();
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

    private void initMaps() {

        mapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapView.getMapAsync(googleMap -> {
            hide(binding.progressBar);

            map = googleMap;
            selectedAddressName = getLocationAddress(
                    requireContext(),
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude());
            binding.tvSearchBar.setText(selectedAddressName);

            setCurrentLocationMarkerOnMap(currentLocation.getLatitude(), currentLocation.getLongitude());
        });
    }

    private void setCurrentLocationMarkerOnMap(double lat, double log) {
        //removing the old marker
        if (currentLocationMarker != null)
            currentLocationMarker.remove();

        currentLocationMarker = map.addMarker(addCustomCurrentMaker(requireContext(), lat, log, R.drawable.ic_marker1));

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, log), 17));
    }

    private void setDropLocationMarkerOnMap(double lat, double log) {
        //removing the old marker
        if (dropLocationMarker != null)
            dropLocationMarker.remove();

        dropLocationMarker = map.addMarker(addCustomCurrentMaker(requireContext(), lat, log, R.drawable.ic_marker2));

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, log), 17));
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
                setDropLocationMarkerOnMap(place.getLatLng().latitude, place.getLatLng().longitude);
                binding.tvSearchBar.setText(selectedAddress.getAddress());

                drawMapRoute();
            }


            @Override
            public void onError(@NotNull Status status) {
                Log.e("error", "onError: " + status.getStatusMessage());
                toast(requireContext(), "Oops! something went wrong...");
            }
        });
    }

    private void drawMapRoute() {
        fitRouteInScreen(map,
                         new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                         selectedAddress.getLatLng(), requireContext());

        String url = getMapsApiDirectionsFromUrl(
                currentLocation.getLatitude() + "",
                currentLocation.getLongitude() + "",
                selectedAddress.getLatLng().latitude + "",
                selectedAddress.getLatLng().longitude + "",
                ""/*avoid=tolls*/);

        printLog("polyline map url = " + url);

        if (!url.equals("")) {
            if (polylineFinal != null) {
                polylineFinal.remove();
            }
            int colourForPathPlot = getResources().getColor(R.color.colorPrimaryCustomDark);

            GetRouteTask downloadTask = new GetRouteTask(map, colourForPathPlot);
            downloadTask.execute(url);
        }
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

            setCurrentLocationMarkerOnMap(
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude());
            selectedAddressName = getLocationAddress(
                    requireContext(),
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude());
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