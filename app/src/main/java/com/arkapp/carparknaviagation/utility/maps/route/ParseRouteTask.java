package com.arkapp.carparknaviagation.utility.maps.route;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.arkapp.carparknaviagation.viewModels.HomePageViewModel.polylineFinal;

/**
 * Created by Abdul Rehman on 12/24/2018.
 */
public class ParseRouteTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    private GoogleMap mGoogleMap;
    private int colourForPathPlot;

    public ParseRouteTask(GoogleMap googleMap, int color) {
        mGoogleMap = googleMap;
        colourForPathPlot = color;
    }

    /**
     * plotting the directions in google map
     */
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(
            String... jsonData) {
        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            PathJSONParser parser = new PathJSONParser();
            routes = parser.parse(jObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
        ArrayList<LatLng> points;
        PolylineOptions polyLineOptions = null;
        // traversing through routes
        if (routes != null) {
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.geodesic(true);
                polyLineOptions.width(12);
                polyLineOptions.color(colourForPathPlot);
                polyLineOptions.startCap(new RoundCap());
                polyLineOptions.endCap(new RoundCap());
                polyLineOptions.jointType(JointType.ROUND);
            }
            try {
                if (polylineFinal != null) {
                    polylineFinal.remove();
                }
                polylineFinal = mGoogleMap.addPolyline(polyLineOptions);
            } catch (Exception e) {
            }

        }
    }
}
