package com.cgg.ghmcpollingapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.model.request.logout.LogoutRequest;
import com.cgg.ghmcpollingapp.model.request.mpin.GenerateMPINRequest;
import com.cgg.ghmcpollingapp.model.response.login.LoginResponse;
import com.cgg.ghmcpollingapp.model.response.logout.LogoutResponse;
import com.cgg.ghmcpollingapp.model.response.mpin.MPINResponse;
import com.cgg.ghmcpollingapp.network.GHMCService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogoutViewModel extends AndroidViewModel {
    private MutableLiveData<LogoutResponse> logoutResponseMutableLiveData;
    private Context context;
    private ErrorHandlerInterface errorHandlerInterface;

    public LogoutViewModel(Context context, Application application) {
        super(application);
        this.context = context;
        logoutResponseMutableLiveData = new MutableLiveData<>();
        errorHandlerInterface = (ErrorHandlerInterface) context;
    }

    public LiveData<LogoutResponse> logoutCall(LogoutRequest logoutRequest) {
        if (logoutResponseMutableLiveData != null) {
            logoutCallService(logoutRequest);
        }
        return logoutResponseMutableLiveData;
    }

    private void logoutCallService(LogoutRequest logoutRequest) {
        GHMCService ghmcService = GHMCService.Factory.create();

        ghmcService.logoutResponse(logoutRequest).enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(@NonNull Call<LogoutResponse> call, @NonNull Response<LogoutResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    logoutResponseMutableLiveData.setValue(response.body());
                }else {
                    errorHandlerInterface.handleError(context.getString(R.string.server_not), context);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LogoutResponse> call, @NonNull Throwable t) {
                errorHandlerInterface.handleError(t, context);
            }
        });
    }

}

