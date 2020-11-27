package com.cgg.ghmcpollingapp.model.request.ps_entry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PSSubmitData {

    @SerializedName("VotePolled")
    @Expose
    private String votePolled;

    @SerializedName("PollingStationID")
    @Expose
    private String pollingStationID;

    public String getVotePolled() {
        return votePolled;
    }

    public void setVotePolled(String votePolled) {
        this.votePolled = votePolled;
    }

    public String getPollingStationID() {
        return pollingStationID;
    }

    public void setPollingStationID(String pollingStationID) {
        this.pollingStationID = pollingStationID;
    }
}
