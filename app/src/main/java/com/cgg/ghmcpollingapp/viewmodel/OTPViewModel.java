package com.cgg.ghmcpollingapp.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.model.login.LoginRequest;
import com.cgg.ghmcpollingapp.model.login.LoginResponse;
import com.cgg.ghmcpollingapp.model.otp.DeviceOTPResponse;
import com.cgg.ghmcpollingapp.network.GHMCService;
import com.cgg.ghmcpollingapp.utils.CustomProgressDialog;

import retrofit2.Call;
import retrofit2.Response;

public class OTPViewModel extends ViewModel {

    private MutableLiveData<LoginResponse> loginMutableLiveData;
    private MutableLiveData<DeviceOTPResponse> deviceOTPResponseMutableLiveData;
    private Context context;
    private ErrorHandlerInterface errorHandlerInterface;
    private CustomProgressDialog customProgressDialog;

    public OTPViewModel(Context context) {
        this.context = context;
        customProgressDialog = new CustomProgressDialog(context);
        errorHandlerInterface = (ErrorHandlerInterface) context;
        loginMutableLiveData = new MutableLiveData<>();
        deviceOTPResponseMutableLiveData = new MutableLiveData<>();
    }


    public LiveData<LoginResponse> callResendOTP(LoginRequest loginRequest) {
        loginMutableLiveData = new MutableLiveData<>();
//        callLoginAPI(loginRequest);
        return loginMutableLiveData;
    }

//
//    private void callLoginAPI(LoginRequest loginRequest) {
//        customProgressDialog.show();
//        GHMCService virtuoService = GHMCService.Factory.create();
//
//        virtuoService.getLoginResponse(loginRequest.getUserName(), loginRequest.getMobileNumber(),
//                loginRequest.getPassword(),
//                loginRequest.getDeviceId(), loginRequest.getDeviceId(), loginRequest.getFcmToken(), AppConstants.ANDROID)
//                .enqueue(new Callback<LoginResponse>() {
//                    @Override
//                    public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
//                        customProgressDialog.dismiss();
//
//                        if (response.isSuccessful() && response.body() != null) {
//                            loginMutableLiveData.setValue(response.body());
//                        } else {
//                            errorHandlerInterface.handleError(context.getString(R.string.server_not), context);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
//                        customProgressDialog.dismiss();
//                        errorHandlerInterface.handleError(t, context);
//                    }
//                });
//    }

}
