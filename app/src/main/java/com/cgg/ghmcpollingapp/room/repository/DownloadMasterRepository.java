package com.cgg.ghmcpollingapp.room.repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.cgg.ghmcpollingapp.interfaces.DownloadMasterInterface;
import com.cgg.ghmcpollingapp.model.response.master.MasterPSData;
import com.cgg.ghmcpollingapp.model.response.master.MasterTimeSlotData;
import com.cgg.ghmcpollingapp.room.dao.DownloadMasterDao;
import com.cgg.ghmcpollingapp.room.database.PollingDatabase;

import java.util.List;

public class DownloadMasterRepository {
    private final DownloadMasterDao downloadMasterDao;

    public DownloadMasterRepository(Application application) {
        PollingDatabase db = PollingDatabase.getDatabase(application);
        downloadMasterDao = db.downloadMasterDao();
    }

    public LiveData<List<MasterPSData>> getAllPSData() {
        return downloadMasterDao.getAllPSMasterData();
    }

    public LiveData<List<MasterTimeSlotData>> getAllTimeSlotData() {
        return downloadMasterDao.getAllTimeSlotMasterData();
    }

    public void insertPSData(final DownloadMasterInterface syncLocationInterface, final List<MasterPSData> masterPSData) {
        new InsertPSDataAsyncTask(syncLocationInterface, masterPSData).execute();
    }


    public void insertTimeSlotLocations(final DownloadMasterInterface syncLocationInterface, final List<MasterTimeSlotData> PollingEntitys) {
        new InsertTimeSlotAsyncTask(syncLocationInterface, PollingEntitys).execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertPSDataAsyncTask extends AsyncTask<Void, Void, Integer> {
        List<MasterPSData> masterPSData;
        DownloadMasterInterface downloadMasterInterface;

        InsertPSDataAsyncTask(DownloadMasterInterface downloadMasterInterface,
                                  List<MasterPSData> masterPSData) {
            this.masterPSData = masterPSData;
            this.downloadMasterInterface = downloadMasterInterface;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            downloadMasterDao.deletePSMasterData();
            downloadMasterDao.insertPSMasterData(masterPSData);
            return downloadMasterDao.pSMasterDataCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            downloadMasterInterface.psDataCount(integer);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertTimeSlotAsyncTask extends AsyncTask<Void, Void, Integer> {
        List<MasterTimeSlotData> PollingEntitys;
        DownloadMasterInterface syncLocationInterface;

        InsertTimeSlotAsyncTask(DownloadMasterInterface syncLocationInterface,
                                 List<MasterTimeSlotData> PollingEntitys) {
            this.PollingEntitys = PollingEntitys;
            this.syncLocationInterface = syncLocationInterface;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            downloadMasterDao.deleteTimeSlotMasterData();
            downloadMasterDao.insertTimeSlotMasterData(PollingEntitys);
            return downloadMasterDao.timeSlotMasterDataCount();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            syncLocationInterface.timeSlotDataCount(integer);
        }
    }
}
