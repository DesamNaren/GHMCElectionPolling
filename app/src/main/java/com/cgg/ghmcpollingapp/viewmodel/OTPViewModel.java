package com.cgg.ghmcpollingapp.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.model.request.login.LoginRequest;
import com.cgg.ghmcpollingapp.model.response.login.LoginResponse;
import com.cgg.ghmcpollingapp.utils.CustomProgressDialog;

public class OTPViewModel extends ViewModel {

    private MutableLiveData<LoginResponse> loginMutableLiveData;
    private Context context;
    private ErrorHandlerInterface errorHandlerInterface;
    private CustomProgressDialog customProgressDialog;

    public OTPViewModel(Context context) {
        this.context = context;
        customProgressDialog = new CustomProgressDialog(context);
        errorHandlerInterface = (ErrorHandlerInterface) context;
        loginMutableLiveData = new MutableLiveData<>();
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
