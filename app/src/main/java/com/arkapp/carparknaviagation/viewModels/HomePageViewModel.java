package com.arkapp.carparknaviagation.viewModels;

import android.content.SharedPreferences;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.data.models.eta.Eta;
import com.arkapp.carparknaviagation.data.models.myTransportCarPark.MyTransportCarPark;
import com.arkapp.carparknaviagation.data.models.myTransportCarPark.MyTransportCarParkAvailability;
import com.arkapp.carparknaviagation.data.models.rates.CarParkInformation;
import com.arkapp.carparknaviagation.data.models.redLightCamera.Feature;
import com.arkapp.carparknaviagation.data.models.speedCamera.SpeedFeature;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCarPark;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCarParkAvailability;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCarParkCharges;
import com.arkapp.carparknaviagation.data.repository.CarParkRepository;
import com.arkapp.carparknaviagation.data.repository.MapRepository;
import com.arkapp.carparknaviagation.data.repository.PrefRepository;
import com.arkapp.carparknaviagation.utility.listeners.HomePageListener;
import com.arkapp.carparknaviagation.utility.maps.search.SearchLocationAdapter;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.arkapp.carparknaviagation.ui.home.Utils.filterAllCarPark;
import static com.arkapp.carparknaviagation.ui.home.Utils.getRouteRedLightCamera;
import static com.arkapp.carparknaviagation.ui.home.Utils.getRouteSpeedCamera;
import static com.arkapp.carparknaviagation.ui.home.Utils.isUraCarPark;
import static com.arkapp.carparknaviagation.ui.home.Utils.removeInvalidMyTransportCarPark;
import static com.arkapp.carparknaviagation.ui.home.Utils.removeInvalidUraCarPark;
import static com.arkapp.carparknaviagation.utility.Constants.API_TOKEN_REFRESH_HOUR;
import static com.arkapp.carparknaviagation.utility.Constants.DEFAULT_CARPARK_COUNT;
import static com.arkapp.carparknaviagation.utility.Constants.SETTING_CARPARK_LOT_KEY;
import static com.arkapp.carparknaviagation.utility.Constants.SETTING_CAR_PARK_KEY;
import static com.arkapp.carparknaviagation.utility.Constants.SETTING_RED_LIGHT_CAMERA_KEY;
import static com.arkapp.carparknaviagation.utility.Constants.SETTING_SPEED_LIMIT_CAMERA_KEY;
import static com.arkapp.carparknaviagation.utility.ViewUtils.isDoubleClicked;
import static com.arkapp.carparknaviagation.utility.ViewUtils.printLog;
import static com.arkapp.carparknaviagation.utility.ViewUtils.show;
import static com.arkapp.carparknaviagation.utility.maps.eta.Utils.getMyTransportCarParkLatLng;
import static com.arkapp.carparknaviagation.utility.maps.eta.Utils.getUraCarParkLatLng;

