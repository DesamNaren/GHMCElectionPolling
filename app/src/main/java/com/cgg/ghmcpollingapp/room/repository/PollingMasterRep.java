package com.cgg.ghmcpollingapp.room.repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;


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

    public void insertEngSectors(final PollingInterface engSyncInterface, final List<PollingEntity> sectorsEntities) {
        new InsertSectorsAsyncTask(engSyncInterface, sectorsEntities).execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertSectorsAsyncTask extends AsyncTask<Void, Void, Integer> {
        List<PollingEntity> sectorsEntities;
        PollingInterface engSyncInterface;

        InsertSectorsAsyncTask(PollingInterface engSyncInterface,
                               List<PollingEntity> sectorsEntities) {
            this.sectorsEntities = sectorsEntities;
            this.engSyncInterface = engSyncInterface;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            pollingMasterDao.deletePollingMaster();
            pollingMasterDao.insertPollingmaster(sectorsEntities);
            return pollingMasterDao.pollingCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            engSyncInterface.setorsCnt(integer);
        }
    }

}
