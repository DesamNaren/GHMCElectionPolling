package com.cgg.ghmcpollingapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.cgg.ghmcpollingapp.databinding.ActivityGenerateMpinBinding;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandler;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.interfaces.GenerateMPINInterface;
import com.cgg.ghmcpollingapp.model.request.mpin.GenerateMPINRequest;
import com.cgg.ghmcpollingapp.model.response.login.LoginResponse;
import com.cgg.ghmcpollingapp.model.response.mpin.MPINResponse;
import com.cgg.ghmcpollingapp.utils.CustomProgressDialog;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.cgg.ghmcpollingapp.viewmodel.GenerateMPINViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

public class GenerateMPINActivity extends AppCompatActivity implements ErrorHandlerInterface, GenerateMPINInterface {

    private Context context;
    private ActivityGenerateMpinBinding binding;
    private SharedPreferences.Editor editor;
    private GenerateMPINViewModel generateMPINViewModel;
    private CustomProgressDialog customProgressDialog;
    private LoginResponse loginResponse;
    private String mobNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = GenerateMPINActivity.this;

        customProgressDialog = new CustomProgressDialog(context);

        SharedPreferences sharedPreferences = PollingApplication.get(context).getPreferences();
        editor = PollingApplication.get(context).getPreferencesEditor();
        Gson gson = PollingApplication.get(context).getGson();


        binding =
                DataBindingUtil.setContentView(this, R.layout.activity_generate_mpin);

        generateMPINViewModel = new GenerateMPINViewModel(context, getApplication());

        try {

            String data = sharedPreferences.getString(AppConstants.LOGIN_RES, "");
            mobNum = sharedPreferences.getString(AppConstants.MOBILE_NO, "");
            loginResponse = gson.fromJson(data, LoginResponse.class);


            if (!(loginResponse != null && loginResponse.getLoginData() != null)) {
                Utils.customErrorAlert(context, getString(R.string.app_name_release), getString(R.string.something));
            }

        } catch (Exception e) {
            Toast.makeText(context, getString(R.string.something), Toast.LENGTH_SHORT).show();
        }

        try {
            binding.enterMPIN.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(binding.enterMPIN, InputMethodManager.SHOW_IMPLICIT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateMPIN();
            }
        });

        binding.enterMPIN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    String mPIN = s.toString();
                    if (mPIN.length() == 4) {
                        binding.confirmMPIN.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.confirmMPIN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    String mPIN = s.toString();
                    if (mPIN.length() == 4) {
                        Utils.hideKeyboard(context, binding.btnSubmit);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void validateMPIN() {
        if (TextUtils.isEmpty(binding.enterMPIN.getText())) {
            binding.enterMPIN.setError(context.getString(R.string.please_enter_4_digit_mpin));
            binding.enterMPIN.requestFocus();
            return;

        } else if (binding.enterMPIN.getText() != null && binding.enterMPIN.getText().toString().length() < 4) {
            binding.enterMPIN.setError(context.getString(R.string.please_enter_4_digit_mpin));
            binding.enterMPIN.requestFocus();
            return;

        } else {
            binding.enterMPIN.setError(null);
        }

        if (TextUtils.isEmpty(binding.confirmMPIN.getText())) {
            binding.confirmMPIN.setError(context.getString(R.string.please_enter_4_digit_confirm_mpin));
            binding.confirmMPIN.requestFocus();
            return;

        } else if (binding.confirmMPIN.getText() != null && binding.confirmMPIN.getText().toString().length() < 4) {
            binding.confirmMPIN.setError(context.getString(R.string.please_enter_4_digit_confirm_mpin));
            binding.confirmMPIN.requestFocus();
            return;

        } else {
            binding.confirmMPIN.setError(null);
        }

        VerifyMPIN(binding.enterMPIN.getText().toString(), binding.confirmMPIN.getText().toString());

    }

    private void VerifyMPIN(String mPin, String conMPin) {
        Utils.hideKeyboard(context, binding.btnSubmit);
        if (mPin.equals(conMPin)) {
            generateMPINCall(mobNum, mPin);
        } else {
            Snackbar.make(binding.rootCl, R.string.confirm_mpin, Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void generateMPINCall(String mobNum, String mPIN) {
        if (Utils.checkInternetConnection(context)) {
            customProgressDialog.show();
            GenerateMPINRequest generateMPINRequest = new GenerateMPINRequest();
            generateMPINRequest.setMobileNo(mobNum);
            generateMPINRequest.setmPIN(mPIN);
            generateMPINRequest.setTokenID(loginResponse.getLoginData().get(0).getTokenID());
            generateMPINViewModel.generateMPINCallService(generateMPINRequest);
        } else {
            Utils.customErrorAlert(context, getResources().getString(R.string.app_name_release), getString(R.string.plz_check_int));
        }
    }

    @Override
    public void handleError(Throwable e, Context context) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
        String errMsg = ErrorHandler.handleError(e, context);
        Utils.customErrorAlert(context, getString(R.string.app_name_release), errMsg);
    }

    @Override
    public void onBackPressed() {
        Utils.customCancelAlert(GenerateMPINActivity.this, getResources().getString(R.string.app_name_release),
                getString(R.string.cancel_otp_process), editor);
    }

    @Override
    public void handleError(String errMsg, Context context) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
        Utils.customErrorAlert(context, getString(R.string.app_name_release), errMsg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
    }

    @Override
    public void generateMPINResponse(MPINResponse mpinResponse) {
        customProgressDialog.dismiss();
        if (mpinResponse != null && mpinResponse.getStatusCode() != null) {
            if (mpinResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.customSessionAlert(GenerateMPINActivity.this, getString(R.string.app_name),
                        mpinResponse.getResponseMessage());
            } else if (mpinResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                editor.putString(AppConstants.mPin, binding.enterMPIN.getText().toString());
                editor.commit();
                Utils.customMPINSuccessAlert(GenerateMPINActivity.this, mpinResponse.getResponseMessage());
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
}
