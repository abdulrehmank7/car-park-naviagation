package com.arkapp.carparknaviagation.utility.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import com.arkapp.carparknaviagation.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

import static com.arkapp.carparknaviagation.utility.Constants.GOOGLE_KEY;
import static com.arkapp.carparknaviagation.utility.ViewUtils.printLog;

/**
 * Created by Abdul Rehman on 21-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class MapUtils {

    public static final int REQUEST_CHECK_SETTINGS = 554;

    private static boolean isLastLoop;
    private static float zoomLevel;

    public static MarkerOptions addCustomCurrentMaker(Context context,
                                                      double lat,
                                                      double log,
                                                      int drawable) {

        //used to set the custom marker image on current location marker
        MarkerOptions marker = new MarkerOptions();

        BitmapDescriptor markerIcon = bitmapDescriptorFromVector(context, drawable);

        marker.position(new LatLng(lat, log));
        marker.title(context.getString(R.string.current_location));
        marker.icon(markerIcon);
        //marker.anchor(bitmap.getWidth() / 2, bitmap.getHeight());

        return marker;
    }

    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        //get the bitmap image from the vector drawable file.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                                            vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    //used for vector drawable in marker
    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //used for vector drawable in marker
    public static BitmapDescriptor bitmapDescriptorFromVector(Context context,
                                                              int vectorResId,
                                                              Bitmap bitmap) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static String getLocationAddress(Context context, double lat, double log) {
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

    /**
     * To get the directions url between pickup location and drop location
     */
    public static String getMapsApiDirectionsFromUrl(String originLat, String originLong,
                                                     String destLat, String destLong,
                                                     String isTollSelected) {
        String tollsParam;
        if (TextUtils.isEmpty(isTollSelected))
            tollsParam = "";
        else tollsParam = "&" + isTollSelected;
        String waypoints = "origin=" + originLat + ","
                + originLong + "&" + "destination="
                + destLat + "," + destLong;
        String sensor = "sensor=false";
        String params = waypoints + "&" + sensor;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/"
                + output + "?key=" + GOOGLE_KEY + "&" + params + "&travelmode=driving&alternatives=false&departure_time=now" + tollsParam;
    }

    public static void fitRouteInScreen(final GoogleMap map,
                                        final LatLng pick,
                                        final LatLng drop,
                                        final Context mContext) {

        //used to reduce the map route size and fit inside the screen
        try {
            isLastLoop = false;
            zoomLevel = 0;

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(pick);
            builder.include(drop);
            final LatLngBounds bounds = builder.build();
            final int width = mContext.getResources().getDisplayMetrics().widthPixels;
            final int height = mContext.getResources().getDisplayMetrics().heightPixels;

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, 0);
            GoogleMap.CancelableCallback callback = new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    if (!map.getProjection().getVisibleRegion().latLngBounds.contains(pick)
                            || !map.getProjection().getVisibleRegion().latLngBounds.contains(drop)) {
                        zoomLevel = map.getCameraPosition().zoom - 0.1f;
                        CameraUpdate cuf = CameraUpdateFactory.zoomTo(zoomLevel);
                        map.animateCamera(cuf, 1, this);
                    } else {
                        if (!isLastLoop) {
                            isLastLoop = true;
                            zoomLevel = map.getCameraPosition().zoom - 0.8f;
                            CameraUpdate cuf = CameraUpdateFactory.zoomTo(zoomLevel);
                            map.animateCamera(cuf, 1, this);
                        } /*else if (isLoadingDone) shiftCenterPointTop(map, pick, drop);*/
                    }
                }

                @Override
                public void onCancel() {
                }
            };
            map.animateCamera(cu, 100, callback);
        } catch (Exception e) {
            printLog(e.getMessage());
        }
    }
}
