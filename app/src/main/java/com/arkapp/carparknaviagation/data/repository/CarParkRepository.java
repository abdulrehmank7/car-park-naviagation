package com.arkapp.carparknaviagation.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.arkapp.carparknaviagation.data.models.carPark.AllCarPark;
import com.arkapp.carparknaviagation.data.models.carPark.AllCarParkAvailability;
import com.arkapp.carparknaviagation.data.models.carPark.Token;
import com.arkapp.carparknaviagation.data.models.carParking.CarParking;
import com.arkapp.carparknaviagation.data.models.eta.Eta;
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
public class CarParkRepository {
    private final Apis apis;
    private final Apis apiNew;
    private final Apis apiGoogle;

    public CarParkRepository() {
        apis = RetrofitService.createService(Apis.class, CAR_PARK_API_1_BASE_URL);
        apiNew = RetrofitService.createService(Apis.class, CAR_PARK_API_2_BASE_URL);
        apiGoogle = RetrofitService.createService(Apis.class, CAR_PARK_API_2_BASE_URL);
    }

    public MutableLiveData<CarParking> getCarParkAvailability() {

        MutableLiveData<CarParking> newsData = new MutableLiveData<>();
        apis.getCarParkData(CAR_PARK_API_KEY).enqueue(new Callback<CarParking>() {
            @Override
            public void onResponse(Call<CarParking> call,
                                   Response<CarParking> response) {
                if (response.isSuccessful()) {
                    newsData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<CarParking> call, Throwable t) {
                newsData.setValue(null);
            }
        });
        return newsData;
    }

    public MutableLiveData<String> getCarParkApiToken() {

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

    public MutableLiveData<AllCarParkAvailability> getAllCarParkAvailability(String token) {

        MutableLiveData<AllCarParkAvailability> data = new MutableLiveData<>();
        apiNew.getCarParkAvailability(CAR_PARK_API_ACCESS_KEY, token).enqueue(new Callback<AllCarParkAvailability>() {
            @Override
            public void onResponse(Call<AllCarParkAvailability> call,
                                   Response<AllCarParkAvailability> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AllCarParkAvailability> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public MutableLiveData<AllCarPark> getAllCarParkCharges(String token) {

        MutableLiveData<AllCarPark> data = new MutableLiveData<>();
        apiNew.getCarParkCharges(CAR_PARK_API_ACCESS_KEY, token).enqueue(new Callback<AllCarPark>() {
            @Override
            public void onResponse(Call<AllCarPark> call, Response<AllCarPark> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AllCarPark> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

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
