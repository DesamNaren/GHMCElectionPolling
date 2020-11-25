package com.cgg.ghmcpollingapp.ui;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.cgg.ghmcpollingapp.BuildConfig;
import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.application.PollingApplication;
import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.cgg.ghmcpollingapp.databinding.ActivitySplashBinding;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandler;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.model.response.login.LoginResponse;
import com.cgg.ghmcpollingapp.network.GHMCService;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.google.gson.Gson;

import org.w3c.dom.Text;

public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 2000;
    private Context context;
    private String appVersion;
    private ActivitySplashBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String mPIN;
    private String buildVariant;
    LoginResponse loginResponse;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        context = SplashActivity.this;

        sharedPreferences = PollingApplication.get(context).getPreferences();
        editor = sharedPreferences.edit();
        gson=PollingApplication.get(this).getGson();
        String response=sharedPreferences.getString(AppConstants.LOGIN_RES,"");
        loginResponse=gson.fromJson(response,LoginResponse.class);
        mPIN=sharedPreferences.getString(AppConstants.mPin,"");

        buildVariant = BuildConfig.BUILD_TYPE;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!TextUtils.isEmpty(mPIN)) {
                        startActivity(new Intent(SplashActivity.this, ValidateMPINActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }

                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, 3000);
        appVersion = Utils.getVersionName(this);

    }
}


