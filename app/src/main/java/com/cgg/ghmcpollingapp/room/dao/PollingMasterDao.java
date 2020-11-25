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

    @Query("SELECT DISTINCT zone_name from polling_master ")
    LiveData<List<String>> getZones();

    @Query("SELECT zone_id from polling_master where zone_name LIKE :zone_name")
    LiveData<String> getZoneId(String zone_name);

    @Query("SELECT DISTINCT circle_name from polling_master where zone_id LIKE :zone_id")
    LiveData<List<String>> getCircles(String zone_id);

    @Query("SELECT circle_id from polling_master where circle_name LIKE :circle_name and zone_id LIKE :zone_id")
    LiveData<String> getCircleId(String circle_name,String zone_id);

    @Query("SELECT DISTINCT ward_name from polling_master where zone_id LIKE :zone_id AND circle_id LIKE :circle_id")
    LiveData<List<String>> getWards(String zone_id,String circle_id);

    @Query("SELECT ward_id from polling_master where ward_name LIKE :ward_name and zone_id LIKE :zone_id AND circle_id LIKE :circle_id")
    LiveData<String> getWardId(String ward_name,String zone_id,String circle_id);

    @Query("SELECT DISTINCT sector_name from polling_master where zone_id LIKE :zone_id AND circle_id LIKE :circle_id AND ward_id LIKE :ward_id ")
    LiveData<List<String>> getSectors(String zone_id,String circle_id,String ward_id);

    @Query("SELECT sector_id from polling_master where sector_name LIKE :sector_name and zone_id LIKE :zone_id AND circle_id LIKE :circle_id AND ward_id LIKE :ward_id")
    LiveData<String> getSectorId(String sector_name,String zone_id,String circle_id,String ward_id);

    @Query("SELECT * from polling_master where sector_id LIKE :sector_id AND zone_id LIKE :zone_id AND circle_id LIKE :circle_id AND ward_id LIKE :ward_id ")
    LiveData<List<PollingEntity>> getPsNames(String sector_id,String zone_id,String circle_id,String ward_id);

    @Query("SELECT * from polling_master where ps_no LIKE :psid AND zone_id LIKE :zone_id AND sector_id LIKE :sector_id AND circle_id LIKE :circle_id AND ward_id LIKE :ward_id ")
    LiveData<PollingEntity> getPsVotes(String psid, String sector_id, String zone_id, String circle_id, String ward_id);

}
