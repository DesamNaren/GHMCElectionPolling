package com.cgg.ghmcpollingapp.model.response.master;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Time_Slot_Master_Tbl")
public class MasterTimeSlotData {

    @PrimaryKey(autoGenerate = true)
    private int pos_id;

    @SerializedName("TIMESLOT_ID")
    @Expose
    private String timeSlotId;
    @SerializedName("TIMESLOT_NAME")
    @Expose
    private String timeSlotName;


    public int getPos_id() {
        return pos_id;
    }

    public void setPos_id(int pos_id) {
        this.pos_id = pos_id;
    }


    public String getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(String timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public String getTimeSlotName() {
        return timeSlotName;
    }

    public void setTimeSlotName(String timeSlotName) {
        this.timeSlotName = timeSlotName;
    }
}
