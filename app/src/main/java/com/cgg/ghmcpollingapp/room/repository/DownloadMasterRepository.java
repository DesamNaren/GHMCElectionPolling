package com.cgg.ghmcpollingapp.room.repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.cgg.ghmcpollingapp.interfaces.DownloadMasterInterface;
import com.cgg.ghmcpollingapp.room.dao.DownloadMasterDao;
import com.cgg.ghmcpollingapp.room.database.PollingDatabase;
import com.cgg.ghmcpollingapp.source.PollingEntity;

import java.util.List;

public class DownloadMasterRepository {
    private final DownloadMasterDao downloadMasterDao;

    public DownloadMasterRepository(Application application) {
        PollingDatabase db = PollingDatabase.getDatabase(application);
        downloadMasterDao = db.downloadMasterDao();
    }

    public LiveData<List<PollingEntity>> getAllPSData() {
        return downloadMasterDao.getAllPSMasterData();
    }

    public LiveData<List<PollingEntity>> getAllTimeSlotData() {
        return downloadMasterDao.getAllTimeSlotMasterData();
    }

    public void insertPSData(final DownloadMasterInterface syncLocationInterface, final List<PollingEntity> PollingEntity) {
        new InsertPSDataAsyncTask(syncLocationInterface, PollingEntity).execute();
    }


    public void insertTimeSlotLocations(final DownloadMasterInterface syncLocationInterface, final List<PollingEntity> PollingEntitys) {
        new InsertTimeSlotAsyncTask(syncLocationInterface, PollingEntitys).execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class InsertPSDataAsyncTask extends AsyncTask<Void, Void, Integer> {
        List<PollingEntity> PollingEntity;
        DownloadMasterInterface downloadMasterInterface;

        InsertPSDataAsyncTask(DownloadMasterInterface downloadMasterInterface,
                                  List<PollingEntity> PollingEntity) {
            this.PollingEntity = PollingEntity;
            this.downloadMasterInterface = downloadMasterInterface;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            downloadMasterDao.deletePSMasterData();
            downloadMasterDao.insertPSMasterData(PollingEntity);
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
        List<PollingEntity> PollingEntitys;
        DownloadMasterInterface syncLocationInterface;

        InsertTimeSlotAsyncTask(DownloadMasterInterface syncLocationInterface,
                                 List<PollingEntity> PollingEntitys) {
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
