package com.cgg.ghmcpollingapp.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.google.gson.Gson;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

public class PollingApplication extends MultiDexApplication {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

//        int currentVersion = Build.VERSION.SDK_INT;
//        if (currentVersion <= Build.VERSION_CODES.KITKAT_WATCH){
//            try {
//                ProviderInstaller.installIfNeeded(getApplicationContext());
//                SSLContext sslContext;
//                sslContext = SSLContext.getInstance("TLSv1.2");
//                sslContext.init(null, null, null);
//                sslContext.createSSLEngine();
//            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException
//                    | NoSuchAlgorithmException | KeyManagementException e) {
//                e.printStackTrace();
//            }
//        }

    }

    public static PollingApplication get(Context context) {
        return (PollingApplication) context.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public SharedPreferences getPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREF, MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public SharedPreferences.Editor getPreferencesEditor() {
        if (editor == null) {
            editor = getPreferences().edit();
        }
        return editor;
    }

    public Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public Context getContext() {
        if (context == null) {
            context = getApplicationContext();
        }
        return context;
    }
}
