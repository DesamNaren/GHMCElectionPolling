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
    public LiveData<String> getZoneId(String zoneName) {
        return pollingMasterDao.getZoneId(zoneName);
    }

    public LiveData<List<String>> getCircles(String zone_id) {
        return pollingMasterDao.getCircles(zone_id);
    }

    public LiveData<String> getCircleId(String circleName, String zoneId) {
        return pollingMasterDao.getCircleId(circleName, zoneId);
    }

    public LiveData<List<String>> getWards(String zone_id, String circleId) {
        return pollingMasterDao.getWards(zone_id, circleId);
    }

    public LiveData<String> getWardId(String wardName, String zoneId, String circleId) {
        return pollingMasterDao.getWardId(wardName, zoneId, circleId);
    }

    public LiveData<List<String>> getSectors(String zone_id, String circleId, String wardId) {
        return pollingMasterDao.getSectors(zone_id, circleId, wardId);
    }

    public LiveData<String> getSectorId(String sectorName, String zoneId, String circleId, String wardId) {
        return pollingMasterDao.getSectorId(sectorName, zoneId, circleId, wardId);
    }

}
