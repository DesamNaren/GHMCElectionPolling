package com.cgg.ghmcpollingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.application.PollingApplication;
import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.cgg.ghmcpollingapp.databinding.ActivityLoginBinding;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandler;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.model.request.login.LoginRequest;
import com.cgg.ghmcpollingapp.model.response.login.LoginResponse;
import com.cgg.ghmcpollingapp.room.repository.PollingMasterRep;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.cgg.ghmcpollingapp.viewmodel.LoginCustomViewModel;
import com.cgg.ghmcpollingapp.viewmodel.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;


public class LoginActivity extends AppCompatActivity implements ErrorHandlerInterface {

    ActivityLoginBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LoginViewModel loginViewModel;
    private LoginRequest loginRequest;
    Context context;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        context = LoginActivity.this;

        binding.headerLyout.imgBack.setVisibility(View.INVISIBLE);

        sharedPreferences = PollingApplication.get(context).getPreferences();
        editor = PollingApplication.get(context).getPreferencesEditor();
        gson = PollingApplication.get(context).getGson();

        loginViewModel = new ViewModelProvider(
                this, new LoginCustomViewModel(binding, context)).
                get(LoginViewModel.class);
        binding.setViewModel(loginViewModel);

        new PollingMasterRep(getApplication());

        loginViewModel.getLoginCall().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {

                if (loginResponse != null && loginResponse.getStatusCode() != null) {

                    if (loginResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                        if (loginResponse.getLoginData().get(0) != null && loginResponse.getLoginData().get(0).getMobileNo() != null) {
                            String loginRes = gson.toJson(loginResponse);
                            editor.putString(AppConstants.MOBILE_NO, loginResponse.getLoginData().get(0).getMobileNo());
                            editor.putString(AppConstants.LOGIN_RES, loginRes);
                            editor.putString(AppConstants.ZONE_ID,loginResponse.getLoginData().get(0).getZoneID());
                            editor.putString(AppConstants.CIRCLE_ID,loginResponse.getLoginData().get(0).getCircleID());
                            editor.putString(AppConstants.WARD_ID,loginResponse.getLoginData().get(0).getWardID());
                            editor.putString(AppConstants.SECTOR_ID,loginResponse.getLoginData().get(0).getSectorID());
                            editor.commit();
                            if (TextUtils.isEmpty(loginResponse.getLoginData().get(0).getMPIN())) {
                                startActivity(new Intent(context, OTPActivity.class));
                            } else if (!TextUtils.isEmpty(loginResponse.getLoginData().get(0).getIsSectorMapped()) &&
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


        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginValidations();
            }
        });
    }

    private void loginValidations() {
        if (TextUtils.isEmpty(binding.etMobileNo.getText())) {
            callSnackBar(getString(R.string.enter_mob_no));
            binding.etMobileNo.requestFocus();
            return;

        } else {
            binding.etMobileNo.setError(null);
        }
        if ((!(binding.etMobileNo.getText().toString().trim().length() == 10) ||
                !((binding.etMobileNo.getText().toString().startsWith("9")) || (binding.etMobileNo.getText().toString().startsWith("8")) ||
                        (binding.etMobileNo.getText().toString().startsWith("7")) || (binding.etMobileNo.getText().toString().startsWith("6"))))) {
            callSnackBar(getString(R.string.enter_valid_mobile_no));
            binding.etMobileNo.requestFocus();
            return;

        } else {
            binding.etMobileNo.setError(null);
        }

        loginRequest = new LoginRequest();
        loginRequest.setMobileNo(binding.etMobileNo.getText().toString().trim());
        loginRequest.setDeviceID(Utils.getDeviceID(context));
        loginRequest.setIPAddress(Utils.getLocalIpAddress());
        callLogin(loginRequest);
    }

    private void callLogin(LoginRequest loginRequest) {
        if (Utils.checkInternetConnection(context)) {
            loginViewModel.callLoginAPI(loginRequest);
        } else {
            Utils.customErrorAlert(context, context.getResources().getString(R.string.app_name), context.getString(R.string.plz_check_int));
        }
    }

    @Override
    public void handleError(Throwable e, Context context) {
        String errMsg = ErrorHandler.handleError(e, context);
        Utils.customErrorAlert(context, getString(R.string.app_name_release), errMsg);
    }

    @Override
    public void handleError(String errMsg, Context context) {
        Utils.customErrorAlert(context, getString(R.string.app_name_release), errMsg);
    }

    void callSnackBar(String msg) {
        Snackbar snackbar = Snackbar.make(binding.relativeLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(getResources().getColor(R.color.white));
        snackbar.show();
    }
}
