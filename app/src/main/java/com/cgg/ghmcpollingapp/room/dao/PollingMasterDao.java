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
public interface PollingMasterDao {

    @Query("SELECT * from ps_master_tbl GROUP By zoneId")
    LiveData<List<MasterPSData>> getZones();

    @Query("SELECT * from ps_master_tbl where zoneId LIKE :zone_id GROUP By circleId")
    LiveData<List<MasterPSData>> getCircles(String zone_id);

    @Query("SELECT * from ps_master_tbl where zoneId LIKE :zone_id AND circleId LIKE :circle_id GROUP By wardId")
    LiveData<List<MasterPSData>> getWards(String zone_id,String circle_id);

    @Query("SELECT * from ps_master_tbl where zoneId LIKE :zone_id AND circleId LIKE :circle_id AND wardId LIKE :ward_id GROUP By sectorId")
    LiveData<List<MasterPSData>> getSectors(String zone_id,String circle_id,String ward_id);

    @Query("SELECT * from ps_master_tbl where sectorId LIKE :sector_id AND zoneId LIKE :zone_id AND circleId LIKE :circle_id AND wardId LIKE :ward_id ")
    LiveData<List<MasterPSData>> getPsNames(String sector_id,String zone_id,String circle_id,String ward_id);

    @Query("SELECT * from Time_Slot_Master_Tbl")
    LiveData<List<MasterTimeSlotData>> getTimeSlots();

}