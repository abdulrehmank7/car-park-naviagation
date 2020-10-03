package com.arkapp.carparknaviagation.ui.home;

import android.content.Context;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.data.models.carPark.AllCarPark;
import com.arkapp.carparknaviagation.data.models.carPark.AllCarParkAvailability;
import com.arkapp.carparknaviagation.data.models.carPark.CarPark;
import com.arkapp.carparknaviagation.data.models.carPark.CarParkAvailability;
import com.arkapp.carparknaviagation.data.models.carParking.Value;
import com.arkapp.carparknaviagation.data.models.redLightCamera.Feature;
import com.arkapp.carparknaviagation.utility.listeners.HomePageListener;
import com.arkapp.carparknaviagation.utility.maps.route.GetRouteTask;
import com.arkapp.carparknaviagation.utility.svy21.LatLonCoordinate;
import com.arkapp.carparknaviagation.utility.svy21.SVY21;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.arkapp.carparknaviagation.utility.ViewUtils.printLog;
import static com.arkapp.carparknaviagation.utility.maps.others.MapUtils.calculateDistanceBetweenPoints;
import static com.arkapp.carparknaviagation.utility.maps.others.MapUtils.fitRouteInScreen;
import static com.arkapp.carparknaviagation.utility.maps.others.MapUtils.getCustomMaker;
import static com.arkapp.carparknaviagation.utility.maps.others.MapUtils.getMapsApiDirectionsFromUrl;

