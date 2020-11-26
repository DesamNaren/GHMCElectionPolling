package com.cgg.ghmcpollingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.application.PollingApplication;
import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.cgg.ghmcpollingapp.databinding.ActivityOtpBinding;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandler;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.model.request.login.LoginRequest;
import com.cgg.ghmcpollingapp.model.response.login.LoginResponse;
import com.cgg.ghmcpollingapp.utils.CustomProgressDialog;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.cgg.ghmcpollingapp.viewmodel.OTPViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

public class OTPActivity extends AppCompatActivity implements ErrorHandlerInterface {

    private Context context;
    private ActivityOtpBinding binding;
    private SharedPreferences.Editor editor;
    private OTPViewModel otpViewModel;
    private LoginResponse loginResponse;
    private int cnt = 0;
    private Gson gson;
    private String mobNum;
    private CustomProgressDialog customProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = OTPActivity.this;

        customProgressDialog = new CustomProgressDialog(context);
        SharedPreferences sharedPreferences = PollingApplication.get(context).getPreferences();
        editor = PollingApplication.get(context).getPreferencesEditor();
        gson = PollingApplication.get(context).getGson();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);

        try {
            binding.firstPinView.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(binding.firstPinView, InputMethodManager.SHOW_IMPLICIT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        otpViewModel = new OTPViewModel(context);
        binding.setViewModel(otpViewModel);


        try {
            String data = sharedPreferences.getString(AppConstants.LOGIN_RES, "");
            mobNum = sharedPreferences.getString(AppConstants.MOBILE_NO, "");
            loginResponse = gson.fromJson(data, LoginResponse.class);

            if (!(loginResponse != null && loginResponse.getLoginData() != null &&
                    !TextUtils.isEmpty(loginResponse.getLoginData().get(0).getOTP())
                    && !TextUtils.isEmpty(mobNum))) {
                Utils.customErrorAlert(context, getString(R.string.app_name_release), getString(R.string.something_wrong_otp));
            }

            set6DigitText(mobNum);
            binding.loggedIn.setText(loginResponse.getLoginData().get(0).getName());
        } catch (Exception e) {
            Toast.makeText(context, getString(R.string.something), Toast.LENGTH_SHORT).show();
        }


        binding.tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.checkInternetConnection(context)) {
                    if (!TextUtils.isEmpty(mobNum)) {
                        customProgressDialog.show();
                        cnt++;
                        binding.firstPinView.setText("");
                        LoginRequest loginRequest = new LoginRequest();

                        loginRequest.setMobileNo(mobNum);
                        loginRequest.setDeviceID(Utils.getDeviceID(context));
                        loginRequest.setIPAddress(Utils.getLocalIpAddress());

                        otpViewModel.callResendOTP(loginRequest).observe(OTPActivity.this, loginResponse -> {
                            customProgressDialog.hide();
                            OTPActivity.this.loginResponse = loginResponse;
                            if (loginResponse != null && loginResponse.getStatusCode() != null) {
                                if (loginResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                                    Utils.customSessionAlert(OTPActivity.this, getString(R.string.app_name),
                                            loginResponse.getResponseMessage());
                                } else if (loginResponse.getStatusCode() == AppConstants.SUCCESS_CODE &&
                                        loginResponse.getLoginData() != null && loginResponse.getLoginData().get(0).getOTP() == null) {
                                    Utils.customErrorAlert(context, getString(R.string.app_name_release), getString(R.string.something_wrong_otp));
                                } else if (loginResponse.getStatusCode() == AppConstants.SUCCESS_CODE &&
                                        loginResponse.getLoginData() != null) {
                                    otpTimer();
                                    storeLoginRes(loginResponse);
                                } else if (loginResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                                    Utils.customErrorAlert(context, getString(R.string.app_name_release), loginResponse.getResponseMessage());
                                } else {
                                    Utils.customErrorAlert(context, getString(R.string.app_name_release), getString(R.string.something));
                                }
                            } else {
                                Utils.customErrorAlert(context, getString(R.string.app_name_release), getString(R.string.server_not));

                            }
                        });
                    }

                } else {
                    Utils.customErrorAlert(context, context.getResources().getString(R.string.app_name), context.getString(R.string.plz_check_int));
                }

            }
        });

        binding.firstPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    String otp = s.toString();
                    if (otp.length() == 6) {
                        Utils.hideKeyboard(context, binding.btnSubmit);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.firstPinView.getText())) {
                    binding.firstPinView.setError(context.getString(R.string.please_enter_6_digit_otp));
                    binding.firstPinView.requestFocus();

                } else if (binding.firstPinView.getText() != null && binding.firstPinView.getText().toString().length() < 6) {
                    binding.firstPinView.setError(context.getString(R.string.please_enter_6_digit_otp));
                    binding.firstPinView.requestFocus();

                } else {
                    if (loginResponse != null && loginResponse.getLoginData() != null
                            && !TextUtils.isEmpty(loginResponse.getLoginData().get(0).getOTP())) {
                        VerifyOTP(binding.firstPinView.getText().toString(),
                                loginResponse.getLoginData().get(0).getOTP());
                    } else {
                        Toast.makeText(context, R.string.otpempty, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void set6DigitText(String mobileNumber) {
        String maskedMobNum = mobileNumber.replaceAll("\\w(?=\\w{4})", "*");
        binding.enter6DigitOtp.setText(getString(R.string.enter_6_digit_otp) + " " + maskedMobNum);
    }

    private void storeLoginRes(LoginResponse loginResponse) {
        String loginRes = gson.toJson(loginResponse);
        editor.putString(AppConstants.MOBILE_NO, mobNum);
        editor.putString(AppConstants.LOGIN_RES, loginRes);
        editor.putString(AppConstants.ZONE_ID, loginResponse.getLoginData().get(0).getZoneID());
        editor.putString(AppConstants.CIRCLE_ID, loginResponse.getLoginData().get(0).getCircleID());
        editor.putString(AppConstants.WARD_ID, loginResponse.getLoginData().get(0).getWardID());
        editor.putString(AppConstants.SECTOR_ID, loginResponse.getLoginData().get(0).getSectorID());
        editor.putString(AppConstants.ZONE_NAME, loginResponse.getLoginData().get(0).getZoneName());
        editor.putString(AppConstants.CIRCLE_NAME, loginResponse.getLoginData().get(0).getCircleName());
        editor.putString(AppConstants.WARD_NAME, loginResponse.getLoginData().get(0).getWardName());
        editor.putString(AppConstants.SECTOR_NAME, loginResponse.getLoginData().get(0).getSectorName());
        editor.putString(AppConstants.TOKEN_ID, loginResponse.getLoginData().get(0).getTokenID());
        editor.putString(AppConstants.mPin, loginResponse.getLoginData().get(0).getMPIN());

        editor.commit();

        editor.commit();
    }


    private void VerifyOTP(String otp, String actOtp) {
        Utils.hideKeyboard(context, binding.btnSubmit);
        if (otp.equals(actOtp)) {
            binding.firstPinView.setError(null);
            context.startActivity(new Intent(context, GenerateMPINActivity.class));
        } else {
            Snackbar.make(binding.rootCl, R.string.invalid_otp, Snackbar.LENGTH_SHORT).show();
            binding.firstPinView.requestFocus();
        }
    }

    @Override
    public void onBackPressed() {
        Utils.customCancelAlert(OTPActivity.this, getResources().getString(R.string.app_name_release),
                getString(R.string.cancel_otp_process), editor);
    }

    private void otpTimer() {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.tvResend.setEnabled(false);
                binding.tvResend.setText(getString(R.string.resend_otp) + ": " + millisUntilFinished / 1000 + " Seconds");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                if (cnt < 3) {
                    binding.tvResend.setEnabled(true);
                    binding.tvResend.setText(getString(R.string.resend_otp));
                } else {
                    binding.tvResend.setVisibility(View.GONE);
                }
            }

        }.start();
    }

    @Override
    public void handleError(Throwable e, Context context) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.hide();
        String errMsg = ErrorHandler.handleError(e, context);
        Utils.customErrorAlert(context, getString(R.string.app_name), errMsg);
    }

    @Override
    public void handleError(String errMsg, Context context) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.hide();
        Utils.customErrorAlert(context, getString(R.string.app_name), errMsg);
    }
}
