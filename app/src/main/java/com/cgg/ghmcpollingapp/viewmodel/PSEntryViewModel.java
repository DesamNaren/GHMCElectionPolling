package com.cgg.ghmcpollingapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.interfaces.PSEntryInterface;
import com.cgg.ghmcpollingapp.model.request.ps_entry.PSEntryRequest;
import com.cgg.ghmcpollingapp.model.request.ps_entry.PSEntrySubmitRequest;
import com.cgg.ghmcpollingapp.model.response.ps_entry.PSEntryResponse;
import com.cgg.ghmcpollingapp.model.response.ps_entry.PSEntrySubmitResponse;
import com.cgg.ghmcpollingapp.network.GHMCService;
import com.cgg.ghmcpollingapp.room.repository.PollingMasterRep;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PSEntryViewModel extends AndroidViewModel {
    private PollingMasterRep pollingMasterRep;
    private MutableLiveData<PSEntryResponse> psEntryResponseMutableLiveData;
    private PSEntryInterface psEntryInterface;
    private Context context;
    private ErrorHandlerInterface errorHandlerInterface;

    private LiveData<List<String>> pollingStations;
    private LiveData<String> pollingStationId;

    public PSEntryViewModel(Context context, Application application) {
        super(application);
        this.context = context;
        psEntryResponseMutableLiveData = new MutableLiveData<>();
        errorHandlerInterface = (ErrorHandlerInterface) context;
        psEntryInterface = (PSEntryInterface) context;
        pollingMasterRep = new PollingMasterRep(application);
        pollingStations = new MutableLiveData<>();

    }


    public LiveData<List<String>> getPollingStations() {
        if (pollingStations != null) {
            pollingStations = pollingMasterRep.getPollingStations();
        }
        return pollingStations;
    }

    public LiveData<String> getPollingStationId(String psName, String zoneId, String circleId, String wardId, String sectorId) {
        if (pollingStationId != null) {
            pollingStationId = pollingMasterRep.getPollingStationId(psName, sectorId, zoneId, circleId, wardId);
        }
        return pollingStationId;
    }


    public void getPSDetails(PSEntryRequest psEntryRequest) {
        Gson gson = new Gson();
        String str = gson.toJson(psEntryRequest);
        GHMCService ghmcService = GHMCService.Factory.create();
        ghmcService.getPSDetailsResponse(psEntryRequest).enqueue(new Callback<PSEntryResponse>() {
            @Override
            public void onResponse(@NonNull Call<PSEntryResponse> call, @NonNull Response<PSEntryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    psEntryInterface.getPSDetails(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PSEntryResponse> call, @NonNull Throwable t) {
                errorHandlerInterface.handleError(t, context);
            }
        });
    }

    public void submitPSEntryCall(PSEntrySubmitRequest psEntrySubmitRequest) {
        Gson gson = new Gson();
        String str = gson.toJson(psEntrySubmitRequest);
        GHMCService virtuoService = GHMCService.Factory.create();
        virtuoService.getPSSubmitResponse(psEntrySubmitRequest).enqueue(new Callback<PSEntrySubmitResponse>() {
            @Override
            public void onResponse(@NonNull Call<PSEntrySubmitResponse> call, @NonNull Response<PSEntrySubmitResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    psEntryInterface.submitPSEntry(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PSEntrySubmitResponse> call, @NonNull Throwable t) {
                errorHandlerInterface.handleError(t, context);
            }
        });
    }

}


