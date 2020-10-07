package com.arkapp.carparknaviagation.ui.home;

import android.content.Context;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.data.models.CustomCarPark;
import com.arkapp.carparknaviagation.data.models.myTransportCarPark.MyTransportCarPark;
import com.arkapp.carparknaviagation.data.models.myTransportCarPark.MyTransportCarParkAvailability;
import com.arkapp.carparknaviagation.data.models.rates.CarParkCharges;
import com.arkapp.carparknaviagation.data.models.rates.CarParkInformation;
import com.arkapp.carparknaviagation.data.models.redLightCamera.Feature;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCarPark;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCarParkAvailability;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCarParkCharges;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCharges;
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

import static com.arkapp.carparknaviagation.utility.Constants.CENTRAL_AREA;
import static com.arkapp.carparknaviagation.utility.Constants.CHARGES_OUTSIDE_CENTRAL_AREA;
import static com.arkapp.carparknaviagation.utility.Constants.CHARGES_WITHIN_CENTRAL_AREA;
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

    public static ArrayList<UraCarParkAvailability> removeInvalidUraCarPark(double fromLat,
                                                                            double fromLng,
                                                                            double toLat,
                                                                            double toLng,
                                                                            UraCarParkCharges carParkCharges,
                                                                            UraCarPark carParkAvailability) {

        final int MINIMUM_SLOTS = 1;
        final double MAXIMUM_SEARCH_RADIUS = 3000;

        List<UraCharges> allUraChargesCharges = carParkCharges.getResult();
        List<UraCarParkAvailability> allUraCarParkAvailability = carParkAvailability.getResult();
        final ArrayList<UraCarParkAvailability> carParkWithAvailableSlotsAndInRadius = new ArrayList<>();

        for (UraCarParkAvailability carPark : allUraCarParkAvailability) {

            if (carPark.getGeometries() == null) continue;

            if (TextUtils.isEmpty(carPark.getLotsAvailable())) continue;

            //removing car park with less than minimum slots
            if (Integer.parseInt(carPark.getLotsAvailable()) < MINIMUM_SLOTS) continue;

            for (UraCharges charges : allUraChargesCharges) {

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

    public static ArrayList<MyTransportCarParkAvailability> removeInvalidMyTransportCarPark(
            double fromLat,
            double fromLng,
            double toLat,
            double toLng,
            MyTransportCarPark carParkAvailability,
            ArrayList<CarParkInformation> allHdbCarParkCharges) {

        final int MINIMUM_SLOTS = 1;
        final double MAXIMUM_SEARCH_RADIUS = 3000;

        List<MyTransportCarParkAvailability> allMyTransportCarParkAvailability = carParkAvailability.getMyTransportCarParkAvailability();
        final ArrayList<MyTransportCarParkAvailability> carParkWithAvailableSlotsAndInRadius = new ArrayList<>();

        for (MyTransportCarParkAvailability availability : allMyTransportCarParkAvailability) {

            if (TextUtils.isEmpty(availability.getLocation())) continue;

            if (availability.getAvailableLots() == null) continue;

            if (TextUtils.isEmpty(availability.getAgency()) || !availability.getAgency().equals("HDB"))
                continue;

            //removing car park with less than minimum slots
            if (availability.getAvailableLots() < MINIMUM_SLOTS) continue;

            String[] location = availability.getLocation().split(" ");
            double carParkLat = Double.parseDouble(location[0]);
            double carParkLng = Double.parseDouble(location[1]);

            availability.setCarParkLat(carParkLat);
            availability.setCarParkLng(carParkLng);

            availability.setDistanceFromDestination(
                    calculateDistanceBetweenPoints(
                            carParkLat,
                            carParkLng,
                            toLat,
                            toLng));

            availability.setDistanceFromOrigin(
                    calculateDistanceBetweenPoints(
                            carParkLat,
                            carParkLng,
                            fromLat,
                            fromLng));

            if (availability.getDistanceFromDestination() <= MAXIMUM_SEARCH_RADIUS) {

                for (String address : CENTRAL_AREA) {
                    if (availability.getDevelopment().toLowerCase().contains(address.toLowerCase())) {
                        CarParkCharges newCharges = new CarParkCharges();
                        newCharges.setWeekDaysRate1(CHARGES_WITHIN_CENTRAL_AREA);
                        availability.setCharges(newCharges);
                        availability.setChargeValue("$2.40/hr");
                        break;
                    }
                }
                if (availability.getCharges() == null || TextUtils.isEmpty(availability.getCharges().getWeekDaysRate1())) {
                    CarParkCharges newCharges = new CarParkCharges();
                    newCharges.setWeekDaysRate1(CHARGES_OUTSIDE_CENTRAL_AREA);
                    availability.setCharges(newCharges);
                    availability.setChargeValue("$1.20/hr");
                }


                int counter = 0;
                for (CarParkInformation info : allHdbCarParkCharges) {
                    counter++;
                    if (info.getCarParkNo().equals(availability.getCarParkID()) ||
                            info.getAddress().trim().equalsIgnoreCase(availability.getDevelopment().trim())) {
                        availability.setInformation(info);
                        carParkWithAvailableSlotsAndInRadius.add(availability);
                        break;
                    }

                    if (counter == allHdbCarParkCharges.size())
                        carParkWithAvailableSlotsAndInRadius.add(availability);
                }
            }
        }

        return carParkWithAvailableSlotsAndInRadius;
    }

    public static ArrayList<UraCarParkAvailability> filterUraCarPark(
            ArrayList<UraCarParkAvailability> validCarParks) {

        final double MAXIMUM_SEARCH_RADIUS = 3000;

        final ArrayList<UraCarParkAvailability> carParkWithEta = new ArrayList<>();
        final ArrayList<UraCarParkAvailability> carParkDistanceChecked = new ArrayList<>();
        final ArrayList<UraCarParkAvailability> finalCarParks = new ArrayList<>();

        for (UraCarParkAvailability validCarPark : validCarParks) {
            validCarPark.setDistanceFromOrigin(Double.parseDouble(validCarPark.getEtaDistanceFromOrigin().getDistance().getValue()));
            validCarPark.setDistanceFromDestination(Double.parseDouble(validCarPark.getEtaDistanceFromDestination().getDistance().getValue()));
            carParkWithEta.add(validCarPark);
        }

        for (UraCarParkAvailability carpark : carParkWithEta) {
            if (carpark.getDistanceFromDestination() <= MAXIMUM_SEARCH_RADIUS) {
                carParkDistanceChecked.add(carpark);
            }
        }

        final ArrayList<UraCarParkAvailability> distanceCarParkRank = new ArrayList<>(carParkDistanceChecked);
        final ArrayList<UraCarParkAvailability> emptyCarParkRank = new ArrayList<>(carParkDistanceChecked);
        final ArrayList<UraCarParkAvailability> originDistanceCarParkRank = new ArrayList<>(carParkDistanceChecked);
        final ArrayList<UraCarParkAvailability> etaCarParkRank = new ArrayList<>(carParkDistanceChecked);

        Collections.sort(distanceCarParkRank, (c1, c2) -> Double.compare(c1.getDistanceFromDestination(), c2.getDistanceFromDestination()));
        Collections.sort(originDistanceCarParkRank, (c1, c2) -> Double.compare(c1.getDistanceFromOrigin(), c2.getDistanceFromOrigin()));
        Collections.sort(emptyCarParkRank, (c1, c2) -> Integer.compare(Integer.parseInt(c2.getLotsAvailable()), Integer.parseInt(c1.getLotsAvailable())));
        Collections.sort(etaCarParkRank, (c1, c2) -> Double.compare(Double.parseDouble(c1.getEtaDistanceFromOrigin().getDuration().getValue()), Double.parseDouble(c2.getEtaDistanceFromOrigin().getDuration().getValue())));

        for (UraCarParkAvailability carPark : carParkDistanceChecked) {
            int sequencePriority = distanceCarParkRank.indexOf(carPark) +
                    emptyCarParkRank.indexOf(carPark) +
                    originDistanceCarParkRank.indexOf(carPark) +
                    etaCarParkRank.indexOf(carPark);

            carPark.setSequencePriority(sequencePriority);
            finalCarParks.add(carPark);
        }

        Collections.sort(finalCarParks, (c1, c2) -> c1.getSequencePriority().compareTo(c2.getSequencePriority()));

        for (UraCarParkAvailability carPark : finalCarParks) {
            printLog("----------------Ura---------------");
            printLog("location: " + carPark.getGeometries().get(0).getCoordinates());
            printLog("name: " + carPark.getCharges().getPpName());
            printLog("slots: " + carPark.getLotsAvailable());
            printLog("priority " + carPark.getSequencePriority());
            printLog("destination distance " + carPark.getDistanceFromDestination());
            printLog("origin distance " + carPark.getDistanceFromOrigin());
        }

        return finalCarParks;
    }


    public static ArrayList<MyTransportCarParkAvailability> filterMyTransportCarPark(
            ArrayList<MyTransportCarParkAvailability> validCarParks) {

        final double MAXIMUM_SEARCH_RADIUS = 3000;

        final ArrayList<MyTransportCarParkAvailability> carParkWithEta = new ArrayList<>();
        final ArrayList<MyTransportCarParkAvailability> carParkDistanceChecked = new ArrayList<>();
        final ArrayList<MyTransportCarParkAvailability> finalCarParks = new ArrayList<>();

        for (MyTransportCarParkAvailability validCarPark : validCarParks) {
            validCarPark.setDistanceFromOrigin(Double.parseDouble(validCarPark.getEtaDistanceFromOrigin().getDistance().getValue()));
            validCarPark.setDistanceFromDestination(Double.parseDouble(validCarPark.getEtaDistanceFromDestination().getDistance().getValue()));
            carParkWithEta.add(validCarPark);
        }

        for (MyTransportCarParkAvailability carpark : carParkWithEta) {
            if (carpark.getDistanceFromDestination() <= MAXIMUM_SEARCH_RADIUS) {
                carParkDistanceChecked.add(carpark);
            }
        }

        final ArrayList<MyTransportCarParkAvailability> distanceCarParkRank = new ArrayList<>(carParkDistanceChecked);
        final ArrayList<MyTransportCarParkAvailability> emptyCarParkRank = new ArrayList<>(carParkDistanceChecked);
        final ArrayList<MyTransportCarParkAvailability> originDistanceCarParkRank = new ArrayList<>(carParkDistanceChecked);
        final ArrayList<MyTransportCarParkAvailability> etaCarParkRank = new ArrayList<>(carParkDistanceChecked);

        Collections.sort(distanceCarParkRank, (c1, c2) -> Double.compare(c1.getDistanceFromDestination(), c2.getDistanceFromDestination()));
        Collections.sort(originDistanceCarParkRank, (c1, c2) -> Double.compare(c1.getDistanceFromOrigin(), c2.getDistanceFromOrigin()));
        Collections.sort(emptyCarParkRank, (c1, c2) -> Integer.compare(c2.getAvailableLots(), c1.getAvailableLots()));
        Collections.sort(etaCarParkRank, (c1, c2) -> Double.compare(Double.parseDouble(c1.getEtaDistanceFromOrigin().getDuration().getValue()), Double.parseDouble(c2.getEtaDistanceFromOrigin().getDuration().getValue())));

        for (MyTransportCarParkAvailability carPark : carParkDistanceChecked) {
            int sequencePriority = distanceCarParkRank.indexOf(carPark) +
                    emptyCarParkRank.indexOf(carPark) +
                    originDistanceCarParkRank.indexOf(carPark) +
                    etaCarParkRank.indexOf(carPark);

            carPark.setSequencePriority(sequencePriority);
            finalCarParks.add(carPark);
        }

        Collections.sort(finalCarParks, (c1, c2) -> c1.getSequencePriority().compareTo(c2.getSequencePriority()));

        for (MyTransportCarParkAvailability carPark : finalCarParks) {
            printLog("------------My Transport----------------");
            printLog("location: " + carPark.getLocation());
            printLog("name: " + carPark.getCharges().getWeekDaysRate1());
            printLog("slots: " + carPark.getAvailableLots());
            printLog("priority " + carPark.getSequencePriority());
            printLog("destination distance " + carPark.getDistanceFromDestination());
            printLog("origin distance " + carPark.getDistanceFromOrigin());
        }

        return finalCarParks;
    }

    public static ArrayList<Object> filterAllCarPark(
            ArrayList<UraCarParkAvailability> validUraCarParks,
            ArrayList<MyTransportCarParkAvailability> validMyTransportCarParks) {

        final double MAXIMUM_SEARCH_RADIUS = 3000;

        final ArrayList<Object> allCarPark = new ArrayList<>();
        allCarPark.addAll(validUraCarParks);
        allCarPark.addAll(validMyTransportCarParks);

        final ArrayList<Object> carParkWithEta = new ArrayList<>();
        final ArrayList<Object> carParkDistanceChecked = new ArrayList<>();
        final ArrayList<Object> finalCarParks = new ArrayList<>();


        for (Object carPark : allCarPark) {
            if (isUraCarPark(carPark)) {
                UraCarParkAvailability data = (UraCarParkAvailability) carPark;
                data.setDistanceFromOrigin(Double.parseDouble(data.getEtaDistanceFromOrigin().getDistance().getValue()));
                data.setDistanceFromDestination(Double.parseDouble(data.getEtaDistanceFromDestination().getDistance().getValue()));
                carParkWithEta.add(data);
            } else {
                MyTransportCarParkAvailability data = (MyTransportCarParkAvailability) carPark;
                data.setDistanceFromOrigin(Double.parseDouble(data.getEtaDistanceFromOrigin().getDistance().getValue()));
                data.setDistanceFromDestination(Double.parseDouble(data.getEtaDistanceFromDestination().getDistance().getValue()));
                carParkWithEta.add(data);
            }
        }

        for (Object carPark : carParkWithEta) {
            if (isUraCarPark(carPark)) {
                UraCarParkAvailability data = (UraCarParkAvailability) carPark;
                if (data.getDistanceFromDestination() <= MAXIMUM_SEARCH_RADIUS) {
                    carParkDistanceChecked.add(data);
                }
            } else {
                MyTransportCarParkAvailability data = (MyTransportCarParkAvailability) carPark;
                if (data.getDistanceFromDestination() <= MAXIMUM_SEARCH_RADIUS) {
                    carParkDistanceChecked.add(data);
                }
            }
        }

        ArrayList<CustomCarPark> sortingList = new ArrayList<>();
        for (Object carpark : carParkDistanceChecked) {
            if (isUraCarPark(carpark)) {
                UraCarParkAvailability data = (UraCarParkAvailability) carpark;
                CustomCarPark customData = new CustomCarPark();
                customData.setId(data.getCarparkNo());
                customData.setAvailableLots(Integer.parseInt(data.getLotsAvailable()));
                customData.setDistanceFromDestination(data.getDistanceFromDestination());
                customData.setDistanceFromOrigin(data.getDistanceFromOrigin());
                customData.setEtaDistanceFromOrigin(data.getEtaDistanceFromOrigin());
                sortingList.add(customData);
            } else {
                MyTransportCarParkAvailability data = (MyTransportCarParkAvailability) carpark;
                CustomCarPark customData = new CustomCarPark();
                customData.setId(data.getCarParkID());
                customData.setAvailableLots(data.getAvailableLots());
                customData.setDistanceFromDestination(data.getDistanceFromDestination());
                customData.setDistanceFromOrigin(data.getDistanceFromOrigin());
                customData.setEtaDistanceFromOrigin(data.getEtaDistanceFromOrigin());
                sortingList.add(customData);
            }
        }


        final ArrayList<CustomCarPark> distanceCarParkRank = new ArrayList<>(sortingList);
        final ArrayList<CustomCarPark> emptyCarParkRank = new ArrayList<>(sortingList);
        final ArrayList<CustomCarPark> originDistanceCarParkRank = new ArrayList<>(sortingList);
        final ArrayList<CustomCarPark> etaCarParkRank = new ArrayList<>(sortingList);

        Collections.sort(distanceCarParkRank, (c1, c2) -> Double.compare(c1.getDistanceFromDestination(), c2.getDistanceFromDestination()));
        Collections.sort(originDistanceCarParkRank, (c1, c2) -> Double.compare(c1.getDistanceFromOrigin(), c2.getDistanceFromOrigin()));
        Collections.sort(emptyCarParkRank, (c1, c2) -> Integer.compare(c2.getAvailableLots(), c1.getAvailableLots()));
        Collections.sort(etaCarParkRank, (c1, c2) -> Double.compare(Double.parseDouble(c1.getEtaDistanceFromOrigin().getDuration().getValue()), Double.parseDouble(c2.getEtaDistanceFromOrigin().getDuration().getValue())));

        final ArrayList<CustomCarPark> finalSortingList = new ArrayList<>();

        for (CustomCarPark carPark : sortingList) {
            int sequencePriority = distanceCarParkRank.indexOf(carPark) +
                    emptyCarParkRank.indexOf(carPark) +
                    originDistanceCarParkRank.indexOf(carPark) +
                    etaCarParkRank.indexOf(carPark);
            carPark.setSequencePriority(sequencePriority);
            finalSortingList.add(carPark);
        }

        Collections.sort(finalSortingList, (c1, c2) -> c1.getSequencePriority().compareTo(c2.getSequencePriority()));

        for (CustomCarPark customCarPark : finalSortingList) {
            printLog("-------------------------------");
            printLog("customCarPark SequencePriority " + customCarPark.getSequencePriority());
            printLog("customCarPark id " + customCarPark.getId());

            for (Object carPark : carParkDistanceChecked) {
                if (isUraCarPark(carPark)) {
                    UraCarParkAvailability data = (UraCarParkAvailability) carPark;
                    if (customCarPark.getId().equals(data.getCarparkNo())) {
                        data.setSequencePriority(customCarPark.getSequencePriority());
                        if (!finalCarParks.contains(data)) {
                            printLog("setting priority ura " + customCarPark.getSequencePriority());
                            printLog("ura id " + data.getCarparkNo());

                            finalCarParks.add(data);
                        }
                        break;
                    }
                } else {
                    MyTransportCarParkAvailability data = (MyTransportCarParkAvailability) carPark;
                    if (customCarPark.getId().equals(data.getCarParkID())) {
                        data.setSequencePriority(customCarPark.getSequencePriority());
                        if (!finalCarParks.contains(data)) {
                            printLog("setting priority my transport " + customCarPark.getSequencePriority());
                            printLog("my transport id " + data.getCarParkID());

                            finalCarParks.add(data);
                        }
                        break;
                    }
                }
            }
        }

        for (Object carPark : finalCarParks) {
            if (isUraCarPark(carPark)) {
                UraCarParkAvailability data = (UraCarParkAvailability) carPark;
                printLog("----------------Ura---------------");
                printLog("location: " + data.getGeometries().get(0).getCoordinates());
                printLog("name: " + data.getCharges().getPpName());
                printLog("slots: " + data.getLotsAvailable());
                printLog("priority " + data.getSequencePriority());
                printLog("destination distance " + data.getDistanceFromDestination());
                printLog("origin distance " + data.getDistanceFromOrigin());
            } else {
                MyTransportCarParkAvailability data = (MyTransportCarParkAvailability) carPark;
                printLog("------------My Transport----------------");
                printLog("location: " + data.getLocation());
                printLog("name: " + data.getCharges().getWeekDaysRate1());
                printLog("slots: " + data.getAvailableLots());
                printLog("priority " + data.getSequencePriority());
                printLog("destination distance " + data.getDistanceFromDestination());
                printLog("origin distance " + data.getDistanceFromOrigin());
            }
        }
        return finalCarParks;
    }

    public static ArrayList<UraCarParkAvailability> filterCarPark2(double fromLat,
                                                                   double fromLng,
                                                                   double toLat,
                                                                   double toLng,
                                                                   UraCarParkCharges carParkCharges,
                                                                   UraCarPark carParkAvailability) {

        final int MINIMUM_SLOTS = 1;
        final double MAXIMUM_SEARCH_RADIUS = 3000;

        List<UraCharges> allUraChargesCharges = carParkCharges.getResult();
        List<UraCarParkAvailability> allUraCarParkAvailability = carParkAvailability.getResult();
        final ArrayList<UraCarParkAvailability> carParkWithAvailableSlotsAndInRadius = new ArrayList<>();
        final ArrayList<UraCarParkAvailability> finalCarParks = new ArrayList<>();

        for (UraCarParkAvailability carPark : allUraCarParkAvailability) {

            if (carPark.getGeometries() == null) continue;

            if (TextUtils.isEmpty(carPark.getLotsAvailable())) continue;

            //removing car park with less than minimum slots
            if (Integer.parseInt(carPark.getLotsAvailable()) < MINIMUM_SLOTS) continue;

            for (UraCharges charges : allUraChargesCharges) {

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

        final ArrayList<UraCarParkAvailability> distanceCarParkRank = new ArrayList<>(carParkWithAvailableSlotsAndInRadius);
        final ArrayList<UraCarParkAvailability> emptyCarParkRank = new ArrayList<>(carParkWithAvailableSlotsAndInRadius);
        final ArrayList<UraCarParkAvailability> etaCarParkRank = new ArrayList<>(carParkWithAvailableSlotsAndInRadius);

        Collections.sort(distanceCarParkRank, (c1, c2) -> Double.compare(c1.getDistanceFromDestination(), c2.getDistanceFromDestination()));
        Collections.sort(emptyCarParkRank, (c1, c2) -> Integer.compare(Integer.parseInt(c2.getLotsAvailable()), Integer.parseInt(c1.getLotsAvailable())));
        Collections.sort(etaCarParkRank, (c1, c2) -> Double.compare(c1.getDistanceFromOrigin(), c2.getDistanceFromOrigin()));

        for (UraCarParkAvailability carPark : carParkWithAvailableSlotsAndInRadius) {
            int sequencePriority = distanceCarParkRank.indexOf(carPark) +
                    emptyCarParkRank.indexOf(carPark) +
                    etaCarParkRank.indexOf(carPark);

            carPark.setSequencePriority(sequencePriority);
            finalCarParks.add(carPark);
        }

        Collections.sort(finalCarParks, (c1, c2) -> c1.getSequencePriority().compareTo(c2.getSequencePriority()));

        for (UraCarParkAvailability carPark : finalCarParks) {
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
                                                             ArrayList<MyTransportCarParkAvailability> carParkings) {
        ArrayList<MarkerOptions> carParkMarkers = new ArrayList<>();

        for (MyTransportCarParkAvailability carPark : carParkings) {
            carParkMarkers.add(getCustomMaker(context,
                                              carPark.getCarParkLat(),
                                              carPark.getCarParkLng(),
                                              R.drawable.ic_parking_marker));
        }

        return carParkMarkers;
    }

    public static ArrayList<MarkerOptions> getCarParkMarkers2(Context context,
                                                              List<UraCarParkAvailability> carParkings) {
        ArrayList<MarkerOptions> carParkMarkers = new ArrayList<>();

        for (UraCarParkAvailability carPark : carParkings) {

            if (carPark.getGeometries() == null) continue;
            String[] location = carPark.getGeometries().get(0).getCoordinates().split(",");
            LatLonCoordinate lanLang = SVY21.computeLatLon(Double.parseDouble(location[1]), Double.parseDouble(location[0]));
            carPark.setLat(lanLang.getLatitude());
            carPark.setLng(lanLang.getLongitude());

            carParkMarkers.add(getCustomMaker(context,
                                              carPark.getLat(),
                                              carPark.getLng(),
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

    public static boolean isUraCarPark(Object carpark) {
        return carpark instanceof UraCarParkAvailability;
    }
}
