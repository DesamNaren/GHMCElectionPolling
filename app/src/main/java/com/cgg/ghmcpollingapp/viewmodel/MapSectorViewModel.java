package com.cgg.ghmcpollingapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.interfaces.SectorMappingInterface;
import com.cgg.ghmcpollingapp.model.request.map_sector.SectorMapRequest;
import com.cgg.ghmcpollingapp.model.response.map_sector.SectorMapResponse;
import com.cgg.ghmcpollingapp.model.response.master.MasterPSData;
import com.cgg.ghmcpollingapp.model.response.master.MasterTimeSlotData;
import com.cgg.ghmcpollingapp.network.GHMCService;
import com.cgg.ghmcpollingapp.room.repository.PollingMasterRep;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapSectorViewModel extends AndroidViewModel {
    private PollingMasterRep pollingMasterRep;
    //    private MutableLiveData<ViewTaskResponse> viewTaskResponseMutableLiveData;
    private SectorMappingInterface sectorMappingInterface;

    private Context context;
    private ErrorHandlerInterface errorHandlerInterface;

    private LiveData<List<MasterPSData>> zones;
    private LiveData<List<MasterPSData>> circles;
    private LiveData<List<MasterPSData>> wards;
    private LiveData<List<MasterPSData>> sectors;
    private LiveData<List<MasterTimeSlotData>> timeSlots;

    public MapSectorViewModel(Context context, Application application) {
        super(application);
        errorHandlerInterface = (ErrorHandlerInterface) context;
        sectorMappingInterface = (SectorMappingInterface) context;
        pollingMasterRep = new PollingMasterRep(application);
        zones = new MutableLiveData<>();
        circles = new MutableLiveData<>();
        wards = new MutableLiveData<>();
        sectors = new MutableLiveData<>();
        timeSlots = new MutableLiveData<>();
    }


    public LiveData<List<MasterPSData>> getZones() {
        if (zones != null) {
            zones = pollingMasterRep.getZones();
        }
        return zones;
    }


    public LiveData<List<MasterPSData>> getCircles(String zoneId) {
        if (circles != null) {
            circles = pollingMasterRep.getCircles(zoneId);
        }
        return circles;
    }

    public LiveData<List<MasterPSData>> getWards(String zoneId, String circleId) {
        if (wards != null) {
            wards = pollingMasterRep.getWards(zoneId, circleId);
        }
        return wards;
    }

    public LiveData<List<MasterPSData>> getSectors(String zoneId, String circleId, String wardId) {
        if (sectors != null) {
            sectors = pollingMasterRep.getSectors(zoneId, circleId, wardId);
        }
        return sectors;
    }

    public void mapSector(SectorMapRequest sectorMapRequest) {
        Gson gson = new Gson();
        String str = gson.toJson(sectorMapRequest);
        GHMCService virtuoService = GHMCService.Factory.create();
        virtuoService.mapSector(sectorMapRequest).enqueue(new Callback<SectorMapResponse>() {
            @Override
            public void onResponse(@NonNull Call<SectorMapResponse> call, @NonNull Response<SectorMapResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sectorMappingInterface.mapSectorResponse(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SectorMapResponse> call, @NonNull Throwable t) {
                errorHandlerInterface.handleError(t, context);
            }
        });
    }

    public LiveData<List<MasterTimeSlotData>> getTimeSlots() {
        if (timeSlots != null) {
            timeSlots = pollingMasterRep.getTimeSlots();
        }
        return timeSlots;
    }


}


