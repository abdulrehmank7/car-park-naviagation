package com.arkapp.carparknaviagation.data.repository;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.data.models.carPark.AllCarPark;
import com.arkapp.carparknaviagation.data.models.rates.CarParkCharges;
import com.arkapp.carparknaviagation.data.models.rates.CarParkInformation;
import com.arkapp.carparknaviagation.data.models.redLightCamera.Feature;
import com.arkapp.carparknaviagation.data.models.redLightCamera.speedCameras;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.arkapp.carparknaviagation.utility.maps.others.MapUtils.getCustomMaker;

/**
 * Created by Abdul Rehman on 27-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class MapRepository {
    private Context context;
    private Gson gson = new Gson();

    public MapRepository(Context context) {
        this.context = context;
    }

    public List<Feature> getRedLightCamera() {
        try {

            InputStream is = context.getResources().openRawResource(R.raw.red_light_cameras);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            String jsonString = writer.toString();
            speedCameras speedCameras = gson.fromJson(jsonString, speedCameras.class);

            return speedCameras.getFeatures();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Feature> getSpeedCamera() {
        try {

            InputStream is = context.getResources().openRawResource(R.raw.speed_camera);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            String jsonString = writer.toString();
            speedCameras speedCameras = gson.fromJson(jsonString, speedCameras.class);

            return speedCameras.getFeatures();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public ArrayList<MarkerOptions> getRedLightMarkers() {
        try {

            InputStream is = context.getResources().openRawResource(R.raw.red_light_cameras);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            String jsonString = writer.toString();

            speedCameras speedCameras = gson.fromJson(jsonString, speedCameras.class);

            ArrayList<MarkerOptions> redLightMarkers = new ArrayList<>();

            for (Feature feature : speedCameras.getFeatures()) {
                redLightMarkers.add(getCustomMaker(context,
                                                   feature.getGeometry().getCoordinates().get(1),
                                                   feature.getGeometry().getCoordinates().get(0),
                                                   R.drawable.ic_red_light_marker));
            }

            return redLightMarkers;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public ArrayList<CarParkCharges> getCarParkCharges() {
        try {

            InputStream is = context.getResources().openRawResource(R.raw.car_park_rates);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            String jsonString = writer.toString();

            return gson.fromJson(jsonString, new TypeToken<List<CarParkCharges>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public ArrayList<CarParkInformation> getCarParkInformation() {
        try {

            InputStream is = context.getResources().openRawResource(R.raw.car_park_information);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            String jsonString = writer.toString();

            return gson.fromJson(jsonString, new TypeToken<List<CarParkInformation>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public AllCarPark getAllCarParkCharges() {
        try {

            InputStream is = context.getResources().openRawResource(R.raw.car_park_charges);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            String jsonString = writer.toString();

            return gson.fromJson(jsonString, AllCarPark.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getLocationAddress(double lat, double log) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());

            addresses = geocoder.getFromLocation(lat, log, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            return address;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public MarkerOptions getLocationMarkerOption(double lat,
                                                 double log,
                                                 int iconDrawable) {
        return getCustomMaker(context, lat, log, iconDrawable);
    }
}
