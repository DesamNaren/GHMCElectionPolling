package com.cgg.ghmcpollingapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.interfaces.PSEntryInterface;
import com.cgg.ghmcpollingapp.model.request.psList.PSListRequest;
import com.cgg.ghmcpollingapp.model.request.ps_entry.PSEntrySubmitRequest;
import com.cgg.ghmcpollingapp.model.response.master.MasterTimeSlotData;
import com.cgg.ghmcpollingapp.model.response.psList.PSListResponse;
import com.cgg.ghmcpollingapp.model.response.ps_entry.PSEntryResponse;
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

    private LiveData<List<MasterTimeSlotData>> timeSlotDataLiveData;

    public PSEntryViewModel(Context context, Application application) {
        super(application);
        this.context = context;
        psEntryResponseMutableLiveData = new MutableLiveData<>();
        errorHandlerInterface = (ErrorHandlerInterface) context;
        psEntryInterface = (PSEntryInterface) context;
        pollingMasterRep = new PollingMasterRep(application);
        timeSlotDataLiveData = new MutableLiveData<>();

    }

   public LiveData<List<MasterTimeSlotData>> getTimeSlots() {
       if (timeSlotDataLiveData != null) {
           timeSlotDataLiveData = pollingMasterRep.getTimeSlots();
       }
       return timeSlotDataLiveData;
   }

    public void getPSList(PSListRequest reportRequest) {
        Gson gson = new Gson();
        String str = gson.toJson(reportRequest);
        GHMCService ghmcService = GHMCService.Factory.create();
        ghmcService.getPSList(reportRequest).enqueue(new Callback<PSListResponse>() {
            @Override
            public void onResponse(@NonNull Call<PSListResponse> call, @NonNull Response<PSListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    psEntryInterface.getPSList(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PSListResponse> call, @NonNull Throwable t) {
                errorHandlerInterface.handleError(t, context);
            }
        });
    }

    public void submitPSEntryCall(PSEntrySubmitRequest psEntrySubmitRequest) {
        Gson gson = new Gson();
        String str = gson.toJson(psEntrySubmitRequest);
        GHMCService virtuoService = GHMCService.Factory.create();
        virtuoService.getPSSubmitResponse(psEntrySubmitRequest).enqueue(new Callback<PSEntryResponse>() {
            @Override
            public void onResponse(@NonNull Call<PSEntryResponse> call, @NonNull Response<PSEntryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    psEntryInterface.submitPSEntry(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PSEntryResponse> call, @NonNull Throwable t) {
                errorHandlerInterface.handleError(t, context);
            }
        });
    }

}



