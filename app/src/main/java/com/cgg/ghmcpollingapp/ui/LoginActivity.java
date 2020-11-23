package com.cgg.ghmcpollingapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.databinding.ActivityLoginBinding;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandler;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.model.login.LoginRequest;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.cgg.ghmcpollingapp.viewmodel.LoginCustomViewModel;
import com.cgg.ghmcpollingapp.viewmodel.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;


public class LoginActivity extends AppCompatActivity implements ErrorHandlerInterface {

    ActivityLoginBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LoginViewModel loginViewModel;
    private LoginRequest loginRequest;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        context = LoginActivity.this;


        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();

        loginViewModel = new ViewModelProvider(
                this, new LoginCustomViewModel(binding, context)).
                get(LoginViewModel.class);
        binding.setViewModel(loginViewModel);
//
//        loginViewModel.getLoginCall().observe(this, new Observer<LoginResponse>() {
//            @Override
//            public void onChanged(LoginResponse loginResponse) {
//
//                if (loginResponse != null && loginResponse.getStatus() != null) {
//
//                    if (loginResponse.getStatus()== AppConstants.SUCCESS_CODE) {
//                        LoginResponse loginResponse1 = loginResponse;
//                        String str = new Gson().toJson(loginResponse1);
//                        editor.putString(AppConstants.LOGIN_RES, str);
//                        editor.putString(AppConstants.FROM_ACTIVITY, AppConstants.LOGIN);
//                        editor.commit();
//                        startActivity(new Intent(context, DashboardActivity.class));
////                        finish();
//                    } else if (loginResponse.getStatus()==AppConstants.FAILURE_CODE) {
//                        Utils.customErrorAlert(context, getString(R.string.app_name), loginResponse.getMessage());
//                    } else {
//                        Utils.customErrorAlert(context, getString(R.string.app_name), getString(R.string.something));
//                    }
//                } else {
//                    Utils.customErrorAlert(context, getString(R.string.app_name), getString(R.string.server_not));
//                }
//            }
//        });


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
        if ((!(binding.etMobileNo.getText().toString().trim().length()==10) ||
                !((binding.etMobileNo.getText().toString().startsWith("9")) || (binding.etMobileNo.getText().toString().startsWith("8")) ||
                        (binding.etMobileNo.getText().toString().startsWith("7")) || (binding.etMobileNo.getText().toString().startsWith("6"))))) {
            callSnackBar(getString(R.string.enter_valid_mobile_no));
            binding.etMobileNo.requestFocus();
            return;

        } else {
            binding.etMobileNo.setError(null);
        }

//        loginRequest = new LoginRequest();
//        loginRequest.setMobileNo(binding.etMobileNo.getText().toString().trim());
//        loginRequest.setPassword(binding.etPwd.getText().toString().trim());
//        editor.putString(AppConstants.MOBILE_NO,binding.etMobileNo.getText().toString().trim());
//        editor.putString(AppConstants.PWD,binding.etPwd.getText().toString().trim());
//        editor.commit();
//        callLogin(loginRequest);
    }

//    private void callLogin(LoginRequest loginRequest) {
//        if (Utils.checkInternetConnection(context)) {
//            loginViewModel.callLoginAPI(loginRequest);
//        } else {
//            Utils.customErrorAlert(context, context.getResources().getString(R.string.app_name), context.getString(R.string.plz_check_int));
//        }
//    }

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
