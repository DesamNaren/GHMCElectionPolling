package com.cgg.ghmcpollingapp.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cgg.ghmcpollingapp.model.response.master.MasterPSData;
import com.cgg.ghmcpollingapp.model.response.master.MasterTimeSlotData;
import com.cgg.ghmcpollingapp.room.dao.PollingMasterDao;
import com.cgg.ghmcpollingapp.room.database.PollingDatabase;

import java.util.List;

public class PollingMasterRep {
    private PollingMasterDao pollingMasterDao;

    public PollingMasterRep(Application application) {
        PollingDatabase db = PollingDatabase.getDatabase(application);
        pollingMasterDao = db.pollingDao();
    }

    public LiveData<List<MasterPSData>> getZones() {
        return pollingMasterDao.getZones();
    }

    public LiveData<List<MasterPSData>> getPollingStations(String zoneId,String circleId,String wardId,String sectorId) {
        return pollingMasterDao.getPsNames(sectorId,zoneId,circleId,wardId);
    }

    public LiveData<List<MasterPSData>> getCircles(String zone_id) {
        return pollingMasterDao.getCircles(zone_id);
    }

    public LiveData<List<MasterPSData>> getWards(String zone_id, String circleId) {
        return pollingMasterDao.getWards(zone_id, circleId);
    }

    public LiveData<List<MasterPSData>> getSectors(String zone_id, String circleId, String wardId) {
        return pollingMasterDao.getSectors(zone_id, circleId, wardId);
    }

    public LiveData<List<MasterTimeSlotData>> getTimeSlots() {
        return pollingMasterDao.getTimeSlots();
    }

}