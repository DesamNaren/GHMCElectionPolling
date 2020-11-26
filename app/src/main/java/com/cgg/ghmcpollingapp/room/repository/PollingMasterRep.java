package com.cgg.ghmcpollingapp.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cgg.ghmcpollingapp.room.dao.PollingMasterDao;
import com.cgg.ghmcpollingapp.room.database.PollingDatabase;
import com.cgg.ghmcpollingapp.source.PollingEntity;

import java.util.List;

public class PollingMasterRep {
    private PollingMasterDao pollingMasterDao;

    public PollingMasterRep(Application application) {
        PollingDatabase db = PollingDatabase.getDatabase(application);
        pollingMasterDao = db.pollingDao();
    }

    public LiveData<List<PollingEntity>> getZones() {
        return pollingMasterDao.getZones();
    }

    public LiveData<List<PollingEntity>> getPollingStations(String zoneId,String circleId,String wardId,String sectorId) {
        return pollingMasterDao.getPsNames(sectorId,zoneId,circleId,wardId);
    }

    public LiveData<List<PollingEntity>> getCircles(String zone_id) {
        return pollingMasterDao.getCircles(zone_id);
    }

    public LiveData<List<PollingEntity>> getWards(String zone_id, String circleId) {
        return pollingMasterDao.getWards(zone_id, circleId);
    }

    public LiveData<List<PollingEntity>> getSectors(String zone_id, String circleId, String wardId) {
        return pollingMasterDao.getSectors(zone_id, circleId, wardId);
    }

}