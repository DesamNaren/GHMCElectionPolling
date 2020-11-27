package com.cgg.ghmcpollingapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.model.request.login.LoginRequest;
import com.cgg.ghmcpollingapp.model.response.login.LoginResponse;
import com.cgg.ghmcpollingapp.network.GHMCService;
import com.cgg.ghmcpollingapp.room.repository.DownloadMasterRepository;
import com.cgg.ghmcpollingapp.source.PollingEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadMasterViewModel extends AndroidViewModel {
    private DownloadMasterRepository downloadMasterRepository;
    private MutableLiveData<LoginResponse> masterMutableLiveData;

    private Context context;
    private ErrorHandlerInterface errorHandlerInterface;
    private LiveData<List<PollingEntity>> masterPSData;
    private LiveData<List<PollingEntity>> masterTimeSlotData;

    public DownloadMasterViewModel(Context context, Application application) {
        super(application);
        this.context = context;
        masterMutableLiveData = new MutableLiveData<>();
        downloadMasterRepository = new DownloadMasterRepository(application);
        masterPSData = new MutableLiveData<>();
        masterTimeSlotData = new MutableLiveData<>();
        errorHandlerInterface = (ErrorHandlerInterface) context;
    }

    public LiveData<List<PollingEntity>> getMasterPSData() {
        if (masterPSData != null) {
            masterPSData = downloadMasterRepository.getAllPSData();
        }
        return masterPSData;
    }

    public LiveData<List<PollingEntity>> getMasterTimeSlotData() {
        if (masterTimeSlotData != null) {
            masterTimeSlotData = downloadMasterRepository.getAllTimeSlotData();
        }
        return masterTimeSlotData;
    }

    public LiveData<LoginResponse> getMasterResponse(String token) {
        if (masterMutableLiveData != null) {
            getMasterResponseCall(token);
        }
        return masterMutableLiveData;
    }

    private void getMasterResponseCall(String token) {
        GHMCService ghmcService = GHMCService.Factory.create();
        ghmcService.getLoginResponse(new LoginRequest()).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    masterMutableLiveData.setValue(response.body());
                } else {
                    errorHandlerInterface.handleError(context.getString(R.string.server_not), context);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                errorHandlerInterface.handleError(t, context);
            }
        });
    }
}



