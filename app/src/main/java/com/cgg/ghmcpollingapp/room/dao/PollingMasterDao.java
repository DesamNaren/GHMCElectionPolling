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
public interface PollingMasterDao {

    @Query("DELETE FROM polling_master")
    void deletePollingMaster();

    @Insert
    void insertPollingmaster(List<PollingEntity> workDetails);

    @Query("SELECT COUNT(*) FROM polling_master")
    int pollingCount();

    @Query("SELECT * from polling_master GROUP By zone_id")
    LiveData<List<PollingEntity>> getZones();

    @Query("SELECT * from polling_master where zone_id LIKE :zone_id GROUP By circle_id")
    LiveData<List<PollingEntity>> getCircles(String zone_id);

    @Query("SELECT * from polling_master where zone_id LIKE :zone_id AND circle_id LIKE :circle_id GROUP By ward_id")
    LiveData<List<PollingEntity>> getWards(String zone_id,String circle_id);

    @Query("SELECT * from polling_master where zone_id LIKE :zone_id AND circle_id LIKE :circle_id AND ward_id LIKE :ward_id GROUP By sector_id")
    LiveData<List<PollingEntity>> getSectors(String zone_id,String circle_id,String ward_id);

    @Query("SELECT * from polling_master where sector_id LIKE :sector_id AND zone_id LIKE :zone_id AND circle_id LIKE :circle_id AND ward_id LIKE :ward_id ")
    LiveData<List<PollingEntity>> getPsNames(String sector_id,String zone_id,String circle_id,String ward_id);

}