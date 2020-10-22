package com.arkapp.carparknaviagation;

import com.arkapp.carparknaviagation.data.models.CustomCarPark;
import com.arkapp.carparknaviagation.data.models.myTransportCarPark.MyTransportCarPark;
import com.arkapp.carparknaviagation.data.models.myTransportCarPark.MyTransportCarParkAvailability;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCarPark;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCarParkAvailability;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCarParkCharges;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCharges;
import com.arkapp.carparknaviagation.ui.carParkList.Utils;
import com.arkapp.carparknaviagation.utility.ViewUtils;
import com.arkapp.carparknaviagation.utility.maps.others.MapUtils;
import com.arkapp.carparknaviagation.utility.retrofit.RetrofitService;
import com.arkapp.carparknaviagation.utility.svy21.LatLonCoordinate;
import com.arkapp.carparknaviagation.utility.svy21.SVY21;
import com.here.android.mpa.routing.Maneuver;

import org.junit.Test;

import java.util.ArrayList;

import static com.arkapp.carparknaviagation.R.drawable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Created by Abdul Rehman on 19-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class JunitCustomTests {

    @Test
    public void isTimeStampFormatCorrect() {
        assertTrue(ViewUtils.getCurrentTimestamp() != 0);
    }

    @Test
    public void doubleRatesTest1() {
        assertTrue(Utils.doubleRate("2.15 $") == 4.30);
    }

    @Test
    public void doubleRatesTest2() {
        assertTrue(Utils.doubleRate("2.15") == 4.30);
    }

    @Test
    public void doubleRatesTest3() {
        assertFalse(Utils.doubleRate("1$") == 4.30);
    }

    @Test
    public void getUraChargeStringTest1() {
        assertNotNull(Utils.getUraChargeString(null));
    }

    @Test
    public void getUraChargeStringTest2() {
        UraCharges charges = new UraCharges();
        charges.setSunPHRate("$0.60");
        charges.setWeekdayRate("$0.60");
        charges.setSatdayRate("$0.60");
        assertNotNull(Utils.getUraChargeString(charges));
    }

    @Test(expected = NullPointerException.class)
    public void getUraChargeStringTest3() {
        UraCharges charges = new UraCharges();
        charges.setSunPHRate("$0.60");
        charges.setWeekdayRate("$0.60");
        charges.setSatdayRate("$0.60");
        assertTrue(Utils.getUraChargeString(charges).toString().length() > 0);
    }

    @Test(expected = NullPointerException.class)
    public void getUraChargeStringTest4() {
        UraCharges charges = new UraCharges();
        charges.setSunPHRate("$0.60");
        charges.setWeekdayRate("$0.60");
        charges.setSatdayRate("$0.60");
        assertFalse(Utils.getUraChargeString(charges).toString().equalsIgnoreCase("Rates Unavailable!"));
    }

    @Test
    public void getMyTransportChargeStringTest1() {
        MyTransportCarParkAvailability charges = new MyTransportCarParkAvailability();
        assertNotNull(Utils.getUraChargeString(null));
    }

    @Test(expected = NullPointerException.class)
    public void getMyTransportChargeStringTest2() {
        MyTransportCarParkAvailability charges = new MyTransportCarParkAvailability();
        String data = Utils.getUraChargeString(null).toString();
        assertTrue(data.equals("Rates Unavailable!"));
    }

    @Test
    public void getNextManeuverIconTest1() {
        assertNotNull(com.arkapp.carparknaviagation.ui.navigation.Utils.getNextManeuverIcon(null));
    }

    @Test
    public void getNextManeuverIconTest2() {
        assertNotNull(com.arkapp.carparknaviagation.ui.navigation.Utils.getNextManeuverIcon(Maneuver.Icon.UNDEFINED));
    }


    @Test
    public void getTurnNameTest1() {
        assertNotNull(com.arkapp.carparknaviagation.ui.navigation.Utils.getTurnName(null));
    }

    @Test
    public void getTurnNameTest2() {
        assertNotNull(com.arkapp.carparknaviagation.ui.navigation.Utils.getTurnName(Maneuver.Turn.UNDEFINED));
    }

    @Test
    public void getEtaUrlTest1() {
        assertNotNull(com.arkapp.carparknaviagation.utility.maps.eta.Utils.getEtaUrl(null, null, null));
    }

    @Test
    public void getDistanceMatrixLatLngObjTest1() {
        assertNotNull(com.arkapp.carparknaviagation.utility.maps.eta.Utils.getDistanceMatrixLatLngObj(null));
    }

    @Test
    public void getDistanceMatrixLatLngObjTest2() {
        assertTrue(com.arkapp.carparknaviagation.utility.maps.eta.Utils.getDistanceMatrixLatLngObj(new ArrayList()).equals(""));
    }

    @Test
    public void getUraCarParkLatLngTest1() {
        assertNotNull(com.arkapp.carparknaviagation.utility.maps.eta.Utils.getUraCarParkLatLng(null));
    }

    @Test
    public void getMyTransportCarParkLatLngTest1() {
        assertNotNull(com.arkapp.carparknaviagation.utility.maps.eta.Utils.getMyTransportCarParkLatLng(null));
    }

    @Test
    public void getBitmapTest1() {
        assertTrue(MapUtils.getBitmap(null) == null);
    }

    @Test
    public void getMapsApiDirectionsFromUrlTest1() {
        assertNotNull(MapUtils.getMapsApiDirectionsFromUrl(null, null, null, null, null));
    }

    @Test
    public void getMapsApiDirectionsFromUrlTest2() {
        assertNotNull(MapUtils.getMapsApiDirectionsFromUrl("", "", "", "", ""));
    }

    @Test
    public void getMapsApiDirectionsFromUrlTest3() {
        assertTrue(MapUtils.getMapsApiDirectionsFromUrl("0.0", "0.0", "0.0", "0.0", "").length() > 0);
    }

    @Test
    public void calculateDistanceBetweenPointsTest1() {
        assertEquals(0f, MapUtils.calculateDistanceBetweenPoints(0.0, 0.0, 0.0, 0.0), 0.0);
    }

    @Test
    public void calculateDistanceBetweenPointsTest2() {
        assertFalse(((int) (MapUtils.calculateDistanceBetweenPoints(
                40.689202777778,
                -74.044219444444,
                38.889069444444,
                -77.034502777778) / 1000)) == 324);
    }

    @Test
    public void getSpeedIconTest1() {
        assertNotNull(MapUtils.getSpeedIcon(0));
    }

    @Test
    public void getSpeedIconTest2() {
        assertEquals(MapUtils.getSpeedIcon(100), drawable.ic_speed_60);
    }

    @Test
    public void getSpeedIconTest3() {
        assertEquals(MapUtils.getSpeedIcon(30), drawable.ic_speed_30);
    }

    @Test
    public void getSpeedIconTest4() {
        assertEquals(MapUtils.getSpeedIcon(30), drawable.ic_speed_30);
    }

    @Test
    public void getSpeedIconTest5() {
        assertEquals(MapUtils.getSpeedIcon(90), drawable.ic_speed_90);
    }

    @Test
    public void getClientTest1() {
        assertNotNull(RetrofitService.getClient());
    }

    @Test
    public void getRetrofitTest1() {
        assertNotNull(RetrofitService.getRetrofit("http://test"));
    }


    @Test
    public void computeLatLonTest1() {
        LatLonCoordinate coordinate = SVY21.computeLatLon(0, 0);
        assertNotNull(coordinate);
    }

    @Test
    public void computeLatLonTest2() {
        LatLonCoordinate coordinate = SVY21.computeLatLon(30314.7936, 31490.4942);
        assertTrue(coordinate.getLatitude() == 1.2904298744155682 &&
                           coordinate.getLongitude() == 103.86468178830191);
    }

    @Test
    public void getRouteRedLightCameraTest1() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.getRouteRedLightCamera(null, null));
    }

    @Test
    public void getRouteRedLightCameraTest2() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.getRouteRedLightCamera(
                new ArrayList<>(),
                null));
    }

    @Test
    public void isUraCarParkTest1() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.isUraCarPark(null));
    }

    @Test
    public void isUraCarParkTest2() {
        assertFalse(com.arkapp.carparknaviagation.ui.home.Utils.isUraCarPark(new MyTransportCarPark()));
    }

    @Test
    public void isUraCarParkTest3() {
        assertFalse(com.arkapp.carparknaviagation.ui.home.Utils.isUraCarPark(new CustomCarPark()));
    }

    @Test
    public void isUraCarParkTest4() {
        assertTrue(com.arkapp.carparknaviagation.ui.home.Utils.isUraCarPark(new UraCarParkAvailability()));
    }

    @Test
    public void removeInvalidUraCarParkTest1() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.removeInvalidUraCarPark(
                0,
                0,
                0,
                0,
                null,
                null,
                0));
    }

    @Test
    public void removeInvalidUraCarParkTest2() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.removeInvalidUraCarPark(
                0,
                0,
                0,
                0,
                new UraCarParkCharges(),
                new UraCarPark(),
                0));
    }

    @Test
    public void removeInvalidMyTransportCarParkTest1() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.removeInvalidMyTransportCarPark(
                0,
                0,
                0,
                0,
                null,
                null,
                0));
    }

    @Test
    public void removeInvalidMyTransportCarParkTest2() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.removeInvalidMyTransportCarPark(
                0,
                0,
                0,
                0,
                new MyTransportCarPark(),
                new ArrayList<>(),
                0));
    }

    @Test
    public void filterUraCarParkTest1() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.filterUraCarPark(null));
    }

    @Test
    public void filterUraCarParkTest2() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.filterUraCarPark(new ArrayList<>()));
    }

    @Test
    public void filterMyTransportCarParkTest1() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.filterMyTransportCarPark(null));
    }

    @Test
    public void filterMyTransportCarParkTest2() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.filterMyTransportCarPark(new ArrayList<>()));
    }

    @Test
    public void filterAllCarParkTest1() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.filterAllCarPark(
                null,
                null));
    }

    @Test
    public void filterAllCarParkTest2() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.filterAllCarPark(
                new ArrayList<>(),
                new ArrayList<>()));
    }
}
