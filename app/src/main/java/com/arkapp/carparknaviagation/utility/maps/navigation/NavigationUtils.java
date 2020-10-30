package com.arkapp.carparknaviagation.utility.maps.navigation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.VectorDrawable;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.data.models.redLightCamera.Feature;
import com.arkapp.carparknaviagation.data.models.speedCamera.SpeedFeature;
import com.arkapp.carparknaviagation.data.repository.PrefRepository;
import com.arkapp.carparknaviagation.databinding.ActivityNavigationBinding;
import com.arkapp.carparknaviagation.utility.Constants;
import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.MapSettings;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.guidance.VoiceCatalog;
import com.here.android.mpa.guidance.VoiceGuidanceOptions;
import com.here.android.mpa.guidance.VoicePackage;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.routing.CoreRouter;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.android.mpa.routing.Router;
import com.here.android.mpa.routing.RoutingError;

import java.lang.ref.WeakReference;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

import static com.arkapp.carparknaviagation.ui.navigation.Utils.getNextManeuverIcon;
import static com.arkapp.carparknaviagation.ui.navigation.Utils.getTurnName;
import static com.arkapp.carparknaviagation.utility.Constants.DEFAULT_SIMULATION_SPEED;
import static com.arkapp.carparknaviagation.utility.Constants.SETTING_SIMULATION_KEY;
import static com.arkapp.carparknaviagation.utility.Constants.SETTING_SIMULATION_SPEED_KEY;
import static com.arkapp.carparknaviagation.utility.ViewUtils.hide;
import static com.arkapp.carparknaviagation.utility.ViewUtils.printLog;
import static com.arkapp.carparknaviagation.utility.ViewUtils.show;
import static com.arkapp.carparknaviagation.utility.ViewUtils.showSnack;
import static com.arkapp.carparknaviagation.utility.ViewUtils.showSnackIndefinate;
import static com.arkapp.carparknaviagation.utility.maps.others.MapUtils.getBitmap;
import static com.arkapp.carparknaviagation.utility.maps.others.MapUtils.getResizedBitmap;
import static com.arkapp.carparknaviagation.utility.maps.others.MapUtils.getSpeedIcon;

/**
 * This class encapsulates the properties and functionality of the Map view.It also triggers a
 * turn-by-turn navigation.
 */
public class NavigationUtils {
    private final PrefRepository prefRepository;
    private final AppCompatActivity activity;
    private AndroidXMapFragment mapFragment;
    private Map map;
    private NavigationManager navigationManager;
    private GeoBoundingBox geoBoundingBox;
    private Route route;
    private boolean foregroundServiceStarted;
    private final ActivityNavigationBinding binding;
    private final SharedPreferences settingPref;
    private int currentMoveDistance = 0;
    private int lastTotalDistance = 0;
    private MapRoute mapRoute;

    private final NavigationManager.PositionListener positionListener = new NavigationManager.PositionListener() {
        @Override
        public void onPositionUpdated(GeoPosition geoPosition) {
            /* Current position information can be retrieved in this callback */
            // the position we get in this callback can be used
            // to reposition the map and change orientation.
            geoPosition.getCoordinate();
            geoPosition.getHeading();
            geoPosition.getSpeed();

            // also remaining time and distance can be
            // fetched from navigation manager
            //navigationManager.getTta(Route.TrafficPenaltyMode.DISABLED, true);
            //navigationManager.getDestinationDistance();

            int distanceToManeuver = (int) (currentMoveDistance - (lastTotalDistance - navigationManager.getDestinationDistance()));
            if (distanceToManeuver >= 0)
                binding.tvDistance.setText(distanceToManeuver + " m");
            else
                binding.tvDistance.setText("0 m");
        }
    };

