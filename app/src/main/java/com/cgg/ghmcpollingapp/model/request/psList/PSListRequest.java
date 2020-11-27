package com.cgg.ghmcpollingapp.model.request.psList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PSListRequest {

    @SerializedName("SectorID")
    @Expose
    private String sectorID;
    @SerializedName("TIMESLOT_ID")
    @Expose
    private String timeSlotId;
    @SerializedName("TokenID")
    @Expose
    private String tokenID;

    public String getSectorID() {
        return sectorID;
    }

    public void setSectorID(String sectorID) {
        this.sectorID = sectorID;
    }

    public String getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(String timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }
}
