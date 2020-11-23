package com.cgg.ghmcpollingapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.model.mpin.MPINResponse;
import com.cgg.ghmcpollingapp.network.GHMCService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenerateMPINViewModel extends AndroidViewModel {
    private MutableLiveData<MPINResponse> mpinResponseMutableLiveData;
    private Context context;
    private ErrorHandlerInterface errorHandlerInterface;

    public GenerateMPINViewModel(Context context, Application application) {
        super(application);
        this.context = context;
        mpinResponseMutableLiveData = new MutableLiveData<>();
        errorHandlerInterface = (ErrorHandlerInterface) context;
    }

    public LiveData<MPINResponse> generateMPINCall(String token, String userName, String mPIN, String header) {
        if (mpinResponseMutableLiveData != null) {
            generateMPINCallService(token, userName,mPIN,  header);
        }
        return mpinResponseMutableLiveData;
    }

    private void generateMPINCallService(String token, String userName, String mPIN, String headerType) {
        GHMCService ghmcService = GHMCService.Factory.create();

//        ghmcService.updateMPINResponse(token, userName, mPIN, headerType).enqueue(new Callback<MPINResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<MPINResponse> call, @NonNull Response<MPINResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    mpinResponseMutableLiveData.setValue(response.body());
//                }else {
//                    errorHandlerInterface.handleError(context.getString(R.string.server_not), context);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<MPINResponse> call, @NonNull Throwable t) {
//                errorHandlerInterface.handleError(t, context);
//            }
//        });
    }

}

