package com.cgg.ghmcpollingapp.network;

import com.cgg.ghmcpollingapp.model.login.LoginRequest;
import com.cgg.ghmcpollingapp.model.login.LoginResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TLService {
    class Factory {
        public static TLService create() {

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GHMCURL.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            return retrofit.create(TLService.class);
        }
    }

    @POST("PettyTradeLoginDetails")
    Call<LoginResponse> getLoginResponse(@Body LoginRequest loginRequest);
}