    private final NavigationManager.NavigationManagerEventListener navigationManagerEventListener = new NavigationManager.NavigationManagerEventListener() {
        @Override
        public void onRunningStateChanged() {
            //Toast.makeText(activity, "Running state changed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNavigationModeChanged() {
            //Toast.makeText(activity, "Navigation mode changed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEnded(NavigationManager.NavigationMode navigationMode) {
            //Toast.makeText(activity, navigationMode + " was ended", Toast.LENGTH_SHORT).show();
            stopForegroundService();
        }

        @Override
        public void onMapUpdateModeChanged(NavigationManager.MapUpdateMode mapUpdateMode) {
            /*Toast.makeText(activity, "Map update mode is changed to " + mapUpdateMode,
                           Toast.LENGTH_SHORT).show();*/
        }

        @Override
        public void onRouteUpdated(Route route) {
            //Toast.makeText(activity, "Route updated", Toast.LENGTH_SHORT).show();
            map.removeMapObject(mapRoute);
            // create a new MapRoute object
            mapRoute = new MapRoute(route);
            // display new route on the map
            map.addMapObject(mapRoute);
        }

        @Override
        public void onCountryInfo(String s, String s1) {
            //Toast.makeText(activity, "Country info updated from " + s + " to " + s1, Toast.LENGTH_SHORT).show();
        }
    };

    private final NavigationManager.NewInstructionEventListener instructListener
            = new NavigationManager.NewInstructionEventListener() {

        @Override
        public void onNewInstructionEvent() {
            // Interpret and present the Maneuver object as it contains
            // turn by turn navigation instructions for the user.\

            if (navigationManager.getNextManeuver() == null) return;
            binding.tvNextMove.setText(getTurnName(navigationManager.getNextManeuver().getTurn()));
            binding.tvRoadName.setText(navigationManager.getNextManeuver().getRoadName());
            if (navigationManager.getNextManeuver().getIcon() != null)
                binding.ivNextMove.setImageResource(getNextManeuverIcon(navigationManager.getNextManeuver().getIcon()));
            binding.tvDistance.setText(navigationManager.getNextManeuver().getDistanceFromPreviousManeuver() + " m");
            lastTotalDistance = (int) navigationManager.getDestinationDistance();

            currentMoveDistance = navigationManager.getNextManeuver().getDistanceFromPreviousManeuver();

            show(binding.cvMove);

        }
    };


    public NavigationUtils(AppCompatActivity activity,
                           PrefRepository prefRepository,
                           ActivityNavigationBinding binding) {
        this.activity = activity;
        this.prefRepository = prefRepository;
        this.binding = binding;

        binding.ivClose.setOnClickListener(view -> activity.onBackPressed());
        settingPref = PreferenceManager.getDefaultSharedPreferences(activity);

        MapSettings.setDiskCacheRootPath(activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());

        initMapFragment();
    }

    private AndroidXMapFragment getMapFragment() {
        return (AndroidXMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.mapfragment);
    }

    private void initMapFragment() {
        /* Locate the mapFragment UI element */
        mapFragment = getMapFragment();

        if (mapFragment != null) {
            /* Initialize the AndroidXMapFragment, results will be given via the called back. */
            mapFragment.init(error -> {

                hide(binding.progressBar);
                hide(binding.info);

                if (error == OnEngineInitListener.Error.NONE) {
                    map = mapFragment.getMap();
                    map.setCenter(new GeoCoordinate(prefRepository.getNavigationStartLat(), prefRepository.getNavigationStartLng()), Map.Animation.NONE);
                    //Put this call in Map.onTransformListener if the animation(Linear/Bow)
                    //is used in setCenter()
                    map.setZoomLevel(13.2);
                    map.getPositionIndicator().setVisible(true);
                    map.getPositionIndicator().setMarker(getCurrentLocationMarker());
                    /*
                     * Get the NavigationManager instance.It is responsible for providing voice
                     * and visual instructions while driving and walking
                     */
                    navigationManager = NavigationManager.getInstance();

                    for (Feature camera : prefRepository.getCurrentRouteRedLightCamera()) {
                        setMarkerOnMap(camera.getGeometry().getCoordinates().get(1),
                                       camera.getGeometry().getCoordinates().get(0),
                                       R.drawable.ic_red_camera);
                    }

                    for (SpeedFeature camera : prefRepository.getCurrentRouteSpeedCamera()) {
                        setMarkerOnMap(camera.getGeometry().getCoordinates().get(1),
                                       camera.getGeometry().getCoordinates().get(0),
                                       getSpeedIcon(camera.getProperties().getSpeed()));
                    }

                    initNavigation();
                } else {
                    new AlertDialog.Builder(activity).setMessage(
                            "Error : " + error.name() + "\n\n" + error.getDetails())
                            .setTitle(R.string.engine_init_error)
                            .setNegativeButton(android.R.string.cancel, (dialog, which) -> activity.finish())
                            .create()
                            .show();
                }
            });
        }
    }

