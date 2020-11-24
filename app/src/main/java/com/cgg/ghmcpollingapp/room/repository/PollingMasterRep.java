package com.cgg.ghmcpollingapp.room.repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;


import androidx.lifecycle.LiveData;

import com.cgg.ghmcpollingapp.interfaces.PollingInterface;
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
    public LiveData<List<String>> getZones() {
        return pollingMasterDao.getZones();
    }


}