/**
 * Created by Abdul Rehman on 27-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class Utils {

    public static void clearSearchTextListener(TextWatcher textWatcher, EditText editText) {
        editText.removeTextChangedListener(textWatcher);
    }

    public static void initSearchTextListener(TextWatcher textWatcher, EditText editText) {
        editText.addTextChangedListener(textWatcher);
    }

    public static Marker setLocationMarkerOnMap(double lat,
                                                double log,
                                                Marker marker,
                                                int iconDrawable,
                                                GoogleMap map,
                                                Context context) {
        //removing the old marker
        if (marker != null)
            marker.remove();

        Marker newMarker = map.addMarker(getCustomMaker(context, lat, log, iconDrawable));

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, log), 17));

        return newMarker;
    }

    public static ArrayList<CarParkAvailability> removeCarParkInvalid(double fromLat,
                                                                      double fromLng,
                                                                      double toLat,
                                                                      double toLng,
                                                                      AllCarPark carParkCharges,
                                                                      AllCarParkAvailability carParkAvailability) {

        final int MINIMUM_SLOTS = 1;
        final double MAXIMUM_SEARCH_RADIUS = 4000;

        List<CarPark> allCarParkCharges = carParkCharges.getResult();
        List<CarParkAvailability> allCarParkAvailability = carParkAvailability.getResult();
        final ArrayList<CarParkAvailability> carParkWithAvailableSlotsAndInRadius = new ArrayList<>();

        for (CarParkAvailability carPark : allCarParkAvailability) {

            if (carPark.getGeometries() == null) continue;

            if (TextUtils.isEmpty(carPark.getLotsAvailable())) continue;

            //removing car park with less than minimum slots
            if (Integer.parseInt(carPark.getLotsAvailable()) < MINIMUM_SLOTS) continue;

            for (CarPark charges : allCarParkCharges) {

                if (charges.getPpCode().equals(carPark.getCarparkNo()) &&
                        charges.getVehCat().equals("Car") &&
                        !carParkWithAvailableSlotsAndInRadius.contains(carPark)) {

                    String[] location = carPark.getGeometries().get(0).getCoordinates().split(",");
                    LatLonCoordinate lanLang = SVY21.computeLatLon(Double.parseDouble(location[1]), Double.parseDouble(location[0]));
                    carPark.setLat(lanLang.getLatitude());
                    carPark.setLng(lanLang.getLongitude());

                    carPark.setCharges(charges);
                    carPark.setDistanceFromDestination(
                            calculateDistanceBetweenPoints(
                                    carPark.getLat(),
                                    carPark.getLng(),
                                    toLat,
                                    toLng));

                    carPark.setDistanceFromOrigin(
                            calculateDistanceBetweenPoints(
                                    carPark.getLat(),
                                    carPark.getLng(),
                                    fromLat,
                                    fromLng));

                    if (carPark.getDistanceFromDestination() <= MAXIMUM_SEARCH_RADIUS)
                        carParkWithAvailableSlotsAndInRadius.add(carPark);
                    break;
                }
            }
        }

        return carParkWithAvailableSlotsAndInRadius;
    }

    public static ArrayList<CarParkAvailability> filterCarPark(
            ArrayList<CarParkAvailability> validCarParks) {

        final double MAXIMUM_SEARCH_RADIUS = 3000;

        final ArrayList<CarParkAvailability> carParkWithEta = new ArrayList<>();
        final ArrayList<CarParkAvailability> carParkDistanceChecked = new ArrayList<>();
        final ArrayList<CarParkAvailability> finalCarParks = new ArrayList<>();

        for (CarParkAvailability validCarPark : validCarParks) {
            validCarPark.setDistanceFromOrigin(Double.parseDouble(validCarPark.getEtaDistanceFromOrigin().getDistance().getValue()));
            validCarPark.setDistanceFromDestination(Double.parseDouble(validCarPark.getEtaDistanceFromDestination().getDistance().getValue()));
            carParkWithEta.add(validCarPark);
        }

        for (CarParkAvailability carpark : carParkWithEta) {
            if (carpark.getDistanceFromDestination() <= MAXIMUM_SEARCH_RADIUS) {
                carParkDistanceChecked.add(carpark);
            }
        }

        final ArrayList<CarParkAvailability> distanceCarParkRank = new ArrayList<>(carParkDistanceChecked);
        final ArrayList<CarParkAvailability> emptyCarParkRank = new ArrayList<>(carParkDistanceChecked);
        final ArrayList<CarParkAvailability> originDistanceCarParkRank = new ArrayList<>(carParkDistanceChecked);
        final ArrayList<CarParkAvailability> etaCarParkRank = new ArrayList<>(carParkDistanceChecked);

        Collections.sort(distanceCarParkRank, (c1, c2) -> Double.compare(c1.getDistanceFromDestination(), c2.getDistanceFromDestination()));
        Collections.sort(originDistanceCarParkRank, (c1, c2) -> Double.compare(c1.getDistanceFromOrigin(), c2.getDistanceFromOrigin()));
        Collections.sort(emptyCarParkRank, (c1, c2) -> Integer.compare(Integer.parseInt(c2.getLotsAvailable()), Integer.parseInt(c1.getLotsAvailable())));
        Collections.sort(etaCarParkRank, (c1, c2) -> Double.compare(Double.parseDouble(c1.getEtaDistanceFromOrigin().getDuration().getValue()), Double.parseDouble(c2.getEtaDistanceFromOrigin().getDuration().getValue())));

        for (CarParkAvailability carPark : carParkDistanceChecked) {
            int sequencePriority = distanceCarParkRank.indexOf(carPark) +
                    emptyCarParkRank.indexOf(carPark) +
                    originDistanceCarParkRank.indexOf(carPark) +
                    etaCarParkRank.indexOf(carPark);

            carPark.setSequencePriority(sequencePriority);
            finalCarParks.add(carPark);
        }

        Collections.sort(finalCarParks, (c1, c2) -> c1.getSequencePriority().compareTo(c2.getSequencePriority()));

        for (CarParkAvailability carPark : finalCarParks) {
            printLog("----------------------------------------");
            printLog("location: " + carPark.getGeometries().get(0).getCoordinates());
            printLog("name: " + carPark.getCharges().getPpName());
            printLog("slots: " + carPark.getLotsAvailable());
            printLog("priority " + carPark.getSequencePriority());
            printLog("destination distance " + carPark.getDistanceFromDestination());
            printLog("origin distance " + carPark.getDistanceFromOrigin());
        }

        return finalCarParks;
    }

    public static ArrayList<CarParkAvailability> filterCarPark2(double fromLat,
                                                                double fromLng,
                                                                double toLat,
                                                                double toLng,
                                                                AllCarPark carParkCharges,
                                                                AllCarParkAvailability carParkAvailability) {

        final int MINIMUM_SLOTS = 1;
        final double MAXIMUM_SEARCH_RADIUS = 3000;

        List<CarPark> allCarParkCharges = carParkCharges.getResult();
        List<CarParkAvailability> allCarParkAvailability = carParkAvailability.getResult();
        final ArrayList<CarParkAvailability> carParkWithAvailableSlotsAndInRadius = new ArrayList<>();
        final ArrayList<CarParkAvailability> finalCarParks = new ArrayList<>();

        for (CarParkAvailability carPark : allCarParkAvailability) {

            if (carPark.getGeometries() == null) continue;

            if (TextUtils.isEmpty(carPark.getLotsAvailable())) continue;

            //removing car park with less than minimum slots
            if (Integer.parseInt(carPark.getLotsAvailable()) < MINIMUM_SLOTS) continue;

            for (CarPark charges : allCarParkCharges) {

                if (charges.getPpCode().equals(carPark.getCarparkNo()) &&
                        charges.getVehCat().equals("Car") &&
                        !carParkWithAvailableSlotsAndInRadius.contains(carPark)) {

                    String[] location = carPark.getGeometries().get(0).getCoordinates().split(",");
                    LatLonCoordinate lanLang = SVY21.computeLatLon(Double.parseDouble(location[1]), Double.parseDouble(location[0]));
                    carPark.setLat(lanLang.getLatitude());
                    carPark.setLng(lanLang.getLongitude());

                    carPark.setCharges(charges);

                    carPark.setDistanceFromDestination(
                            calculateDistanceBetweenPoints(
                                    carPark.getLat(),
                                    carPark.getLng(),
                                    toLat,
                                    toLng));

                    carPark.setDistanceFromOrigin(
                            calculateDistanceBetweenPoints(
                                    carPark.getLat(),
                                    carPark.getLng(),
                                    fromLat,
                                    fromLng));

                    if (carPark.getDistanceFromDestination() <= MAXIMUM_SEARCH_RADIUS)
                        carParkWithAvailableSlotsAndInRadius.add(carPark);
                    break;
                }
            }
        }

        final ArrayList<CarParkAvailability> distanceCarParkRank = new ArrayList<>(carParkWithAvailableSlotsAndInRadius);
        final ArrayList<CarParkAvailability> emptyCarParkRank = new ArrayList<>(carParkWithAvailableSlotsAndInRadius);
        final ArrayList<CarParkAvailability> etaCarParkRank = new ArrayList<>(carParkWithAvailableSlotsAndInRadius);

        Collections.sort(distanceCarParkRank, (c1, c2) -> Double.compare(c1.getDistanceFromDestination(), c2.getDistanceFromDestination()));
        Collections.sort(emptyCarParkRank, (c1, c2) -> Integer.compare(Integer.parseInt(c2.getLotsAvailable()), Integer.parseInt(c1.getLotsAvailable())));
        Collections.sort(etaCarParkRank, (c1, c2) -> Double.compare(c1.getDistanceFromOrigin(), c2.getDistanceFromOrigin()));

        for (CarParkAvailability carPark : carParkWithAvailableSlotsAndInRadius) {
            int sequencePriority = distanceCarParkRank.indexOf(carPark) +
                    emptyCarParkRank.indexOf(carPark) +
                    etaCarParkRank.indexOf(carPark);

            carPark.setSequencePriority(sequencePriority);
            finalCarParks.add(carPark);
        }

        Collections.sort(finalCarParks, (c1, c2) -> c1.getSequencePriority().compareTo(c2.getSequencePriority()));

        for (CarParkAvailability carPark : finalCarParks) {
            printLog("----------------------------------------");
            printLog("location: " + carPark.getGeometries().get(0).getCoordinates());
            printLog("name: " + carPark.getCharges().getPpName());
            printLog("slots: " + carPark.getLotsAvailable());
            printLog("priority " + carPark.getSequencePriority());
            printLog("destination distance " + carPark.getDistanceFromDestination());
            printLog("origin distance " + carPark.getDistanceFromOrigin());
        }

        return finalCarParks;
    }

    public static ArrayList<MarkerOptions> getCarParkMarkers(Context context,
                                                             ArrayList<Value> carParkings) {
        ArrayList<MarkerOptions> carParkMarkers = new ArrayList<>();

        for (Value carPark : carParkings) {
            carParkMarkers.add(getCustomMaker(context,
                                              carPark.getCarParkLat(),
                                              carPark.getCarParkLng(),
                                              R.drawable.ic_parking_marker));
        }

        return carParkMarkers;
    }

    public static void drawMapRoute(double fromLat,
                                    double fromLng,
                                    double toLat,
                                    double toLng,
                                    double destinationLat,
                                    double destinationLng,
                                    Context context,
                                    GoogleMap map,
                                    HomePageListener listener,
                                    Polyline polylineFinal) {
        fitRouteInScreen(map,
                         new LatLng(fromLat, fromLng),
                         new LatLng(toLat, toLng),
                         new LatLng(destinationLat, destinationLng),
                         context);

        String url = getMapsApiDirectionsFromUrl(
                fromLat + "",
                fromLng + "",
                toLat + "",
                toLng + "",
                ""/*avoid=tolls*/);

        printLog("polyline map url = " + url);

        if (!url.equals("")) {
            if (polylineFinal != null) {
                polylineFinal.remove();
            }
            int colourForPathPlot = context.getResources().getColor(R.color.colorPrimaryCustomDark);

            GetRouteTask downloadTask = new GetRouteTask(colourForPathPlot, listener);
            downloadTask.execute(url);
        }
    }

    public static List<Feature> getRouteRedLightCamera(List<Feature> cameras,
                                                       Polyline polylineFinal) {
        final int minimumDistance = 100;
        ArrayList<Feature> routeCamera = new ArrayList<>();

        for (Feature camera : cameras) {
            for (LatLng latLng : polylineFinal.getPoints()) {
                float dist = calculateDistanceBetweenPoints(
                        latLng.latitude,
                        latLng.longitude,
                        camera.getGeometry().getCoordinates().get(1),
                        camera.getGeometry().getCoordinates().get(0));
                if (dist < minimumDistance) {
                    printLog("added red light camera " + camera.getGeometry().getCoordinates().get(1) +
                                     " " +
                                     camera.getGeometry().getCoordinates().get(0) + " dist " + dist);
                    if (!routeCamera.contains(camera)) {
                        routeCamera.add(camera);
                        break;
                    }
                }
            }
        }

        return routeCamera;
    }

    public static List<Feature> getRouteSpeedCamera(List<Feature> cameras, Polyline polylineFinal) {
        final int minimumDistance = 500;
        ArrayList<Feature> routeCamera = new ArrayList<>();

        for (Feature camera : cameras) {
            for (LatLng latLng : polylineFinal.getPoints()) {
                float dist = calculateDistanceBetweenPoints(
                        latLng.latitude,
                        latLng.longitude,
                        camera.getGeometry().getCoordinates().get(1),
                        camera.getGeometry().getCoordinates().get(0));
                if (dist < minimumDistance) {
                    printLog("added speed camera " + camera.getGeometry().getCoordinates().get(1) +
                                     " " +
                                     camera.getGeometry().getCoordinates().get(0) + " dist " + dist);
                    if (!routeCamera.contains(camera)) {
                        routeCamera.add(camera);
                        break;
                    }
                }
            }
        }

        return routeCamera;
    }

    public static ArrayList<MarkerOptions> getRedLightMarker(List<Feature> cameras,
                                                             Context context) {

        ArrayList<MarkerOptions> redLightMarkers = new ArrayList<>();
        for (Feature feature : cameras) {
            redLightMarkers.add(getCustomMaker(context,
                                               feature.getGeometry().getCoordinates().get(1),
                                               feature.getGeometry().getCoordinates().get(0),
                                               R.drawable.ic_red_camera));
        }

        return redLightMarkers;
    }

    public static ArrayList<MarkerOptions> getSpeedMarker(List<Feature> cameras, Context context) {

        ArrayList<MarkerOptions> redLightMarkers = new ArrayList<>();
        for (Feature feature : cameras) {
            redLightMarkers.add(getCustomMaker(context,
                                               feature.getGeometry().getCoordinates().get(1),
                                               feature.getGeometry().getCoordinates().get(0),
                                               R.drawable.ic_speed_camera));
        }

        return redLightMarkers;
    }
}