    private void createRoute() {
        /* Initialize a CoreRouter */
        CoreRouter coreRouter = new CoreRouter();

        /* Initialize a RoutePlan */
        RoutePlan routePlan = new RoutePlan();

        /*
         * Initialize a RouteOption. HERE Mobile SDK allow users to define their own parameters for the
         * route calculation,including transport modes,route types and route restrictions etc.Please
         * refer to API doc for full list of APIs
         */
        RouteOptions routeOptions = new RouteOptions();
        /* Other transport modes are also available e.g Pedestrian */
        routeOptions.setTransportMode(RouteOptions.TransportMode.CAR);
        /* Disable highway in this route. */
        routeOptions.setHighwaysAllowed(false);
        /* Calculate the shortest route available. */
        routeOptions.setRouteType(RouteOptions.Type.SHORTEST);
        /* Calculate 1 route. */
        routeOptions.setRouteCount(1);
        /* Finally set the route option */
        routePlan.setRouteOptions(routeOptions);

        /* Define waypoints for the route */
        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(prefRepository.getNavigationStartLat(), prefRepository.getNavigationStartLng()));
        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(prefRepository.getNavigationEndLat(), prefRepository.getNavigationEndLng()));

        /* Add both waypoints to the route plan */
        routePlan.addWaypoint(startPoint);
        routePlan.addWaypoint(destination);

        /* Trigger the route calculation,results will be called back via the listener */
        coreRouter.calculateRoute(routePlan, new Router.Listener<List<RouteResult>, RoutingError>() {

            @Override
            public void onProgress(int i) {
                /* The calculation progress can be retrieved in this callback. */
            }

            @Override
            public void onCalculateRouteFinished(
                    List<RouteResult> routeResults,
                    RoutingError routingError) {
                /* Calculation is done.Let's handle the result */
                if (routingError == RoutingError.NONE) {
                    if (routeResults.get(0).getRoute() != null) {

                        route = routeResults.get(0).getRoute();
                        /* Create a MapRoute so that it can be placed on the map */
                        mapRoute = new MapRoute(routeResults.get(0).getRoute());

                        /* Show the maneuver number on top of the route */
                        mapRoute.setManeuverNumberVisible(true);
                        mapRoute.setColor(R.color.colorPrimaryCustomDark);
                        mapRoute.setManeuverNumberColor(R.color.colorPrimaryCustomDark);

                        /* Add the MapRoute to the map */
                        map.addMapObject(mapRoute);

                        /*
                         * We may also want to make sure the map view is orientated properly
                         * so the entire route can be easily seen.
                         */
                        geoBoundingBox = routeResults.get(0).getRoute().getBoundingBox();
                        map.zoomTo(geoBoundingBox, Map.Animation.NONE,
                                   Map.MOVE_PRESERVE_ORIENTATION);

                        startNavigation();
                    } else {
                        Toast.makeText(activity,
                                       "Error:route results returned is not valid",
                                       Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(activity,
                                   "Error:route calculation returned error code: " + routingError,
                                   Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void initNavigation() {
        if (route == null) {
            createRoute();
        }
    }

    private void stopNavigation() {
        navigationManager.stop();
        /*
         * Restore the map orientation to show entire route on screen
         */
        map.zoomTo(geoBoundingBox, Map.Animation.NONE, 0f);
        route = null;
    }

    /*private void initNaviControlButton() {
        //m_naviControlButton = m_activity.findViewById(R.id.naviCtrlButton);
        m_naviControlButton.setText(R.string.start_navi);
        m_naviControlButton.setOnClickListener(v -> {
            *//*
     * To start a turn-by-turn navigation, a concrete route object is required.
     *
     * The route calculation requires local map data.Unless there is pre-downloaded map
     * data on device by utilizing MapLoader APIs,it's not recommended to trigger the
     * route calculation immediately after the MapEngine is initialized.The
     * INSUFFICIENT_MAP_DATA error code may be returned by CoreRouter in this case.
     *
     *//*
            if (m_route == null) {
                createRoute();
            } else {
                m_navigationManager.stop();
                *//*
     * Restore the map orientation to show entire route on screen
     *//*
                m_map.zoomTo(m_geoBoundingBox, Map.Animation.NONE, 0f);
                m_naviControlButton.setText(R.string.start_navi);
                m_route = null;
            }
        });
    }*/

    /*
     * Android 8.0 (API level 26) limits how frequently background apps can retrieve the user's
     * current location. Apps can receive location updates only a few times each hour.
     * See href="https://developer.android.com/about/versions/oreo/background-location-limits.html
     * In order to retrieve location updates more frequently start a foreground service.
     * See https://developer.android.com/guide/components/services.html#Foreground
     */
    private void startForegroundService() {
        if (!foregroundServiceStarted) {
            foregroundServiceStarted = true;
            Intent startIntent = new Intent(activity, ForegroundService.class);
            startIntent.setAction(ForegroundService.START_ACTION);
            activity.getApplicationContext().startService(startIntent);
        }
    }

    private void stopForegroundService() {
        if (foregroundServiceStarted) {
            foregroundServiceStarted = false;
            Intent stopIntent = new Intent(activity, ForegroundService.class);
            stopIntent.setAction(ForegroundService.STOP_ACTION);
            activity.getApplicationContext().startService(stopIntent);
        }
    }

    private void startNavigation() {
        /* Configure Navigation manager to launch navigation on current map */
        navigationManager.setMap(map);
        map.setTilt(60);
        /*
         * Start the turn-by-turn navigation.Please note if the transport mode of the passed-in
         * route is pedestrian, the NavigationManager automatically triggers the guidance which is
         * suitable for walking. Simulation and tracking modes can also be launched at this moment
         * by calling either simulate() or startTracking()
         */

        if (settingPref.getBoolean(SETTING_SIMULATION_KEY, true)) {
            //Default Simulation speed is set to 20 m/s
            navigationManager.simulate(route, settingPref.getInt(SETTING_SIMULATION_SPEED_KEY, DEFAULT_SIMULATION_SPEED));
            startForegroundService();
        } else {
            navigationManager.startNavigation(route);
            startForegroundService();
        }
        /* Choose navigation modes between real time navigation and simulation *//*
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(m_activity);
        alertDialogBuilder.setTitle("Navigation");
        alertDialogBuilder.setMessage("Choose Mode");
        alertDialogBuilder.setNegativeButton("Navigation", (dialoginterface, i) -> {
            m_navigationManager.startNavigation(m_route);
            m_map.setTilt(60);
            startForegroundService();
        });
        alertDialogBuilder.setPositiveButton("Simulation", (dialoginterface, i) -> {
            m_navigationManager.simulate(m_route, 60);//Simualtion speed is set to 60 m/s
            m_map.setTilt(60);
            startForegroundService();
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();*/
        /*
         * Set the map update mode to ROADVIEW.This will enable the automatic map movement based on
         * the current location.If user gestures are expected during the navigation, it's
         * recommended to set the map update mode to NONE first. Other supported update mode can be
         * found in HERE Mobile SDK for Android (Premium) API doc
         */
        navigationManager.setMapUpdateMode(NavigationManager.MapUpdateMode.ROADVIEW);

        /*
         * NavigationManager contains a number of listeners which we can use to monitor the
         * navigation status and getting relevant instructions.In this example, we will add 2
         * listeners for demo purpose,please refer to HERE Android SDK API documentation for details
         */
        addNavigationListeners();
        setVoiceGuidance();
    }

    private void setVoiceGuidance() {
        VoiceCatalog voiceCatalog = VoiceCatalog.getInstance();
        voiceCatalog.downloadCatalog(errorCode -> {
            if (errorCode == VoiceCatalog.Error.NONE) {
                // catalog download successful

                // Get the list of voice packages from the voice catalog list
                List<VoicePackage> voicePackages = VoiceCatalog.getInstance().getCatalogList();
                long id;
                // select
                String selectedLanguageKey = settingPref.getString(Constants.SETTING_LANGUAGE_KEY, "ENG");
                printLog("selectedLanguageKey " + selectedLanguageKey);
                String selectedLanguageId = selectedLanguageKey.split("~")[0];
                String selectedLanguageGender = selectedLanguageKey.split("~")[1];

                for (VoicePackage vPackage : voicePackages) {

                    printLog("vPackage.getMarcCode() " + vPackage.getMarcCode());
                    printLog("vPackage.getLocalizedLanguage() " + vPackage.getLocalizedLanguage());

                    if (vPackage.getMarcCode().equalsIgnoreCase(selectedLanguageId) &&
                            vPackage.getGender().name().equalsIgnoreCase(selectedLanguageGender)) {
                        id = vPackage.getId();
                        printLog("id" + id);
                        printLog("voiceCatalog.isLocalVoiceSkin(id)" + voiceCatalog.isLocalVoiceSkin(id));
                        if (!voiceCatalog.isLocalVoiceSkin(id)) {
                            showSnackIndefinate(binding.parent, "Downloading voice assistant data...");
                            voiceCatalog.downloadVoice(id, errorCode1 -> {
                                if (errorCode1 == VoiceCatalog.Error.NONE) {
                                    printLog("download completed");
                                    showSnack(binding.parent, "Download Completed");

                                    //voice skin download successful
                                    VoiceGuidanceOptions voiceGuidanceOptions = navigationManager.getVoiceGuidanceOptions();
                                    // set the voice skin for use by navigation manager
                                    voiceGuidanceOptions.setVoiceSkin(voiceCatalog.getLocalVoiceSkin(id));
                                    navigationManager.setNaturalGuidanceMode(EnumSet.of(NavigationManager.NaturalGuidanceMode.JUNCTION,
                                                                                        NavigationManager.NaturalGuidanceMode.TRAFFIC_LIGHT,
                                                                                        NavigationManager.NaturalGuidanceMode.STOP_SIGN));
                                } else {
                                    showSnack(binding.parent, "Download Error");
                                    printLog("download error " + errorCode1.name());
                                }
                            });
                        } else {

                            VoiceGuidanceOptions voiceGuidanceOptions = navigationManager.getVoiceGuidanceOptions();
                            // set the voice skin for use by navigation manager
                            voiceGuidanceOptions.setVoiceSkin(voiceCatalog.getLocalVoiceSkin(id));
                            navigationManager.setNaturalGuidanceMode(EnumSet.of(NavigationManager.NaturalGuidanceMode.JUNCTION,
                                                                                NavigationManager.NaturalGuidanceMode.TRAFFIC_LIGHT,
                                                                                NavigationManager.NaturalGuidanceMode.STOP_SIGN));
                        }
                        break;

                    }
                }
            }
        });
    }

    private void addNavigationListeners() {

        /*
         * Register a NavigationManagerEventListener to monitor the status change on
         * NavigationManager
         */
        navigationManager.addNavigationManagerEventListener(
                new WeakReference<>(navigationManagerEventListener));

        /* Register a PositionListener to monitor the position updates */
        navigationManager.addPositionListener(new WeakReference<>(positionListener));

        // start listening for navigation events
        navigationManager.addNewInstructionEventListener(new WeakReference<>(instructListener));
    }

    public void onDestroy() {
        /* Stop the navigation when app is destroyed */
        if (navigationManager != null) {
            stopForegroundService();
            navigationManager.stop();
        }
    }

    private void setMarkerOnMap(double lat, double log, int drawable) {
        GeoCoordinate location = new GeoCoordinate(lat, log);
        map.setCenter(location, Map.Animation.NONE);

        //removing the old marker
        MapMarker marker = new MapMarker();

        setCustomMaker(activity, marker, drawable);
        marker.setCoordinate(location);

        map.addMapObject(marker);
    }

    private void setCustomMaker(Context context, MapMarker marker, int drawable) {

        //used to set the custom marker image on current location marker
        try {
            Image currentMarkerImage = new Image();
            try {
                Bitmap icon = getBitmap((VectorDrawable) Objects.requireNonNull(ContextCompat.getDrawable(context, drawable)));
                currentMarkerImage.setBitmap(icon);
            } catch (Exception e) {
                Bitmap icon = getResizedBitmap(getBitmap(drawable, context), 78);
                currentMarkerImage.setBitmap(icon);
            }
            marker.setAnchorPoint(new PointF(currentMarkerImage.getWidth() / 2, currentMarkerImage.getHeight()));

            marker.setIcon(currentMarkerImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Image getCurrentLocationMarker() {
        Image image = new Image();
        Bitmap icon = getResizedBitmap(
                getBitmap((VectorDrawable) Objects.requireNonNull(
                        ContextCompat.getDrawable(activity, R.drawable.ic_marker1))), 78);
        image.setBitmap(icon);
        return image;
    }
}
