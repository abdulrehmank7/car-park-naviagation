package com.arkapp.carparknaviagation.ui.home;

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
import com.google.android.gms.common.api.Status;
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

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static com.arkapp.carparknaviagation.utility.Constants.GOOGLE_KEY;
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

        initHereMaps();
        initAutoComplete();
        initClickListeners();
    }

    private void initClickListeners() {
        binding.cvSearchBar.setOnClickListener(view1 -> {
            final View root = autocompleteFragment.getView();
            root.post(() -> root.findViewById(R.id.places_autocomplete_search_input).performClick());
        });

        binding.cvSetting.setOnClickListener(view12 -> showSnack(binding.parent, "In development..."));
        binding.cvMyLocation.setOnClickListener(view12 -> showSnack(binding.parent, "In development..."));
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
                            map.setMapScheme(Map.Scheme.NORMAL_TRAFFIC_DAY);
                            map.setExtrudedBuildingsVisible(false);
                            map.setLandmarksVisible(false);
                        }
                        mapView.setMap(map);


                        setMarkerOnMap(1.366026, 103.847595);

                    } else {
                        Log.e("ERROR", error.getStackTrace());
                    }
                });
    }

    private void setMarkerOnMap(double lat, double log) {
        GeoCoordinate location = new GeoCoordinate(lat, log);

        map.setCenter(location, Map.Animation.NONE);
        MapMarker defaultMarker = new MapMarker();
        defaultMarker.setCoordinate(location);

        map.addMapObject(defaultMarker);

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
        //autocompleteFragment.setCountry("sgp");
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
}