package com.arkapp.carparknaviagation.viewModels;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.data.models.carPark.AllCarPark;
import com.arkapp.carparknaviagation.data.models.carPark.AllCarParkAvailability;
import com.arkapp.carparknaviagation.data.models.carPark.CarParkAvailability;
import com.arkapp.carparknaviagation.data.models.eta.Eta;
import com.arkapp.carparknaviagation.data.models.redLightCamera.Feature;
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

import static com.arkapp.carparknaviagation.ui.home.Utils.filterCarPark;
import static com.arkapp.carparknaviagation.ui.home.Utils.getRouteRedLightCamera;
import static com.arkapp.carparknaviagation.ui.home.Utils.getRouteSpeedCamera;
import static com.arkapp.carparknaviagation.ui.home.Utils.removeCarParkInvalid;
import static com.arkapp.carparknaviagation.utility.Constants.API_TOKEN_REFRESH_HOUR;
import static com.arkapp.carparknaviagation.utility.ViewUtils.isDoubleClicked;
import static com.arkapp.carparknaviagation.utility.ViewUtils.printLog;
import static com.arkapp.carparknaviagation.utility.maps.eta.Utils.getCarParkLatLng;

/**
 * Created by Abdul Rehman on 27-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class HomePageViewModel extends ViewModel {

    public PrefRepository prefRepository;
    public ArrayList<CarParkAvailability> allFilteredCarPark = new ArrayList<>();
    public AllCarParkAvailability allCarParkAvailability;
    public AllCarPark allCarParkCharges;
    public ArrayList<Marker> redLightMarkers = new ArrayList();
    public ArrayList<Marker> speedCameraMarkers = new ArrayList();
    public Marker dropLocationMarker;
    public Marker currentSelectedCarParkMarker;
    public int currentSelectedCarParkNo = 0;

    public Place selectedAddress;
    public CarParkAvailability currentSelectedCarPark;
    public Marker currentLocationMarker;
    public Polyline polylineFinal = null;
    public BottomSheetDialog carParkList;
    public String selectedAddressName;
    private MapRepository mapRepository;
    private CarParkRepository carParkRepository = new CarParkRepository();
    public HomePageListener listener;
    private ArrayList<CarParkAvailability> validCarPark;
    public SearchLocationAdapter searchAdapter;
    public boolean isEditMode = false;

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

   /* public MutableLiveData<CarParking> getCarParkData() {
        return carParkRepository.getCarParkAvailability();
    }*/

    public MutableLiveData<String> getCarParkToken() {
        return carParkRepository.getCarParkApiToken();
    }

    /*public MutableLiveData<AllCarPark> getCarParkCharges() {
        return carParkRepository.getAllCarParkCharges(prefRepository.getApiToken());
    }*/

    public MutableLiveData<AllCarParkAvailability> getCarParkAvailability() {
        return carParkRepository.getAllCarParkAvailability(prefRepository.getApiToken());
    }

    public MutableLiveData<Eta> getCarParkEtaFromOrigin(ArrayList<String> allCarParkLatLng,
                                                        String fromLat, String fromLng) {
        return carParkRepository.getCarParkEta(allCarParkLatLng, fromLat, fromLng);
    }

    public MutableLiveData<Eta> getCarParkEtaFromDestination(ArrayList<String> allCarParkLatLng,
                                                             String toLat, String toLng) {
        return carParkRepository.getCarParkEta(allCarParkLatLng, toLat, toLng);
    }

    private void initAllCarParkCharges() {
        allCarParkCharges = mapRepository.getAllCarParkCharges();
    }

    /*public void initRedLightCamera() {
        listener.setRedLightCamera(mapRepository.getRedLightMarkers());
    }*/

    public void initCarParkMarker() {

        //removing old markers
        if (currentSelectedCarParkMarker != null)
            currentSelectedCarParkMarker.remove();

        validCarPark = removeCarParkInvalid(prefRepository.getCurrentLocation().latitude,
                                            prefRepository.getCurrentLocation().longitude,
                                            selectedAddress.getLatLng().latitude,
                                            selectedAddress.getLatLng().longitude,
                                            allCarParkCharges,
                                            allCarParkAvailability);

        if (validCarPark.size() > 0)
            listener.setCarParkEtaFromOrigin(
                    getCarParkEtaFromOrigin(getCarParkLatLng(validCarPark),
                                            prefRepository.getCurrentLocation().latitude + "",
                                            prefRepository.getCurrentLocation().longitude + ""));
    }

    public void addEtaInCarParkFromDestination(Eta data) {

        int count = 0;
        ArrayList<CarParkAvailability> carParkWithEta = new ArrayList<>();
        for (CarParkAvailability carParkAvailability : validCarPark) {
            carParkAvailability.setEtaDistanceFromDestination(data.getRows().get(0).getElements().get(count));
            carParkWithEta.add(carParkAvailability);
            count++;
        }
        validCarPark = carParkWithEta;
        allFilteredCarPark = filterCarPark(validCarPark);
        listener.setCarParking();
    }

    public void addEtaInCarParkFromOrigin(Eta data) {
        int count = 0;
        ArrayList<CarParkAvailability> carParkWithEta = new ArrayList<>();
        for (CarParkAvailability carParkAvailability : validCarPark) {
            carParkAvailability.setEtaDistanceFromOrigin(data.getRows().get(0).getElements().get(count));
            carParkWithEta.add(carParkAvailability);
            count++;
        }
        validCarPark = carParkWithEta;

        listener.setCarParkEtaFromDestination(
                getCarParkEtaFromDestination(getCarParkLatLng(validCarPark),
                                             selectedAddress.getLatLng().latitude + "",
                                             selectedAddress.getLatLng().longitude + ""));
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
        printLog("toUpdateToken " + currentTime.after(refreshTime));
        return currentTime.after(refreshTime);
    }

    public void initRouteMarker() {
        List<Feature> routeRedLightCameras = getRouteRedLightCamera(mapRepository.getRedLightCamera(), polylineFinal);
        List<Feature> routeSpeedCameras = getRouteSpeedCamera(mapRepository.getSpeedCamera(), polylineFinal);

        listener.setRouteCameras(routeRedLightCameras, routeSpeedCameras);
    }

}
