package com.cgg.ghmcpollingapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.cgg.ghmcpollingapp.databinding.ActivityOtpBinding;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandler;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.model.response.login.LoginResponse;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.cgg.ghmcpollingapp.viewmodel.LoginViewModel;
import com.cgg.ghmcpollingapp.viewmodel.OTPViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

public class OTPActivity extends AppCompatActivity implements ErrorHandlerInterface {

    private Context context;
    private LoginViewModel loginViewModel;
    private ActivityOtpBinding binding;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private OTPViewModel otpViewModel;
    private String username, password, mobileNum, devID, fcmToken;
    private LoginResponse loginResponse;

    private BroadcastReceiver mIntentReceiver;
    private String email, pic, empName;
    private int cnt = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = OTPActivity.this;

        SharedPreferences sharedPreferences =getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();


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

        loginViewModel = new LoginViewModel(context);
        otpViewModel = new OTPViewModel(context);
        binding.setViewModel(otpViewModel);


        try {
            devID = Utils.getDeviceID(context);
            
            String data = sharedPreferences.getString(AppConstants.LOGIN_RES, "");
            loginResponse = gson.fromJson(data, LoginResponse.class);
//
//            if (loginResponse != null && loginResponse.getData() != null &&
//                    !TextUtils.isEmpty(loginResponse.getData().getOtpMobile())
//                    && !TextUtils.isEmpty(loginResponse.getData().getMobileNumber())) {
//            } else {
//                Utils.customErrorAlert(context, getString(R.string.app_name_release), getString(R.string.something));
//            }

        } catch (Exception e) {
            Toast.makeText(context, getString(R.string.something), Toast.LENGTH_SHORT).show();
        }


        binding.tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnt++;
                binding.firstPinView.setText("");
//                LoginRequest loginRequest = new LoginRequest(username,
//                        password, mobileNum,
//                        devID, devID, fcmToken);
//
//                if (TextUtils.isEmpty(password)) {
//                    loginRequest.setUserName(null);
//                } else {
//                    loginRequest.setMobileNumber(null);
//                }

//                otpViewModel.callResendOTP(loginRequest).observe(com.cgg.virtuo.ui.general.OTPActivity.this, loginResponse -> {
//
//                    OTPActivity.this.loginResponse = loginResponse;
//                    if (loginResponse != null && loginResponse.getStatusCode() != null) {
//                        if (loginResponse.getStatusCode() == AppConstants.SESSION_EXPIRE) {
//                            Utils.customSessionAlert(com.cgg.virtuo.ui.general.OTPActivity.this, getString(R.string.app_name_release),
//                                    getString(R.string.session_expire), editor);
//                        } else if (loginResponse.getStatusCode() == AppConstants.SUCCESS_CODE &&
//                                loginResponse.getData() != null) {
//                            otpTimer();
//                            storeLoginRes(loginResponse);
//                        } else if (loginResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
//                            Utils.customErrorAlert(context, getString(R.string.app_name_release), loginResponse.getStatusMessage());
//                        } else {
//                            Utils.customErrorAlert(context, getString(R.string.app_name_release), getString(R.string.something));
//                        }
//                    } else {
//                        Utils.customErrorAlert(context, getString(R.string.app_name_release), getString(R.string.server_not));
//
//                    }
//                });
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

                } else if (binding.firstPinView.getText().toString().length() < 6) {
                    binding.firstPinView.setError(context.getString(R.string.please_enter_6_digit_otp));
                    binding.firstPinView.requestFocus();

                } else {
//                    if (loginResponse != null && loginResponse.getData() != null
//                            && !TextUtils.isEmpty(loginResponse.getData().getOtpMobile())) {
//                        VerifyOTP(binding.firstPinView.getText().toString(),
//                                loginResponse.getData().getOtpMobile());
//                    } else {
//                        Toast.makeText(context, R.string.otpempty, Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        });

    }


    private void VerifyOTP(String otp, String actOtp) {
        Utils.hideKeyboard(context, binding.btnSubmit);
        if (otp.equals(actOtp)) {
            binding.firstPinView.setError(null);
//            context.startActivity(new Intent(context, GenerateMPINActivity.class));
        } else {
            Snackbar.make(binding.rootCl, R.string.invalid_otp, Snackbar.LENGTH_SHORT).show();
            binding.firstPinView.requestFocus();
        }
    }

    @Override
    public void handleError(Throwable e, Context context) {
        String errMsg = ErrorHandler.handleError(e, context);
        Utils.customErrorAlert(context, getString(R.string.app_name_release), errMsg);
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
    protected void onResume() {
        super.onResume();
    }

    private BroadcastReceiver mGpsSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent != null && intent.getAction() != null &&
                        intent.getAction().matches("android.provider.Telephony.SMS_RECEIVED")) {

                    Bundle data = intent.getExtras();
                    Object[] pdus = new Object[0];
                    if (data != null) {
                        pdus = (Object[]) data.get("pdus");
                    }
                    if (pdus != null) {
                        for (Object o : pdus) {
                            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) o);
                            String sender = smsMessage.getDisplayOriginatingAddress();
                            String messageBody = smsMessage.getMessageBody();
                            if (sender.contains("CGGHRM")) {
                                Intent smsIntent = new Intent("OTP");
                                smsIntent.putExtra("message", messageBody);
                                LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);
                                if (messageBody.contains("Your one time password")) {
                                    String[] splitStr = messageBody.split("\\.");
                                    String otp = splitStr[0].substring(splitStr[0].length() - 6);
                                    binding.firstPinView.setText(otp);
                                    if (TextUtils.isEmpty(binding.firstPinView.getText())) {
                                        binding.firstPinView.setError(context.getString(R.string.please_enter_6_digit_otp));
                                        binding.firstPinView.requestFocus();

                                    } else if (binding.firstPinView.getText().toString().length() < 6) {
                                        binding.firstPinView.setError(context.getString(R.string.please_enter_6_digit_otp));
                                        binding.firstPinView.requestFocus();

                                    } else {
                                        Toast.makeText(context, R.string.otpverified, Toast.LENGTH_SHORT).show();

//                                        if (loginResponse != null && loginResponse.getData() != null) {
//                                            VerifyOTP(binding.firstPinView.getText().toString(), loginResponse.getData().getOtpMobile());
//                                        } else {
//                                            Toast.makeText(context, R.string.otpempty, Toast.LENGTH_SHORT).show();
//                                        }
                                    }
                                }

                            }


                        }
                    }
                }
            } catch (
                    Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
//        unregisterReceiver(mGpsSwitchStateReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }

    @Override
    public void handleError(String errMsg, Context context) {
        Utils.customErrorAlert(context, getString(R.string.app_name_release), errMsg);
    }
}
