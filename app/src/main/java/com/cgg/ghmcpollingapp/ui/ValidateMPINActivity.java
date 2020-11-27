package com.cgg.ghmcpollingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.application.PollingApplication;
import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.cgg.ghmcpollingapp.databinding.ActivityValidateMpinBinding;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandler;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.model.request.login.LoginRequest;
import com.cgg.ghmcpollingapp.model.request.mpin.GenerateMPINRequest;
import com.cgg.ghmcpollingapp.model.response.login.LoginResponse;
import com.cgg.ghmcpollingapp.model.response.mpin.MPINResponse;
import com.cgg.ghmcpollingapp.utils.CustomProgressDialog;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.cgg.ghmcpollingapp.viewmodel.GenerateMPINViewModel;
import com.cgg.ghmcpollingapp.viewmodel.LoginViewModel;
import com.google.gson.Gson;

public class ValidateMPINActivity extends AppCompatActivity implements ErrorHandlerInterface {

    private ActivityValidateMpinBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    Context context;
    private LoginViewModel loginViewModel;
    LoginResponse loginResponse;
    Gson gson;
    String mobNo, mpin;
    CustomProgressDialog customProgressDialog;
    GenerateMPINViewModel generateMPINViewModel;
    private String tokenId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_validate_mpin);
        context = ValidateMPINActivity.this;

        sharedPreferences = PollingApplication.get(this).getPreferences();
        editor = sharedPreferences.edit();
        loginViewModel = new LoginViewModel(context);
        customProgressDialog = new CustomProgressDialog(context);
        generateMPINViewModel = new GenerateMPINViewModel(context, getApplication());

        gson = PollingApplication.get(this).getGson();
        mobNo = sharedPreferences.getString(AppConstants.MOBILE_NO, "");
        mpin = sharedPreferences.getString(AppConstants.mPin, "");
        tokenId = sharedPreferences.getString(AppConstants.TOKEN_ID, "");
        String response = sharedPreferences.getString(AppConstants.LOGIN_RES, "");
        loginResponse = gson.fromJson(response, LoginResponse.class);
        if (!(loginResponse != null && loginResponse.getLoginData() != null && loginResponse
                .getLoginData().get(0) != null)) {

            Utils.customErrorAlert(context, getString(R.string.app_name),
                    getString(R.string.something) + " while fetching login response onCreate");
        }
        set6DigitText(mobNo);
        binding.loggedIn.setText(loginResponse.getLoginData().get(0).getName());

        binding.tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    forgotMPINCall();
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

        binding.firstPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s!=null && s.length()==4){
                    Utils.hideKeyboard(context, binding.firstPinView);
                }
            }
        });

        loginViewModel.getLoginCall().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {

                if (loginResponse != null && loginResponse.getStatusCode() != null) {

                    if (loginResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                        Utils.customSessionAlert(ValidateMPINActivity.this, getString(R.string.app_name),
                                loginResponse.getResponseMessage());
                    } else if (loginResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                        if (loginResponse.getLoginData().get(0) != null && loginResponse.getLoginData().get(0).getMobileNo() != null) {
                            String loginRes = new Gson().toJson(loginResponse);
                            editor.putString(AppConstants.MOBILE_NO, loginResponse.getLoginData().get(0).getMobileNo());
                            editor.putString(AppConstants.LOGIN_RES, loginRes);
                            editor.putString(AppConstants.ZONE_ID, loginResponse.getLoginData().get(0).getZoneID());
                            editor.putString(AppConstants.CIRCLE_ID, loginResponse.getLoginData().get(0).getCircleID());
                            editor.putString(AppConstants.WARD_ID, loginResponse.getLoginData().get(0).getWardID());
                            editor.putString(AppConstants.SECTOR_ID, loginResponse.getLoginData().get(0).getSectorID());
                            editor.putString(AppConstants.ZONE_NAME, loginResponse.getLoginData().get(0).getZoneName());
                            editor.putString(AppConstants.CIRCLE_NAME, loginResponse.getLoginData().get(0).getCircleName());
                            editor.putString(AppConstants.WARD_NAME, loginResponse.getLoginData().get(0).getWardName());
                            editor.putString(AppConstants.SECTOR_NAME, loginResponse.getLoginData().get(0).getSectorName());
                            editor.putString(AppConstants.mPin, loginResponse.getLoginData().get(0).getMPIN());
                            editor.putString(AppConstants.TOKEN_ID, loginResponse.getLoginData().get(0).getTokenID());
                            editor.commit();

                            if (!TextUtils.isEmpty(loginResponse.getLoginData().get(0).getIsSectorMapped()) &&
                                    !loginResponse.getLoginData().get(0).getIsSectorMapped().equalsIgnoreCase(AppConstants.TRUE)) {
                                binding.firstPinView.setText("");
                                startActivity(new Intent(context, MapSectorActivity.class));
                            } else {
                                binding.firstPinView.setText("");
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
            public void onClick(View v) {
                validateMPIN();
            }
        });
    }

    private void set6DigitText(String mobileNumber) {
        String maskedMobNum = mobileNumber.replaceAll("\\w(?=\\w{4})", "*");
        binding.empEmailTv.setText(getString(R.string.logged_with) + " " + maskedMobNum);
    }

    private void forgotMPINCall() {
        if (Utils.checkInternetConnection(context)) {
            customProgressDialog.show();
            GenerateMPINRequest generateMPINRequest = new GenerateMPINRequest();
            generateMPINRequest.setMobileNo(mobNo);
            generateMPINRequest.setmPIN(null);
            generateMPINRequest.setTokenID(tokenId);
            LiveData<MPINResponse> mpinResponseLiveData = generateMPINViewModel.generateMPINCall(generateMPINRequest);
            mpinResponseLiveData.observe(ValidateMPINActivity.this, new Observer<MPINResponse>() {
                @Override
                public void onChanged(MPINResponse mpinResponse) {
                    customProgressDialog.dismiss();
                    mpinResponseLiveData.removeObservers(ValidateMPINActivity.this);
                    if (mpinResponse != null && mpinResponse.getStatusCode() != null) {
                        if (mpinResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                            Utils.customSessionAlert(ValidateMPINActivity.this, getString(R.string.app_name),
                                    mpinResponse.getResponseMessage());
                        } else if (mpinResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                            editor.clear();
                            editor.commit();
                            startActivity(new Intent(ValidateMPINActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        } else {
                            customProgressDialog.dismiss();
                            Utils.customErrorAlert(context, getString(R.string.app_name),
                                    mpinResponse.getResponseMessage());
                        }
                    } else {
                        customProgressDialog.dismiss();
                        Utils.customErrorAlert(context, getString(R.string.app_name),
                                getString(R.string.server_not));
                    }
                }
            });
        } else {
            Utils.customErrorAlert(context, getResources().getString(R.string.app_name_release), getString(R.string.plz_check_int));
        }
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

        } else if (!binding.firstPinView.getText().toString().equalsIgnoreCase(mpin)) {
            binding.firstPinView.setError(context.getString(R.string.invalid_mpin));
            binding.firstPinView.requestFocus();
            return;

        } else {
            binding.firstPinView.setError(null);
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setMobileNo(mobNo);
            loginRequest.setDeviceID(Utils.getDeviceID(context));
            loginRequest.setIPAddress(Utils.getLocalIpAddress());
            callLogin(loginRequest);
        }


    }

    private void callLogin(LoginRequest loginRequest) {
        if (Utils.checkInternetConnection(context)) {
            loginViewModel.callLoginAPI(loginRequest, binding.btnSubmit);
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

    @Override
    public void onBackPressed() {
        Intent newIntent = new Intent(ValidateMPINActivity.this, QuitAppActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
        finish();
    }
}