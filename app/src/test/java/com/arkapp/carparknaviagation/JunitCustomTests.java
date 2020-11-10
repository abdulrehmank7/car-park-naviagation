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

    //To test if timestamp will be non zero
    @Test
    public void isTimeStampFormatCorrect() {
        assertTrue(ViewUtils.getCurrentTimestamp() != 0);
    }

    //To test if doubleRate method will give correct result with 2.15 $ as input
    @Test
    public void doubleRatesTest1() {
        assertTrue(Utils.doubleRate("2.15 $") == 4.30);
    }

    //To test if doubleRate method will give correct result with 2.15 as input
    @Test
    public void doubleRatesTest2() {
        assertTrue(Utils.doubleRate("2.15") == 4.30);
    }

    //To test if doubleRate method will give correct result with 1$ as input
    @Test
    public void doubleRatesTest3() {
        assertFalse(Utils.doubleRate("1$") == 4.30);
    }

    //To test if getUraChargeString method with null as input
    @Test
    public void getUraChargeStringTest1() {
        assertNotNull(Utils.getUraChargeString(null));
    }

    //To test if getUraChargeString method with random inputs and check the result for null pointer
    @Test
    public void getUraChargeStringTest2() {
        UraCharges charges = new UraCharges();
        charges.setSunPHRate("$0.60");
        charges.setWeekdayRate("$0.60");
        charges.setSatdayRate("$0.60");
        assertNotNull(Utils.getUraChargeString(charges));
    }

    //To test if getUraChargeString method with random inputs and check the length of the result
    @Test(expected = NullPointerException.class)
    public void getUraChargeStringTest3() {
        UraCharges charges = new UraCharges();
        charges.setSunPHRate("$0.60");
        charges.setWeekdayRate("$0.60");
        charges.setSatdayRate("$0.60");
        assertTrue(Utils.getUraChargeString(charges).toString().length() > 0);
    }

    //To test if getUraChargeString method with random inputs
    @Test(expected = NullPointerException.class)
    public void getUraChargeStringTest4() {
        UraCharges charges = new UraCharges();
        charges.setSunPHRate("$0.60");
        charges.setWeekdayRate("$0.60");
        charges.setSatdayRate("$0.60");
        assertFalse(Utils.getUraChargeString(charges).toString().equalsIgnoreCase("Rates Unavailable!"));
    }

    //To test if getMyTransportChargeString method with null as input
    @Test
    public void getMyTransportChargeStringTest1() {
        MyTransportCarParkAvailability charges = new MyTransportCarParkAvailability();
        assertNotNull(Utils.getUraChargeString(null));
    }

    //To test if getMyTransportChargeString method with null as input
    @Test(expected = NullPointerException.class)
    public void getMyTransportChargeStringTest2() {
        MyTransportCarParkAvailability charges = new MyTransportCarParkAvailability();
        String data = Utils.getUraChargeString(null).toString();
        assertTrue(data.equals("Rates Unavailable!"));
    }

    //To test if getNextManeuverIcon method with null inputs
    @Test
    public void getNextManeuverIconTest1() {
        assertNotNull(com.arkapp.carparknaviagation.ui.navigation.Utils.getNextManeuverIcon(null));
    }

    //To test if getNextManeuverIcon method with Maneuver.Icon.UNDEFINED inputs
    @Test
    public void getNextManeuverIconTest2() {
        assertNotNull(com.arkapp.carparknaviagation.ui.navigation.Utils.getNextManeuverIcon(Maneuver.Icon.UNDEFINED));
    }

    //To test if getTurnName method with null as input
    @Test
    public void getTurnNameTest1() {
        assertNotNull(com.arkapp.carparknaviagation.ui.navigation.Utils.getTurnName(null));
    }

    //To test if getTurnName method with Maneuver.Turn.UNDEFINED inputs
    @Test
    public void getTurnNameTest2() {
        assertNotNull(com.arkapp.carparknaviagation.ui.navigation.Utils.getTurnName(Maneuver.Turn.UNDEFINED));
    }

    //To test if getEtaUrl method with null as input
    @Test
    public void getEtaUrlTest1() {
        assertNotNull(com.arkapp.carparknaviagation.utility.maps.eta.Utils.getEtaUrl(null, null, null));
    }

    //To test if getDistanceMatrixLatLngObj method with null as input
    @Test
    public void getDistanceMatrixLatLngObjTest1() {
        assertNotNull(com.arkapp.carparknaviagation.utility.maps.eta.Utils.getDistanceMatrixLatLngObj(null));
    }
    //To test if getDistanceMatrixLatLngObj method with empty inputs
    @Test
    public void getDistanceMatrixLatLngObjTest2() {
        assertTrue(com.arkapp.carparknaviagation.utility.maps.eta.Utils.getDistanceMatrixLatLngObj(new ArrayList()).equals(""));
    }

    //To test if getUraCarParkLatLng method with null as input
    @Test
    public void getUraCarParkLatLngTest1() {
        assertNotNull(com.arkapp.carparknaviagation.utility.maps.eta.Utils.getUraCarParkLatLng(null));
    }

    //To test if getMyTransportCarParkLatLng method with null as input
    @Test
    public void getMyTransportCarParkLatLngTest1() {
        assertNotNull(com.arkapp.carparknaviagation.utility.maps.eta.Utils.getMyTransportCarParkLatLng(null));
    }

    //To test if getBitmap method with null as input
    @Test
    public void getBitmapTest1() {
        assertTrue(MapUtils.getBitmap(null) == null);
    }

    //To test if getMapsApiDirectionsFromUrl method with null as input
    @Test
    public void getMapsApiDirectionsFromUrlTest1() {
        assertNotNull(MapUtils.getMapsApiDirectionsFromUrl(null, null, null, null, null));
    }

    //To test if getMapsApiDirectionsFromUrl method with empty string as input
    @Test
    public void getMapsApiDirectionsFromUrlTest2() {
        assertNotNull(MapUtils.getMapsApiDirectionsFromUrl("", "", "", "", ""));
    }

    //To test if getMapsApiDirectionsFromUrl method with 0.0 string as input
    @Test
    public void getMapsApiDirectionsFromUrlTest3() {
        assertTrue(MapUtils.getMapsApiDirectionsFromUrl("0.0", "0.0", "0.0", "0.0", "").length() > 0);
    }

    //To test if calculateDistanceBetweenPoints method with 0.0 as input
    @Test
    public void calculateDistanceBetweenPointsTest1() {
        assertEquals(0f, MapUtils.calculateDistanceBetweenPoints(0.0, 0.0, 0.0, 0.0), 0.0);
    }

    //To test if calculateDistanceBetweenPoints method distance from some random latitude and longitude points
    @Test
    public void calculateDistanceBetweenPointsTest2() {
        assertFalse(((int) (MapUtils.calculateDistanceBetweenPoints(
                40.689202777778,
                -74.044219444444,
                38.889069444444,
                -77.034502777778) / 1000)) == 324);
    }

    //To test if getSpeedIcon method with 0 as input
    @Test
    public void getSpeedIconTest1() {
        assertNotNull(MapUtils.getSpeedIcon(0));
    }

    //To test if getSpeedIcon method with speed input as 100
    @Test
    public void getSpeedIconTest2() {
        assertEquals(MapUtils.getSpeedIcon(100), drawable.ic_speed_60);
    }

    //To test if getSpeedIcon method with speed input as 30
    @Test
    public void getSpeedIconTest3() {
        assertEquals(MapUtils.getSpeedIcon(30), drawable.ic_speed_30);
    }

    //To test if getSpeedIcon method with speed input as 40
    @Test
    public void getSpeedIconTest4() {
        assertEquals(MapUtils.getSpeedIcon(40), drawable.ic_speed_40);
    }

    //To test if getSpeedIcon method with speed input as 90
    @Test
    public void getSpeedIconTest5() {
        assertEquals(MapUtils.getSpeedIcon(90), drawable.ic_speed_90);
    }

    //To test if getClient method will return null as response
    @Test
    public void getClientTest1() {
        assertNotNull(RetrofitService.getClient());
    }

    //To test if getRetrofit method with mock url as inputs
    @Test
    public void getRetrofitTest1() {
        assertNotNull(RetrofitService.getRetrofit("http://test"));
    }

    //To test if computeLatLon method with empty inputs
    @Test
    public void computeLatLonTest1() {
        LatLonCoordinate coordinate = SVY21.computeLatLon(0, 0);
        assertNotNull(coordinate);
    }

    //To test if computeLatLon method will give correct latitude longitude after conversion.
    @Test
    public void computeLatLonTest2() {
        LatLonCoordinate coordinate = SVY21.computeLatLon(30314.7936, 31490.4942);
        assertTrue(coordinate.getLatitude() == 1.2904298744155682 &&
                           coordinate.getLongitude() == 103.86468178830191);
    }

    //To test if getRouteRedLightCamera method with null as input
    @Test
    public void getRouteRedLightCameraTest1() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.getRouteRedLightCamera(null, null));
    }

    //To test if getRouteRedLightCamera method with empty inputs
    @Test
    public void getRouteRedLightCameraTest2() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.getRouteRedLightCamera(
                new ArrayList<>(),
                null));
    }

    //To test if isUraCarPark method with null as input
    @Test
    public void isUraCarParkTest1() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.isUraCarPark(null));
    }

    //To test if isUraCarPark method with empty inputs
    @Test
    public void isUraCarParkTest2() {
        assertFalse(com.arkapp.carparknaviagation.ui.home.Utils.isUraCarPark(new MyTransportCarPark()));
    }

    //To test if isUraCarPark method with empty inputs
    @Test
    public void isUraCarParkTest3() {
        assertFalse(com.arkapp.carparknaviagation.ui.home.Utils.isUraCarPark(new CustomCarPark()));
    }

    //To test if isUraCarPark method with empty inputs
    @Test
    public void isUraCarParkTest4() {
        assertTrue(com.arkapp.carparknaviagation.ui.home.Utils.isUraCarPark(new UraCarParkAvailability()));
    }

    //To test if removeInvalidUraCarPark method with null and 0 as inputs
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

    //To test if removeInvalidUraCarPark method with empty and 0 as inputs
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

    //To test if removeInvalidMyTransportCarPark method with null and 0 as inputs
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

    //To test if removeInvalidMyTransportCarPark method with empty and 0 as inputs
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

    //To test if filterUraCarPark method with null as inputs
    @Test
    public void filterUraCarParkTest1() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.filterUraCarPark(null));
    }

    //To test if filterUraCarPark method with empty inputs
    @Test
    public void filterUraCarParkTest2() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.filterUraCarPark(new ArrayList<>()));
    }

    //To test if filterMyTransportCarPark method with null as inputs
    @Test
    public void filterMyTransportCarParkTest1() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.filterMyTransportCarPark(null));
    }

    //To test if filterMyTransportCarPark method with empty inputs
    @Test
    public void filterMyTransportCarParkTest2() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.filterMyTransportCarPark(new ArrayList<>()));
    }

    //To test if filterAllCarPark method with null as inputs
    @Test
    public void filterAllCarParkTest1() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.filterAllCarPark(
                null,
                null));
    }

    //To test if filterAllCarPark method with empty inputs
    @Test
    public void filterAllCarParkTest2() {
        assertNotNull(com.arkapp.carparknaviagation.ui.home.Utils.filterAllCarPark(
                new ArrayList<>(),
                new ArrayList<>()));
    }
}
