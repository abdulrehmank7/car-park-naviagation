package com.arkapp.carparknaviagation.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.data.models.PlaceAutoComplete;
import com.arkapp.carparknaviagation.data.models.SearchedHistory;
import com.arkapp.carparknaviagation.data.models.eta.ElementsForEta;
import com.arkapp.carparknaviagation.data.models.eta.Eta;
import com.arkapp.carparknaviagation.data.models.myTransportCarPark.MyTransportCarParkAvailability;
import com.arkapp.carparknaviagation.data.models.redLightCamera.Feature;
import com.arkapp.carparknaviagation.data.models.speedCamera.SpeedFeature;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCarParkAvailability;
import com.arkapp.carparknaviagation.data.repository.MapRepository;
import com.arkapp.carparknaviagation.data.repository.PrefRepository;
import com.arkapp.carparknaviagation.databinding.FragmentHomeBinding;
import com.arkapp.carparknaviagation.ui.carParkList.CarParkListAdapter;
import com.arkapp.carparknaviagation.ui.main.MainActivity;
import com.arkapp.carparknaviagation.ui.navigation.NavigationActivity;
import com.arkapp.carparknaviagation.utility.listeners.HomePageListener;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.arkapp.carparknaviagation.ui.carParkList.Utils.getPerHourCharge;
import static com.arkapp.carparknaviagation.ui.home.Utils.clearSearchTextListener;
import static com.arkapp.carparknaviagation.ui.home.Utils.drawMapRoute;
import static com.arkapp.carparknaviagation.ui.home.Utils.getRedLightMarker;
import static com.arkapp.carparknaviagation.ui.home.Utils.getSpeedMarker;
import static com.arkapp.carparknaviagation.ui.home.Utils.initSearchTextListener;
import static com.arkapp.carparknaviagation.ui.home.Utils.isUraCarPark;
import static com.arkapp.carparknaviagation.ui.home.Utils.setLocationMarkerOnMap;
import static com.arkapp.carparknaviagation.ui.main.MainActivity.gpsListener;
import static com.arkapp.carparknaviagation.utility.ViewUtils.dpToPx;
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
import static com.arkapp.carparknaviagation.utility.maps.others.MapUtils.getCustomMaker;

public class HomeFragment extends Fragment implements HomePageListener {

    private FragmentHomeBinding binding = null;
    private GoogleMap map = null;
    private SupportMapFragment mapView = null;

    private HomePageViewModel viewModel;