/**
 * Created by Abdul Rehman on 27-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class HomePageViewModel extends ViewModel {

    public PrefRepository prefRepository;
    private SharedPreferences settingPref;
    public HomePageListener listener;
    public UraCarPark allUraCarParkAvailability;
    public UraCarParkCharges allUraCarParkCharges;
    public UraCarParkAvailability currentSelectedUraCarPark;
    public MyTransportCarPark allMyTransportAvailability;
    public MyTransportCarParkAvailability currentSelectedMyTransportCarPark;
    public ArrayList<Object> allFilteredCarPark = new ArrayList<>();
    public Object currentSelectedCarParkObj;
    public ArrayList<Marker> redLightMarkers = new ArrayList<>();
    public ArrayList<Marker> speedCameraMarkers = new ArrayList<>();
    public String selectedAddressName;
    public boolean processedUraData = false;
    public boolean processedMyTransportData = false;
    public boolean startNavigation = false;
    private CarParkRepository carParkRepository = new CarParkRepository();

    public Marker dropLocationMarker;
    public Marker currentSelectedCarParkMarker;
    public int currentSelectedCarParkNo = 0;
    public Place selectedAddress;

    public Marker currentLocationMarker;
    private MapRepository mapRepository;

    public Polyline polylineFinal = null;
    public BottomSheetDialog carParkList;
    public SearchLocationAdapter searchAdapter;

    public boolean isEditMode = false;
    private ArrayList<UraCarParkAvailability> validUraCarPark;
    private ArrayList<MyTransportCarParkAvailability> validMyTransportCarPark;
    private ArrayList<CarParkInformation> allHdbCarParkCharges;

    public HomePageViewModel(MapRepository mapRepository,
                             PrefRepository prefRepository,
                             SharedPreferences settingPref) {
        this.mapRepository = mapRepository;
        this.prefRepository = prefRepository;
        this.settingPref = settingPref;

        initAllCarParkCharges();
    }

    //This method will set the current location marker on map
    public void initCurrentMarker() {
        if (prefRepository.getCurrentLocation() != null) {

            selectedAddressName = mapRepository.getLocationAddress(prefRepository.getCurrentLocation().latitude, prefRepository.getCurrentLocation().longitude);

            listener.setCurrentLocationMarker(mapRepository.getLocationMarkerOption(prefRepository.getCurrentLocation().latitude,
                                                                                    prefRepository.getCurrentLocation().longitude,
                                                                                    R.drawable.ic_marker1));
        }
    }

    public MutableLiveData<String> getUraCarParkToken() {
        return carParkRepository.getUraCarParkApiToken();
    }

    public MutableLiveData<UraCarPark> getUraCarParkAvailability() {
        return carParkRepository.getUraCarParkAvailability(prefRepository.getApiToken());
    }

    public MutableLiveData<MyTransportCarPark> getMyTransportCarParkAvailability() {
        return carParkRepository.getMyTransportCarParkAvailability();
    }

    public MutableLiveData<Eta> getCarParkEtaFromOrigin(ArrayList<String> allCarParkLatLng,
                                                        String fromLat,
                                                        String fromLng) {
        return carParkRepository.getCarParkEta(allCarParkLatLng, fromLat, fromLng);
    }

    public MutableLiveData<Eta> getCarParkEtaFromDestination(ArrayList<String> allCarParkLatLng,
                                                             String toLat,
                                                             String toLng) {
        return carParkRepository.getCarParkEta(allCarParkLatLng, toLat, toLng);
    }

    private void initAllCarParkCharges() {
        allUraCarParkCharges = mapRepository.getUraCarParkCharges();
        allHdbCarParkCharges = mapRepository.getHdbCarParkInformation();

    }

    //This method will initiate the process of filtering the carparks and showing on map.
    public void initCarParkMarker() {

        //removing old markers
        if (currentSelectedCarParkMarker != null)
            currentSelectedCarParkMarker.remove();

        //Checking if the carpark functionality is enanled in settings.
        if (!settingPref.getBoolean(SETTING_CAR_PARK_KEY, true)) {
            listener.setDestinationMarker();
            ArrayList<String> fromLatLng = new ArrayList<String>();
            fromLatLng.add(String.format("%s,%s", selectedAddress.getLatLng().latitude, selectedAddress.getLatLng().longitude));
            listener.getDestinationEta(getCarParkEtaFromOrigin(fromLatLng,
                                                               prefRepository.getCurrentLocation().latitude + "",
                                                               prefRepository.getCurrentLocation().longitude + ""));
            return;
        }

        processedUraData = false;
        processedMyTransportData = false;

        //Generating the list of valid URA car parks.
        validUraCarPark = removeInvalidUraCarPark(
                prefRepository.getCurrentLocation().latitude,
                prefRepository.getCurrentLocation().longitude,
                selectedAddress.getLatLng().latitude,
                selectedAddress.getLatLng().longitude,
                allUraCarParkCharges,
                allUraCarParkAvailability,
                settingPref.getInt(SETTING_CARPARK_LOT_KEY, DEFAULT_CARPARK_COUNT));

        //Generating the list of valid MyTransport car parks.
        validMyTransportCarPark = removeInvalidMyTransportCarPark(
                prefRepository.getCurrentLocation().latitude,
                prefRepository.getCurrentLocation().longitude,
                selectedAddress.getLatLng().latitude,
                selectedAddress.getLatLng().longitude,
                allMyTransportAvailability,
                allHdbCarParkCharges,
                settingPref.getInt(SETTING_CARPARK_LOT_KEY, DEFAULT_CARPARK_COUNT));

        //If the carpark are available then getting the ETA and distance for the car park.
        if (validUraCarPark.size() > 0) {
            listener.setUraCarParkEtaFromOrigin(
                    getCarParkEtaFromOrigin(getUraCarParkLatLng(validUraCarPark),
                                            prefRepository.getCurrentLocation().latitude + "",
                                            prefRepository.getCurrentLocation().longitude + ""));
        } else
            processedUraData = true;


        //If the carpark are available then getting the ETA and distance for the car park.
        if (validMyTransportCarPark.size() > 0) {
            listener.setMyTransportCarParkEtaFromOrigin(
                    getCarParkEtaFromOrigin(getMyTransportCarParkLatLng(validMyTransportCarPark),
                                            prefRepository.getCurrentLocation().latitude + "",
                                            prefRepository.getCurrentLocation().longitude + ""));
        } else
            processedMyTransportData = true;

        if (validMyTransportCarPark.size() == 0 && validUraCarPark.size() == 0) {
            allFilteredCarPark.clear();
            listener.setCarParking();
        }

    }

    //Adding the carpark eta and distance in the carpark data.
    public void addEtaInUraCarParkFromOrigin(Eta data) {
        int count = 0;
        ArrayList<UraCarParkAvailability> carParkWithEta = new ArrayList<>();
        for (UraCarParkAvailability uraCarParkAvailability : validUraCarPark) {
            uraCarParkAvailability.setEtaDistanceFromOrigin(data.getRows().get(0).getElements().get(count));
            carParkWithEta.add(uraCarParkAvailability);
            count++;
        }
        validUraCarPark = carParkWithEta;

        listener.setUraCarParkEtaFromDestination(
                getCarParkEtaFromDestination(getUraCarParkLatLng(validUraCarPark),
                                             selectedAddress.getLatLng().latitude + "",
                                             selectedAddress.getLatLng().longitude + ""));
    }

    //Adding the carpark eta and distance in the carpark data.
    public void addEtaInUraCarParkFromDestination(Eta data) {

        int count = 0;
        ArrayList<UraCarParkAvailability> carParkWithEta = new ArrayList<>();
        for (UraCarParkAvailability uraCarParkAvailability : validUraCarPark) {
            uraCarParkAvailability.setEtaDistanceFromDestination(data.getRows().get(0).getElements().get(count));
            carParkWithEta.add(uraCarParkAvailability);
            count++;
        }
        validUraCarPark = carParkWithEta;
        processedUraData = true;
        if (processedMyTransportData) {
            allFilteredCarPark.clear();
            allFilteredCarPark = filterAllCarPark(validUraCarPark, validMyTransportCarPark);
            listener.setCarParking();
        }
    }

    //Adding the carpark eta and distance in the carpark data.
    public void addEtaInMyTransportCarParkFromOrigin(Eta data) {
        int count = 0;
        ArrayList<MyTransportCarParkAvailability> carParkWithEta = new ArrayList<>();
        for (MyTransportCarParkAvailability myTransportParkAvailability : validMyTransportCarPark) {
            myTransportParkAvailability.setEtaDistanceFromOrigin(data.getRows().get(0).getElements().get(count));
            carParkWithEta.add(myTransportParkAvailability);
            count++;
        }
        validMyTransportCarPark = carParkWithEta;

        listener.setMyTransportCarParkEtaFromDestination(
                getCarParkEtaFromDestination(getMyTransportCarParkLatLng(validMyTransportCarPark),
                                             selectedAddress.getLatLng().latitude + "",
                                             selectedAddress.getLatLng().longitude + ""));
    }

    //Adding the carpark eta and distance in the carpark data.
    public void addEtaInMyTransportCarParkFromDestination(Eta data) {

        int count = 0;
        ArrayList<MyTransportCarParkAvailability> carParkWithEta = new ArrayList<>();
        for (MyTransportCarParkAvailability myTransportCarParkAvailability : validMyTransportCarPark) {
            myTransportCarParkAvailability.setEtaDistanceFromDestination(data.getRows().get(0).getElements().get(count));
            carParkWithEta.add(myTransportCarParkAvailability);
            count++;
        }
        validMyTransportCarPark = carParkWithEta;

        processedMyTransportData = true;
        if (processedUraData) {
            allFilteredCarPark.clear();
            allFilteredCarPark = filterAllCarPark(validUraCarPark, validMyTransportCarPark);
            listener.setCarParking();
        }
    }

    //Handling the forward arrow click on home screen.
    public void forwardClicked(View view) {
        if (isDoubleClicked(1500))
            return;
        if (allFilteredCarPark.size() > 0 && currentSelectedCarParkNo < allFilteredCarPark.size() - 1) {
            currentSelectedCarParkNo += 1;
            //removing old markers
            if (currentSelectedCarParkMarker != null) currentSelectedCarParkMarker.remove();
            listener.setCarParking();
        }
    }

    //Handling the backward arrow click on home screen.
    public void backwardClicked(View view) {
        if (isDoubleClicked(1500)) return;

        if (allFilteredCarPark.size() > 0 && currentSelectedCarParkNo > 0) {
            currentSelectedCarParkNo -= 1;
            //removing old markers
            if (currentSelectedCarParkMarker != null) currentSelectedCarParkMarker.remove();
            listener.setCarParking();
        }
    }

    //Handling the carpark click on home screen.
    public void nearestCarParkClicked(View view) {
        listener.showCarParkList();
    }

    //Checking if the URA token is valid or not.
    public boolean toUpdateToken() {
        Date currentTime = new Date();

        if (prefRepository.getApiTokenUpdateTime() == null) return true;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(prefRepository.getApiTokenUpdateTime());
        calendar.add(Calendar.HOUR, API_TOKEN_REFRESH_HOUR);
        Date refreshTime = calendar.getTime();
        printLog("refresh time " + refreshTime);
        printLog("current time " + currentTime);
        printLog("toUpdateToken " + currentTime.after(refreshTime));
        return currentTime.after(refreshTime);
    }

    //Initializing the route markers
    public void initRouteMarker() {
        List<Feature> routeRedLightCameras = new ArrayList<>();
        List<SpeedFeature> routeSpeedCameras = new ArrayList<>();

        if (settingPref.getBoolean(SETTING_RED_LIGHT_CAMERA_KEY, true)) {
            routeRedLightCameras = getRouteRedLightCamera(mapRepository.getRedLightCamera(), polylineFinal);
            prefRepository.setCurrentRouteRedLightCamera(routeRedLightCameras);
        }

        if (settingPref.getBoolean(SETTING_SPEED_LIMIT_CAMERA_KEY, true)) {
            routeSpeedCameras = getRouteSpeedCamera(mapRepository.getSpeedCamera(), polylineFinal);
            prefRepository.setCurrentRouteSpeedCamera(routeSpeedCameras);
        }

        listener.setRouteCameras(routeRedLightCameras, routeSpeedCameras);
    }

    //Handling the navigation click on home screen.
    public void navigationClicked(View view) {
        if (isDoubleClicked(1500)) return;

        prefRepository.setNavigationStartLat((float) prefRepository.getCurrentLocation().latitude);
        prefRepository.setNavigationStartLng((float) prefRepository.getCurrentLocation().longitude);

        if (!settingPref.getBoolean(SETTING_CAR_PARK_KEY, true)) {
            prefRepository.setNavigationEndLat((float) selectedAddress.getLatLng().latitude);
            prefRepository.setNavigationEndLng((float) selectedAddress.getLatLng().longitude);
        } else {
            if (isUraCarPark(currentSelectedCarParkObj)) {
                prefRepository.setNavigationEndLat((float) currentSelectedUraCarPark.getLat());
                prefRepository.setNavigationEndLng((float) currentSelectedUraCarPark.getLng());
            } else {
                prefRepository.setNavigationEndLat((float) currentSelectedMyTransportCarPark.getCarParkLat());
                prefRepository.setNavigationEndLng((float) currentSelectedMyTransportCarPark.getCarParkLng());
            }
        }
        listener.startNavigation();
    }

    public void showHistoryDestination() {
        printLog("showHistoryDestination called");
        searchAdapter.clear();
        searchAdapter.showHistory = true;
        show(searchAdapter.searchRecyclerView);
        searchAdapter.historyList = prefRepository.getDestinationHistory();
        searchAdapter.notifyDataSetChanged();
    }
}
