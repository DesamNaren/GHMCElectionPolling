package com.cgg.ghmcpollingapp.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cgg.ghmcpollingapp.model.response.master.MasterPSData;
import com.cgg.ghmcpollingapp.model.response.master.MasterTimeSlotData;

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

    @Query("DELETE FROM PS_Master_Tbl")
    void deletePSMasterData();

    @Insert
    void insertPSMasterData(List<MasterPSData> masterPSData);

    @Query("SELECT COUNT(*) FROM PS_Master_Tbl")
    int pSMasterDataCount();

    @Query("DELETE FROM Time_Slot_Master_Tbl")
    void deleteTimeSlotMasterData();

    @Insert
    void insertTimeSlotMasterData(List<MasterTimeSlotData> pollingEntities);

    @Query("SELECT COUNT(*) FROM Time_Slot_Master_Tbl")
    int timeSlotMasterDataCount();

    @Query("SELECT * from PS_Master_Tbl")
    LiveData<List<MasterPSData>> getAllPSMasterData();

    @Query("SELECT * from Time_Slot_Master_Tbl")
    LiveData<List<MasterTimeSlotData>> getAllTimeSlotMasterData();

}
