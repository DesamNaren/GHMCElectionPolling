package com.cgg.ghmcpollingapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.model.request.mpin.GenerateMPINRequest;
import com.cgg.ghmcpollingapp.network.GHMCService;

public class GenerateMPINViewModel extends AndroidViewModel {
    private MutableLiveData<GenerateMPINRequest> mpinResponseMutableLiveData;
    private Context context;
    private ErrorHandlerInterface errorHandlerInterface;

    public GenerateMPINViewModel(Context context, Application application) {
        super(application);
        this.context = context;
        mpinResponseMutableLiveData = new MutableLiveData<>();
        errorHandlerInterface = (ErrorHandlerInterface) context;
    }

    public LiveData<GenerateMPINRequest> generateMPINCall(String token, String userName, String mPIN, String header) {
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

