package com.arkapp.carparknaviagation.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.arkapp.carparknaviagation.data.models.eta.Eta;
import com.arkapp.carparknaviagation.data.models.myTransportCarPark.MyTransportCarPark;
import com.arkapp.carparknaviagation.data.models.uraCarPark.Token;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCarPark;
import com.arkapp.carparknaviagation.utility.retrofit.Apis;
import com.arkapp.carparknaviagation.utility.retrofit.RetrofitService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.arkapp.carparknaviagation.utility.Constants.CAR_PARK_API_1_BASE_URL;
import static com.arkapp.carparknaviagation.utility.Constants.CAR_PARK_API_2_BASE_URL;
import static com.arkapp.carparknaviagation.utility.Constants.CAR_PARK_API_ACCESS_KEY;
import static com.arkapp.carparknaviagation.utility.Constants.CAR_PARK_API_KEY;
import static com.arkapp.carparknaviagation.utility.maps.eta.Utils.getEtaUrl;

/**
 * Created by Abdul Rehman on 28-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This is the Carpark repository used to get the data from server related to the carpark
 * availability, location, eta etc.
 */
public class CarParkRepository {
    private final Apis apis;
    private final Apis apiNew;
    private final Apis apiGoogle;

    public CarParkRepository() {
        /**
         * Creating the retrofit services for the api to get the data
         * From the Apis like MyTransport, URA, Google etc.
         */
        apis = RetrofitService.createService(Apis.class, CAR_PARK_API_1_BASE_URL);
        apiNew = RetrofitService.createService(Apis.class, CAR_PARK_API_2_BASE_URL);
        apiGoogle = RetrofitService.createService(Apis.class, CAR_PARK_API_2_BASE_URL);
    }

    /**
     * This method will get the carpark data from MyTransport website. It will contain all the
     * details of the carpark.
     */
    public MutableLiveData<MyTransportCarPark> getMyTransportCarParkAvailability() {

        MutableLiveData<MyTransportCarPark> newsData = new MutableLiveData<>();
        apis.getCarParkData(CAR_PARK_API_KEY).enqueue(new Callback<MyTransportCarPark>() {
            @Override
            public void onResponse(Call<MyTransportCarPark> call,
                                   Response<MyTransportCarPark> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MyTransportCarPark> call, Throwable t) {
                newsData.setValue(null);
            }
        });
        return newsData;
    }

    /**
     * This method will get the token for calling Ura Api. We will need this token before
     * calling the Ura carpark api.
     */
    public MutableLiveData<String> getUraCarParkApiToken() {

        MutableLiveData<String> data = new MutableLiveData<>();

        apiNew.getCarParkToken(CAR_PARK_API_ACCESS_KEY).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.body().getResult() != null) {
                    data.setValue(response.body().getResult());
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                data.setValue("");

            }
        });
        return data;
    }

    /**
     * This method will get the carpark data from Ura website. It will contain all the
     * details of the carpark.
     */
    public MutableLiveData<UraCarPark> getUraCarParkAvailability(String token) {

        MutableLiveData<UraCarPark> data = new MutableLiveData<>();
        apiNew.getCarParkAvailability(CAR_PARK_API_ACCESS_KEY, token).enqueue(new Callback<UraCarPark>() {
            @Override
            public void onResponse(Call<UraCarPark> call,
                                   Response<UraCarPark> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UraCarPark> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    /**
     * This method will get the ETA and distance of carpark from current location.
     * This data will be used to show the ETA and Distance on home screen.
     */
    public MutableLiveData<Eta> getCarParkEta(ArrayList<String> allCarParkLatLng,
                                              String fromLat,
                                              String fromLng) {

        MutableLiveData<Eta> data = new MutableLiveData<>();
        apiGoogle.getCarParkEta(getEtaUrl(allCarParkLatLng, fromLat, fromLng)).enqueue(new Callback<Eta>() {
            @Override
            public void onResponse(Call<Eta> call, Response<Eta> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Eta> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

}
