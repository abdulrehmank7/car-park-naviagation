package com.arkapp.carparknaviagation.ui.home;

import android.Manifest;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.data.models.PlaceAutoComplete;
import com.arkapp.carparknaviagation.data.repository.MapRepository;
import com.arkapp.carparknaviagation.databinding.FragmentHomeBinding;
import com.arkapp.carparknaviagation.utility.listeners.HomePageListener;
import com.arkapp.carparknaviagation.utility.maps.route.GetRouteTask;
import com.arkapp.carparknaviagation.utility.maps.search.SearchLocationAdapter;
import com.arkapp.carparknaviagation.utility.viewModelFactory.HomePageViewModelFactory;
import com.arkapp.carparknaviagation.viewModels.HomePageViewModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.arkapp.carparknaviagation.ui.home.Utils.clearSearchTextListener;
import static com.arkapp.carparknaviagation.ui.home.Utils.initSearchTextListener;
import static com.arkapp.carparknaviagation.ui.home.Utils.setLocationMarkerOnMap;
import static com.arkapp.carparknaviagation.ui.main.MainActivity.currentLocation;
import static com.arkapp.carparknaviagation.ui.main.MainActivity.gpsListener;
import static com.arkapp.carparknaviagation.utility.ViewUtils.hide;
import static com.arkapp.carparknaviagation.utility.ViewUtils.hideKeyboard;
import static com.arkapp.carparknaviagation.utility.ViewUtils.initVerticalAdapter;
import static com.arkapp.carparknaviagation.utility.ViewUtils.isDoubleClicked;
import static com.arkapp.carparknaviagation.utility.ViewUtils.printLog;
import static com.arkapp.carparknaviagation.utility.ViewUtils.show;
import static com.arkapp.carparknaviagation.utility.ViewUtils.showSnack;
import static com.arkapp.carparknaviagation.utility.maps.others.LocationUtils.getGPSSettingTask;
import static com.arkapp.carparknaviagation.utility.maps.others.LocationUtils.startLocationUpdates;
import static com.arkapp.carparknaviagation.utility.maps.others.MapUtils.REQUEST_CHECK_SETTINGS;
import static com.arkapp.carparknaviagation.utility.maps.others.MapUtils.fitRouteInScreen;
import static com.arkapp.carparknaviagation.utility.maps.others.MapUtils.getMapsApiDirectionsFromUrl;

public class HomeFragment extends Fragment implements HomePageListener {

    private FragmentHomeBinding binding;
    private GoogleMap map = null;
    private SupportMapFragment mapView = null;

