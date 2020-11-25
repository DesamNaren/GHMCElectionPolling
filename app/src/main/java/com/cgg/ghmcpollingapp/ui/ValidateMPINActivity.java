package com.cgg.ghmcpollingapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.application.PollingApplication;
import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.cgg.ghmcpollingapp.databinding.ActivityValidateMpinBinding;
import com.cgg.ghmcpollingapp.model.response.login.LoginResponse;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.cgg.ghmcpollingapp.viewmodel.LoginViewModel;
import com.google.gson.Gson;

public class ValidateMPINActivity extends AppCompatActivity {

    private ActivityValidateMpinBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    Context context;
    private LoginViewModel loginViewModel;
    LoginResponse loginResponse;
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_validate_mpin);
        context= ValidateMPINActivity.this;

        sharedPreferences = PollingApplication.get(this).getPreferences();
        editor = sharedPreferences.edit();
        loginViewModel=new LoginViewModel(context);

        gson=PollingApplication.get(this).getGson();
        String response=sharedPreferences.getString(AppConstants.LOGIN_RES,"");
        loginResponse=gson.fromJson(response,LoginResponse.class);
        if (!(loginResponse != null && loginResponse.getLoginData() != null && loginResponse
                .getLoginData().get(0) != null)) {

            Utils.customErrorAlert(context, getString(R.string.app_name),
                    getString(R.string.something) + " while fetching login response onCreate");
        }
        binding.tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    forgotMPINCall(username);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.tvNotYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
                Intent newIntent = new Intent(context, LoginActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
                finish();
            }
        });


        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateMPIN();
            }
        });
    }
    public void validateMPIN() {
        if (binding.firstPinView.getText() == null ||
                TextUtils.isEmpty(binding.firstPinView.getText().toString())) {
            binding.firstPinView.setError(context.getString(R.string.enter_4_digit_mpin));
            binding.firstPinView.requestFocus();
            return;

        } else if (binding.firstPinView.getText().toString().length() < 4) {
            binding.firstPinView.setError(context.getString(R.string.please_enter_4_digit_mpin));
            binding.firstPinView.requestFocus();
            return;

        }else if (!binding.firstPinView.getText().toString().equalsIgnoreCase(loginResponse.getLoginData().get(0).getMPIN())) {
            binding.firstPinView.setError(context.getString(R.string.invalid_mpin));
            binding.firstPinView.requestFocus();
            return;

        } else {
            binding.firstPinView.setError(null);
        }

        loginViewModel.getLoginCall().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {

                if (loginResponse != null && loginResponse.getStatusCode() != null) {

                    if (loginResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

                        if (loginResponse.getLoginData().get(0) != null && loginResponse.getLoginData().get(0).getMobileNo() != null) {
                            String loginRes = new Gson().toJson(loginResponse);
                            editor.putString(AppConstants.MOBILE_NO, loginResponse.getLoginData().get(0).getMobileNo());
                            editor.putString(AppConstants.LOGIN_RES, loginRes);
                            editor.putString(AppConstants.ZONE_ID,loginResponse.getLoginData().get(0).getZoneID());
                            editor.putString(AppConstants.CIRCLE_ID,loginResponse.getLoginData().get(0).getCircleID());
                            editor.putString(AppConstants.WARD_ID,loginResponse.getLoginData().get(0).getWardID());
                            editor.putString(AppConstants.SECTOR_ID,loginResponse.getLoginData().get(0).getSectorID());
                            editor.commit();

                            if (!TextUtils.isEmpty(loginResponse.getLoginData().get(0).getIsSectorMapped()) &&
                                    !loginResponse.getLoginData().get(0).getIsSectorMapped().equalsIgnoreCase(AppConstants.TRUE)) {
                                startActivity(new Intent(context, MapSectorActivity.class));
                            } else {
                                startActivity(new Intent(context, DashboardActivity.class));
                            }
                        } else {
                            Utils.customErrorAlert(context, getString(R.string.app_name), getString(R.string.something));
                        }

                    } else if (loginResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                        Utils.customErrorAlert(context, getString(R.string.app_name), loginResponse.getResponseMessage());
                    } else {
                        Utils.customErrorAlert(context, getString(R.string.app_name), getString(R.string.something));
                    }
                } else {
                    Utils.customErrorAlert(context, getString(R.string.app_name), getString(R.string.server_not));
                }
            }
        });


    }
}