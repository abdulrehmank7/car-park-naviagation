package com.arkapp.carparknaviagation.viewModels;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.data.models.eta.Eta;
import com.arkapp.carparknaviagation.data.models.myTransportCarPark.MyTransportCarPark;
import com.arkapp.carparknaviagation.data.models.myTransportCarPark.MyTransportCarParkAvailability;
import com.arkapp.carparknaviagation.data.models.rates.CarParkInformation;
import com.arkapp.carparknaviagation.data.models.redLightCamera.Feature;
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
import static com.arkapp.carparknaviagation.utility.ViewUtils.isDoubleClicked;
import static com.arkapp.carparknaviagation.utility.ViewUtils.printLog;
import static com.arkapp.carparknaviagation.utility.maps.eta.Utils.getMyTransportCarParkLatLng;
import static com.arkapp.carparknaviagation.utility.maps.eta.Utils.getUraCarParkLatLng;

/**
 * Created by Abdul Rehman on 27-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class HomePageViewModel extends ViewModel {

    public PrefRepository prefRepository;
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
                             PrefRepository prefRepository) {
        this.mapRepository = mapRepository;
        this.prefRepository = prefRepository;

        initAllCarParkCharges();
    }

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

    public void initCarParkMarker() {

        //removing old markers
        if (currentSelectedCarParkMarker != null)
            currentSelectedCarParkMarker.remove();

        processedUraData = false;
        processedMyTransportData = false;

        validUraCarPark = removeInvalidUraCarPark(prefRepository.getCurrentLocation().latitude,
                                                  prefRepository.getCurrentLocation().longitude,
                                                  selectedAddress.getLatLng().latitude,
                                                  selectedAddress.getLatLng().longitude,
                                                  allUraCarParkCharges,
                                                  allUraCarParkAvailability);

        validMyTransportCarPark = removeInvalidMyTransportCarPark(prefRepository.getCurrentLocation().latitude,
                                                                  prefRepository.getCurrentLocation().longitude,
                                                                  selectedAddress.getLatLng().latitude,
                                                                  selectedAddress.getLatLng().longitude,
                                                                  allMyTransportAvailability,
                                                                  allHdbCarParkCharges);
        if (validUraCarPark.size() > 0) {
            listener.setUraCarParkEtaFromOrigin(
                    getCarParkEtaFromOrigin(getUraCarParkLatLng(validUraCarPark),
                                            prefRepository.getCurrentLocation().latitude + "",
                                            prefRepository.getCurrentLocation().longitude + ""));
        } else
            processedUraData = true;

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

    public void backwardClicked(View view) {
        if (isDoubleClicked(1500)) return;

        if (allFilteredCarPark.size() > 0 && currentSelectedCarParkNo > 0) {
            currentSelectedCarParkNo -= 1;
            //removing old markers
            if (currentSelectedCarParkMarker != null) currentSelectedCarParkMarker.remove();
            listener.setCarParking();
        }
    }

    public void nearestCarParkClicked(View view) {
        listener.showCarParkList();
    }

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

    public void initRouteMarker() {
        List<Feature> routeRedLightCameras = getRouteRedLightCamera(mapRepository.getRedLightCamera(), polylineFinal);
        List<Feature> routeSpeedCameras = getRouteSpeedCamera(mapRepository.getSpeedCamera(), polylineFinal);

        prefRepository.setCurrentRouteRedLightCamera(routeRedLightCameras);
        prefRepository.setCurrentRouteSpeedCamera(routeSpeedCameras);

        listener.setRouteCameras(routeRedLightCameras, routeSpeedCameras);
    }

    public void navigationClicked(View view) {
        if (isDoubleClicked(1500)) return;

        prefRepository.setNavigationStartLat((float) prefRepository.getCurrentLocation().latitude);
        prefRepository.setNavigationStartLng((float) prefRepository.getCurrentLocation().longitude);
        if (isUraCarPark(currentSelectedCarParkObj)) {
            prefRepository.setNavigationEndLat((float) currentSelectedUraCarPark.getLat());
            prefRepository.setNavigationEndLng((float) currentSelectedUraCarPark.getLng());
        } else {
            prefRepository.setNavigationEndLat((float) currentSelectedMyTransportCarPark.getCarParkLat());
            prefRepository.setNavigationEndLng((float) currentSelectedMyTransportCarPark.getCarParkLng());
        }
        listener.startNavigation();
    }

}