    private HomePageViewModel viewModel;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable != null && editable.toString().length() > 0)
                viewModel.searchAdapter.getSearchResult(editable.toString());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater);
        MapRepository repository = new MapRepository(requireContext());
        HomePageViewModelFactory factory = new HomePageViewModelFactory(repository);

        viewModel = new ViewModelProvider(this, factory).get(HomePageViewModel.class);
        viewModel.listener = this;
        binding.setViewmodel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        show(binding.progressBar);

        //Overriding the gpsListener to get the updates
        gpsListener = this::checkGpsSetting;

        initMaps();
        initSearchUI();
        initClickListeners();
        initSearchField();
    }

    private void initMaps() {

        mapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapView.getMapAsync(googleMap -> {
            hide(binding.progressBar);

            map = googleMap;

            viewModel.initCurrentMarker();
            viewModel.initRedLightCamera();
            binding.etSearchBar.setText(viewModel.selectedAddressName);

        });
    }

    private void initSearchUI() {
        viewModel.searchAdapter = new SearchLocationAdapter(new ArrayList(), requireContext());
        initVerticalAdapter(
                binding.rvSearchResult,
                viewModel.searchAdapter,
                false);
    }

    private void initClickListeners() {
        binding.cvMyLocation.setOnClickListener(view12 -> {
            showSnack(binding.parent, "Fetching Current Location...");
            askRuntimePermission();
        });
        binding.cvSetting.setOnClickListener(view12 -> showSnack(binding.parent, "In development..."));
        binding.cvHistory.setOnClickListener(view12 -> showSnack(binding.parent, "In development..."));
    }

    private void initSearchField() {

        binding.etSearchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Your piece of code on keyboard search click
                viewModel.searchAdapter.selectFirstAddress();
                binding.etSearchBar.clearFocus();
                return true;
            }
            return false;
        });

        binding.ivSearchIcon.setOnClickListener(view13 -> {
            if (viewModel.isEditMode)
                binding.etSearchBar.clearFocus();
            else
                binding.etSearchBar.requestFocus();
        });

        binding.etSearchBar.setOnFocusChangeListener((view12, hasFocus) -> {
            clearSearchTextListener(textWatcher, binding.etSearchBar);
            if (hasFocus) {
                binding.ivSearchIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back));
                viewModel.isEditMode = true;
                binding.etSearchBar.setText("");
                initSearchTextListener(textWatcher, binding.etSearchBar);
            } else {
                hideKeyboard(requireContext(), binding.etSearchBar);
                binding.ivSearchIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_search));
                viewModel.searchAdapter.clear();
                viewModel.isEditMode = false;
                if (viewModel.selectedAddress != null)
                    binding.etSearchBar.setText(viewModel.selectedAddress.getAddress());
                else
                    binding.etSearchBar.setText(viewModel.selectedAddressName);
            }
        });

        viewModel.searchAdapter.setOnItemClickListener((position, view1) -> {
            clearSearchTextListener(textWatcher, binding.etSearchBar);
            if (isDoubleClicked(1000)) return;
            try {
                final PlaceAutoComplete item = viewModel.searchAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);

                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

                viewModel.searchAdapter.placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                    binding.etSearchBar.clearFocus();
                    hideKeyboard(requireContext(), binding.etSearchBar);
                    viewModel.searchAdapter.clear();
                    Place place = response.getPlace();

                    //Do the things here on Click.....
                    viewModel.selectedAddress = place;
                    binding.etSearchBar.setText(viewModel.selectedAddress.getAddress());
                    //binding.etSearchBar.setText(item.description);

                    viewModel.dropLocationMarker = setLocationMarkerOnMap(
                            place.getLatLng().latitude,
                            place.getLatLng().longitude,
                            viewModel.dropLocationMarker,
                            R.drawable.ic_marker2,
                            map,
                            requireContext());

                    drawMapRoute();
                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        printLog(getString(R.string.oops));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void drawMapRoute() {
        fitRouteInScreen(map,
                         new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                         viewModel.selectedAddress.getLatLng(), requireContext());

        String url = getMapsApiDirectionsFromUrl(
                currentLocation.getLatitude() + "",
                currentLocation.getLongitude() + "",
                viewModel.selectedAddress.getLatLng().latitude + "",
                viewModel.selectedAddress.getLatLng().longitude + "",
                ""/*avoid=tolls*/);

        printLog("polyline map url = " + url);

        if (!url.equals("")) {
            if (HomePageViewModel.polylineFinal != null) {
                HomePageViewModel.polylineFinal.remove();
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

            if (currentLocation != null) {
                viewModel.initCurrentMarker();
                binding.etSearchBar.setText(viewModel.selectedAddressName);
            }
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

    @Override
    public void setCurrentLocationMarker(MarkerOptions markerOptions) {
        if (viewModel.currentLocationMarker != null)
            viewModel.currentLocationMarker.remove();

        viewModel.currentLocationMarker = map.addMarker(markerOptions);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 17));

    }

    @Override
    public void setRedLightCamera(ArrayList<MarkerOptions> redLightMarkers) {
        for (MarkerOptions markerOption : redLightMarkers) {
            viewModel.redLightMarkers.add(map.addMarker(markerOption));
        }
    }
}