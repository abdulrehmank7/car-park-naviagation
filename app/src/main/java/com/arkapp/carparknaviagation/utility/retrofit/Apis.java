package com.arkapp.carparknaviagation.utility.retrofit;

import com.arkapp.carparknaviagation.data.models.carPark.AllCarPark;
import com.arkapp.carparknaviagation.data.models.carPark.AllCarParkAvailability;
import com.arkapp.carparknaviagation.data.models.carPark.Token;
import com.arkapp.carparknaviagation.data.models.carParking.CarParking;
import com.arkapp.carparknaviagation.data.models.eta.Eta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Abdul Rehman on 28-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public interface Apis {

    @Headers({"Accept: application/json"})
    @GET("CarParkAvailabilityv2")
    Call<CarParking> getCarParkData(@Header("AccountKey") String key);

    @POST("insertNewToken.action")
    Call<Token> getCarParkToken(@Header("AccessKey") String key);

    @POST("invokeUraDS?service=Car_Park_Details")
    Call<AllCarPark> getCarParkCharges(@Header("AccessKey") String key,
                                       @Header("Token") String token);

    @POST("invokeUraDS?service=Car_Park_Availability")
    Call<AllCarParkAvailability> getCarParkAvailability(@Header("AccessKey") String key,
                                                        @Header("Token") String token);

    @GET
    Call<Eta> getCarParkEta(@Url String url);

}
