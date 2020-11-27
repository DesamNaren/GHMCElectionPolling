package com.cgg.ghmcpollingapp.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cgg.ghmcpollingapp.source.PollingEntity;

import java.util.List;


/**
 * The Room Magic is in this file, where you map file_provider_paths Java method call to an SQL query.
 * <p>
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */
//

@Dao
public interface DownloadMasterDao {

    @Query("DELETE FROM polling_master")
    void deletePSMasterData();

    @Insert
    void insertPSMasterData(List<PollingEntity> pollingEntities);

    @Query("SELECT COUNT(*) FROM polling_master")
    int pSMasterDataCount();

    @Query("DELETE FROM polling_master")
    void deleteTimeSlotMasterData();

    @Insert
    void insertTimeSlotMasterData(List<PollingEntity> pollingEntities);

    @Query("SELECT COUNT(*) FROM polling_master")
    int timeSlotMasterDataCount();

    @Query("SELECT * from polling_master")
    LiveData<List<PollingEntity>> getAllPSMasterData();

    @Query("SELECT * from polling_master")
    LiveData<List<PollingEntity>> getAllTimeSlotMasterData();

}
