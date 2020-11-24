package com.cgg.ghmcpollingapp.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cgg.ghmcpollingapp.room.dao.PollingMasterDao;
import com.cgg.ghmcpollingapp.room.database.PollingDatabase;

import java.util.List;

public class PollingMasterRep {
    private PollingMasterDao pollingMasterDao;

    public PollingMasterRep(Application application) {
        PollingDatabase db = PollingDatabase.getDatabase(application);
        pollingMasterDao = db.pollingDao();
    }

    public LiveData<List<String>> getZones() {
        return pollingMasterDao.getZones();
    }

    public LiveData<List<String>> getPollingStations() {
        return pollingMasterDao.getPollingStations();
    }

    public LiveData<String> getPollingStationId(String psName, String zoneId ,String circleId, String wardId, String sectorId) {
        return pollingMasterDao.getPsId(psName, sectorId, zoneId, circleId, wardId);
    }


}
