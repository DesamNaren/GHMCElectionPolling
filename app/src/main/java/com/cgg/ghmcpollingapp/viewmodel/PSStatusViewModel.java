package com.cgg.ghmcpollingapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.interfaces.PSEntryInterface;
import com.cgg.ghmcpollingapp.interfaces.PsStatusInterface;
import com.cgg.ghmcpollingapp.model.request.reports.ReportRequest;
import com.cgg.ghmcpollingapp.model.response.ps_entry.PSEntryResponse;
import com.cgg.ghmcpollingapp.model.response.report.ReportResponse;
import com.cgg.ghmcpollingapp.network.GHMCService;
import com.cgg.ghmcpollingapp.room.repository.PollingMasterRep;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PSStatusViewModel extends AndroidViewModel {
    private PollingMasterRep pollingMasterRep;
    private MutableLiveData<PSEntryResponse> psEntryResponseMutableLiveData;
    private PsStatusInterface psStatusInterface;
    private Context context;
    private ErrorHandlerInterface errorHandlerInterface;

//    private LiveData<List<PollingEntity>> pollingStations;
//    private LiveData<PollingEntity> pollingStationId;

    public PSStatusViewModel(Context context, Application application) {
        super(application);
        this.context = context;
        psEntryResponseMutableLiveData = new MutableLiveData<>();
        errorHandlerInterface = (ErrorHandlerInterface) context;
        psStatusInterface = (PsStatusInterface) context;
        pollingMasterRep = new PollingMasterRep(application);
//        pollingStations = new MutableLiveData<>();
//        pollingStationId = new MutableLiveData<>();

    }


    public void getReports(ReportRequest reportRequest) {
        Gson gson = new Gson();
        String str = gson.toJson(reportRequest);
        GHMCService ghmcService = GHMCService.Factory.create();
        ghmcService.getReport(reportRequest).enqueue(new Callback<ReportResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReportResponse> call, @NonNull Response<ReportResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    psStatusInterface.reportsResponse(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReportResponse> call, @NonNull Throwable t) {
                errorHandlerInterface.handleError(t, context);
            }
        });
    }

}



