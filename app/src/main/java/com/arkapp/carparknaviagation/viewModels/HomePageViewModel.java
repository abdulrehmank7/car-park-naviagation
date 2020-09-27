package com.arkapp.carparknaviagation.viewModels;

import androidx.lifecycle.ViewModel;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.data.repository.MapRepository;
import com.arkapp.carparknaviagation.utility.listeners.HomePageListener;
import com.arkapp.carparknaviagation.utility.maps.search.SearchLocationAdapter;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;

import static com.arkapp.carparknaviagation.ui.main.MainActivity.currentLocation;

/**
 * Created by Abdul Rehman on 27-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class HomePageViewModel extends ViewModel {

    public static Polyline polylineFinal = null;
    public Place selectedAddress;
    public Marker currentLocationMarker;
    public String selectedAddressName;
    public Marker dropLocationMarker;
    public HomePageListener listener;
    public SearchLocationAdapter searchAdapter;
    public ArrayList<Marker> redLightMarkers = new ArrayList();

    public boolean isEditMode = false;
    private MapRepository repository;

    public HomePageViewModel(MapRepository repository) {
        this.repository = repository;
    }

    public void initCurrentMarker() {
        if (currentLocation != null) {

            selectedAddressName = repository.getLocationAddress(currentLocation.getLatitude(), currentLocation.getLongitude());
            listener.setCurrentLocationMarker(repository.getLocationMarkerOption(currentLocation.getLatitude(),
                                                                                 currentLocation.getLongitude(),
                                                                                 R.drawable.ic_marker1));
        }
    }

    public void initRedLightCamera() {
        listener.setRedLightCamera(repository.getRedLightMarkers());
    }


}