    //Text change listener for the search field in the home screen.
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            //Showing the past history or suggestion according to the text length
            if (editable != null && editable.toString().length() > 0) {
                viewModel.searchAdapter.showHistory = false;
                viewModel.searchAdapter.getSearchResult(editable.toString());
            } else
                viewModel.showHistoryDestination();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater);
        MapRepository repository = new MapRepository(requireContext());
        PrefRepository prefRepository = new PrefRepository(requireContext());
        SharedPreferences settingPref = PreferenceManager.getDefaultSharedPreferences(requireContext());

        //Initializing the view model of the home screen.
        HomePageViewModelFactory factory = new HomePageViewModelFactory(repository, prefRepository, settingPref);

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

        //Initializing the home screen components.
        initMaps();
        initSearchUI();
        initClickListeners();
        initSearchField();
        initCarParkAvailabilityData();

    }

    //Getting the carpark data from the apis.
    private void initCarParkAvailabilityData() {
        //Checking the URA token if its refreshed or not
        if (viewModel.toUpdateToken())
            viewModel.getUraCarParkToken()
                    .observe(getViewLifecycleOwner(), token -> {
                        viewModel.prefRepository.setApiToken(token);

                        viewModel.getUraCarParkAvailability()
                                .observe(getViewLifecycleOwner(), carParking -> viewModel.allUraCarParkAvailability = carParking);

                    });
        else viewModel.getUraCarParkAvailability().observe(getViewLifecycleOwner(), response -> {

            if (response == null || response.getMessage().equals(getString(R.string.token_error))) {
                viewModel.getUraCarParkToken()
                        .observe(getViewLifecycleOwner(), token -> {
                            viewModel.prefRepository.setApiToken(token);

                            viewModel.getUraCarParkAvailability()
                                    .observe(getViewLifecycleOwner(), newResponse ->
                                            viewModel.allUraCarParkAvailability = newResponse);
                        });
            } else
                viewModel.allUraCarParkAvailability = response;
        });

        viewModel.getMyTransportCarParkAvailability()
                .observe(getViewLifecycleOwner(), response -> viewModel.allMyTransportAvailability = response);
    }

    //Initializing the google map and showing current location marker on map
    private void initMaps() {

        mapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapView.getMapAsync(googleMap -> {
            hide(binding.progressBar);

            map = googleMap;

            viewModel.initCurrentMarker();
            binding.etSearchBar.setText(viewModel.selectedAddressName);

        });
    }

    //Initializing the search field with the recycler view.
    private void initSearchUI() {
        viewModel.searchAdapter = new SearchLocationAdapter(new ArrayList(), requireContext());
        initVerticalAdapter(
                binding.rvSearchResult,
                viewModel.searchAdapter,
                false);
    }

    //Adding the click listener for the setting and gps icon in the home screen
    private void initClickListeners() {
        binding.cvMyLocation.setOnClickListener(view12 -> {
            showSnack(binding.parent, "Fetching Current Location...");
            askRuntimePermission();
        });
        binding.cvSetting.setOnClickListener(view12 -> {
            ((MainActivity) requireActivity()).openSetting();
        });
    }

    //Initializing the search field listeners with custom logic to behave as expected.
    private void initSearchField() {

        //Enter button click listener from softkeyboard
        binding.etSearchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Your piece of code on keyboard search click
                viewModel.searchAdapter.selectFirstAddress();
                binding.etSearchBar.clearFocus();
                return true;
            }
            return false;
        });

        //Search icon listener in the search field.
        binding.ivSearchIcon.setOnClickListener(view13 -> {
            if (viewModel.isEditMode)
                binding.etSearchBar.clearFocus();
            else
                binding.etSearchBar.requestFocus();
        });

        //Focus change listener for the search bar in the home screen.
        binding.etSearchBar.setOnFocusChangeListener((view12, hasFocus) -> {
            clearSearchTextListener(textWatcher, binding.etSearchBar);
            hide(binding.cvBottomView);
            map.setPadding(0, 0, 0, 0);

            if (hasFocus) {
                binding.ivSearchIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back));
                viewModel.isEditMode = true;
                binding.etSearchBar.setText("");
                viewModel.showHistoryDestination();
                initSearchTextListener(textWatcher, binding.etSearchBar);
            } else {
                hideKeyboard(requireContext(), binding.etSearchBar);
                binding.ivSearchIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_search));
                viewModel.searchAdapter.clear();
                viewModel.searchAdapter.clearHistory();
                viewModel.isEditMode = false;
                if (viewModel.selectedAddress != null) {
                    binding.etSearchBar.setText(viewModel.selectedAddress.getAddress());
                    show(binding.cvBottomView);
                    map.setPadding(0, dpToPx(68), 0, dpToPx(158));
                } else
                    binding.etSearchBar.setText(viewModel.selectedAddressName);
            }
        });

        //When suggestion address is clicked on Home screen. We will get the lat lang of the address.
        viewModel.searchAdapter.setOnItemClickListener((position, view1) -> {
            if (isDoubleClicked(1000)) return;
            clearSearchTextListener(textWatcher, binding.etSearchBar);

            show(binding.progressBar);
            viewModel.currentSelectedCarParkNo = 0;
            try {
                final String placeId;
                if (viewModel.searchAdapter.showHistory) {
                    final SearchedHistory history = viewModel.searchAdapter.historyList.get(position);
                    placeId = history.placeId;
                } else {
                    final PlaceAutoComplete destination = viewModel.searchAdapter.getItem(position);
                    placeId = String.valueOf(destination.placeId);

                    SearchedHistory destinationHistory = new SearchedHistory(
                            destination.placeId.toString(),
                            destination.description.toString(),
                            destination.area.toString());

                    viewModel.prefRepository.setDestinationHistory(destinationHistory);
                }
                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

                //Fetching the selected address latitude, longitude and name
                viewModel.searchAdapter.placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                    binding.etSearchBar.clearFocus();
                    hideKeyboard(requireContext(), binding.etSearchBar);
                    viewModel.searchAdapter.clear();
                    viewModel.searchAdapter.clearHistory();

                    Place place = response.getPlace();

                    viewModel.selectedAddress = place;
                    binding.etSearchBar.setText(viewModel.selectedAddress.getAddress());
                    //binding.etSearchBar.setText(destination.description);

                    viewModel.dropLocationMarker = setLocationMarkerOnMap(
                            place.getLatLng().latitude,
                            place.getLatLng().longitude,
                            viewModel.dropLocationMarker,
                            R.drawable.ic_marker2,
                            map,
                            requireContext());


                    viewModel.initCarParkMarker();
                }).addOnFailureListener((exception) -> {
                    hide(binding.progressBar);
                    if (exception instanceof ApiException) {
                        printLog(getString(R.string.oops));
                    }
                });
            } catch (Exception e) {
                hide(binding.progressBar);
                e.printStackTrace();
            }
        });
    }

    //Showing the current location marker on map and animating the google map
    @Override
    public void setCurrentLocationMarker(MarkerOptions markerOptions) {
        if (viewModel.currentLocationMarker != null)
            viewModel.currentLocationMarker.remove();

        viewModel.currentLocationMarker = map.addMarker(markerOptions);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(viewModel.prefRepository.getCurrentLocation().latitude,
                           viewModel.prefRepository.getCurrentLocation().longitude), 17));

    }

    //Showing the destination maker on the home screen when carpark functionality
    //is disabled from setting screen.
    @Override
    public void setDestinationMarker() {
        show(binding.progressBar);
        hide(binding.cvBottomView);

        map.setPadding(0, dpToPx(68), 0, dpToPx(72));

        drawMapRoute(
                viewModel.prefRepository.getCurrentLocation().latitude,
                viewModel.prefRepository.getCurrentLocation().longitude,
                viewModel.selectedAddress.getLatLng().latitude,
                viewModel.selectedAddress.getLatLng().longitude,
                viewModel.selectedAddress.getLatLng().latitude,
                viewModel.selectedAddress.getLatLng().longitude,
                requireContext(),
                map,
                this,
                viewModel.polylineFinal);

        show(binding.cvNavigate);
    }

    //Showing the carpark marker on the home screen
    @Override
    public void setCarParking() {
        hide(binding.cvNavigate);

        //If navigation is clicked from carpark list open naviagtion
        if (viewModel.startNavigation) {
            showSnack(binding.parent, getString(R.string.starting_navigation));
        }

        //If no carpark are available then show msg.
        printLog("setting car park");
        if (viewModel.allFilteredCarPark.size() == 0) {
            hide(binding.cvBottomView);
            showSnack(binding.parent, "No car park found!");
            hide(binding.progressBar);
            return;
        }
        show(binding.progressBar);
        show(binding.cvBottomView);

        if (viewModel.currentSelectedCarParkNo == 0) hide(binding.btBack);
        else show(binding.btBack);

        if (viewModel.currentSelectedCarParkNo == viewModel.allFilteredCarPark.size() - 1)
            hide(binding.btForward);
        else show(binding.btForward);

        viewModel.currentSelectedCarParkObj = viewModel.allFilteredCarPark.get(viewModel.currentSelectedCarParkNo);
        show(binding.cvBottomView);

        //Checking if URA or MyTransport carpark and showing their details on home screen.
        if (isUraCarPark(viewModel.currentSelectedCarParkObj)) {
            viewModel.currentSelectedUraCarPark = (UraCarParkAvailability) viewModel.currentSelectedCarParkObj;
            viewModel.currentSelectedCarParkMarker = map.addMarker(getCustomMaker(requireContext(),
                                                                                  viewModel.currentSelectedUraCarPark.getLat(),
                                                                                  viewModel.currentSelectedUraCarPark.getLng(),
                                                                                  R.drawable.ic_parking_marker));

            map.setPadding(0, dpToPx(68), 0, dpToPx(158));
            drawMapRoute(
                    viewModel.prefRepository.getCurrentLocation().latitude,
                    viewModel.prefRepository.getCurrentLocation().longitude,
                    viewModel.currentSelectedUraCarPark.getLat(),
                    viewModel.currentSelectedUraCarPark.getLng(),
                    viewModel.selectedAddress.getLatLng().latitude,
                    viewModel.selectedAddress.getLatLng().longitude,
                    requireContext(),
                    map,
                    this,
                    viewModel.polylineFinal);

            binding.tvCarParkName.setText(viewModel.currentSelectedUraCarPark.getCharges().getPpName());
            binding.tvEstimatedDistance.setText(viewModel.currentSelectedUraCarPark.getEtaDistanceFromOrigin().getDistance().getText());
            binding.tvEstimatedTime.setText(viewModel.currentSelectedUraCarPark.getEtaDistanceFromOrigin().getDuration().getText());
            binding.tvAvailableLots.setText(String.format("%s", viewModel.currentSelectedUraCarPark.getLotsAvailable()));
            binding.tvRates.setText(String.format(Locale.ENGLISH, "$%.2f/Hr", getPerHourCharge(viewModel.currentSelectedUraCarPark.getCharges())));
        } else {
            viewModel.currentSelectedMyTransportCarPark = (MyTransportCarParkAvailability) viewModel.currentSelectedCarParkObj;

            viewModel.currentSelectedCarParkMarker = map.addMarker(getCustomMaker(requireContext(),
                                                                                  viewModel.currentSelectedMyTransportCarPark.getCarParkLat(),
                                                                                  viewModel.currentSelectedMyTransportCarPark.getCarParkLng(),
                                                                                  R.drawable.ic_parking_marker));

            map.setPadding(0, dpToPx(68), 0, dpToPx(158));
            drawMapRoute(
                    viewModel.prefRepository.getCurrentLocation().latitude,
                    viewModel.prefRepository.getCurrentLocation().longitude,
                    viewModel.currentSelectedMyTransportCarPark.getCarParkLat(),
                    viewModel.currentSelectedMyTransportCarPark.getCarParkLng(),
                    viewModel.selectedAddress.getLatLng().latitude,
                    viewModel.selectedAddress.getLatLng().longitude,
                    requireContext(),
                    map,
                    this,
                    viewModel.polylineFinal);

            binding.tvCarParkName.setText(viewModel.currentSelectedMyTransportCarPark.getDevelopment());
            binding.tvEstimatedDistance.setText(viewModel.currentSelectedMyTransportCarPark.getEtaDistanceFromOrigin().getDistance().getText());
            binding.tvEstimatedTime.setText(viewModel.currentSelectedMyTransportCarPark.getEtaDistanceFromOrigin().getDuration().getText());
            binding.tvAvailableLots.setText(String.format("%s", viewModel.currentSelectedMyTransportCarPark.getAvailableLots()));
            binding.tvRates.setText(viewModel.currentSelectedMyTransportCarPark.getChargeValue());
        }
    }

    //Showing the carpark list if the Bottom view is clicked in home screen.
    @Override
    public void showCarParkList() {
        CarParkListAdapter carParkListAdapter = new CarParkListAdapter(viewModel);

        //Creating bottom sheet dialog to show the carpark list.
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_car_park_list, null);
        viewModel.carParkList = new BottomSheetDialog(requireContext());
        viewModel.carParkList.setContentView(bottomSheetView);

        //used to set the height of driver list to fullscreen
        viewModel.carParkList.setOnShowListener(dialogInterface -> {
            BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
            View bottomSheetInternal = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        //Setting carpark recycler view adapter to show car park list.
        RecyclerView rvCarParkList = bottomSheetView.findViewById(R.id.rvCarParks);
        initVerticalAdapter(rvCarParkList, carParkListAdapter, true);
        bottomSheetView.findViewById(R.id.btBack).setOnClickListener(view -> viewModel.carParkList.dismiss());

        viewModel.carParkList.show();
    }

    //Setting the route on the map from origin to the carpark
    @Override
    public void setRoute(PolylineOptions polyLineOptions) {
        if (viewModel.polylineFinal != null)
            viewModel.polylineFinal.remove();
        viewModel.polylineFinal = map.addPolyline(polyLineOptions);
        viewModel.initRouteMarker();
    }

    //Shwoing the route camera like red light and speed camera.
    @Override
    public void setRouteCameras(List<Feature> redLightMarkers, List<SpeedFeature> routeSpeedCameras) {
        hide(binding.progressBar);
        //removing old markers
        if (!viewModel.redLightMarkers.isEmpty())
            for (Marker marker : viewModel.redLightMarkers)
                marker.remove();


        //Checking route latitude longitude and showing the red light marker.
        for (MarkerOptions markerOption : getRedLightMarker(redLightMarkers, requireContext())) {
            viewModel.redLightMarkers.add(map.addMarker(markerOption));
        }

        //removing old markers
        if (!viewModel.speedCameraMarkers.isEmpty())
            for (Marker marker : viewModel.speedCameraMarkers)
                marker.remove();

        //Checking route latitude longitude and showing the speed camera marker.
        for (MarkerOptions markerOption : getSpeedMarker(routeSpeedCameras, requireContext())) {
            viewModel.speedCameraMarkers.add(map.addMarker(markerOption));
        }

        if (viewModel.startNavigation) {
            viewModel.startNavigation = false;
            startNavigation();
        }
    }

    @Override
    public void setUraCarParkEtaFromOrigin(MutableLiveData<Eta> carParkEtaFromOrigin) {
        carParkEtaFromOrigin.observe(getViewLifecycleOwner(), eta -> viewModel.addEtaInUraCarParkFromOrigin(eta));
    }

    @Override
    public void setUraCarParkEtaFromDestination(MutableLiveData<Eta> carParkEtaFromDestination) {
        carParkEtaFromDestination.observe(getViewLifecycleOwner(), eta -> viewModel.addEtaInUraCarParkFromDestination(eta));
    }

    @Override
    public void setMyTransportCarParkEtaFromOrigin(MutableLiveData<Eta> carParkEtaFromOrigin) {
        carParkEtaFromOrigin.observe(getViewLifecycleOwner(), eta -> viewModel.addEtaInMyTransportCarParkFromOrigin(eta));
    }

    @Override
    public void setMyTransportCarParkEtaFromDestination(
            MutableLiveData<Eta> carParkEtaFromDestination) {
        carParkEtaFromDestination.observe(getViewLifecycleOwner(), eta -> viewModel.addEtaInMyTransportCarParkFromDestination(eta));
    }

    @Override
    public void getDestinationEta(MutableLiveData<Eta> destinationEtaFromOrigin) {
        destinationEtaFromOrigin.observe(
                getViewLifecycleOwner(),
                eta -> {
                    ElementsForEta data = eta.getRows().get(0).getElements().get(0);
                    binding.tvNavigateEstimatedDistance.setText(data.getDistance().getText());
                    binding.tvNavigateEstimatedTime.setText(data.getDuration().getText());
                });
    }

    //On navigation clicked starting the navigation
    @Override
    public void startNavigation() {
        if (binding.progressBar.getVisibility() == View.VISIBLE) {
            showSnack(binding.parent, getString(R.string.wait));
            return;
        }
        startActivity(new Intent(requireContext(), NavigationActivity.class));
    }

    //Getting the location permission and gps permission of the device.
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

    //Check if gps is enable or not.
    public void checkGpsSetting() {
        Task<LocationSettingsResponse> task = getGPSSettingTask(requireContext());
        task.addOnSuccessListener(requireActivity(), locationSettingsResponse -> {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            startLocationUpdates(requireContext());

            if (viewModel.prefRepository.getCurrentLocation() != null) {
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
}