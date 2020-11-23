package com.cgg.ghmcpollingapp.network;

import com.cgg.ghmcpollingapp.BuildConfig;
import com.cgg.ghmcpollingapp.model.request.login.LoginRequest;
import com.cgg.ghmcpollingapp.model.response.login.LoginResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GHMCService {
    static String BASE_URL = BuildConfig.SERVER_URL;
    class Factory {
        public static GHMCService create() {

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            return retrofit.create(GHMCService.class);
        }
    }

    @POST("PettyTradeLoginDetails")
    Call<LoginResponse> getLoginResponse(@Body LoginRequest loginRequest);
}



