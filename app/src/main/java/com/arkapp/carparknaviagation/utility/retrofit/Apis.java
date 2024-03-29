package com.arkapp.carparknaviagation.utility.retrofit;

import com.arkapp.carparknaviagation.data.models.eta.Eta;
import com.arkapp.carparknaviagation.data.models.myTransportCarPark.MyTransportCarPark;
import com.arkapp.carparknaviagation.data.models.uraCarPark.Token;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCarPark;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCarParkCharges;

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

//Retrofit callback created with api urls.
public interface Apis {

    @Headers({"Accept: application/json"})
    @GET("CarParkAvailabilityv2")
    Call<MyTransportCarPark> getCarParkData(@Header("AccountKey") String key);

    @POST("insertNewToken.action")
    Call<Token> getCarParkToken(@Header("AccessKey") String key);

    @POST("invokeUraDS?service=Car_Park_Details")
    Call<UraCarParkCharges> getUraCarParkCharges(@Header("AccessKey") String key,
                                                 @Header("Token") String token);

    @POST("invokeUraDS?service=Car_Park_Availability")
    Call<UraCarPark> getCarParkAvailability(@Header("AccessKey") String key,
                                            @Header("Token") String token);

    @GET
    Call<Eta> getCarParkEta(@Url String url);

}
