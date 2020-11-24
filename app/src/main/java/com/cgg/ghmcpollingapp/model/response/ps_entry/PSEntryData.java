package com.cgg.ghmcpollingapp.model.response.ps_entry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PSEntryData {

    @SerializedName("totalVotesInPS")
    @Expose
    private String totalVotesInPS;
    @SerializedName("nextAllowedTimeSlot")
    @Expose
    private String nextAllowedTimeSlot;
    @SerializedName("cumulativeVotesPolled")
    @Expose
    private String cumulativeVotesPolled;

    public String getTotalVotesInPS() {
        return totalVotesInPS;
    }

    public void setTotalVotesInPS(String totalVotesInPS) {
        this.totalVotesInPS = totalVotesInPS;
    }

    public String getNextAllowedTimeSlot() {
        return nextAllowedTimeSlot;
    }

    public void setNextAllowedTimeSlot(String nextAllowedTimeSlot) {
        this.nextAllowedTimeSlot = nextAllowedTimeSlot;
    }

    public String getCumulativeVotesPolled() {
        return cumulativeVotesPolled;
    }

    public void setCumulativeVotesPolled(String cumulativeVotesPolled) {
        this.cumulativeVotesPolled = cumulativeVotesPolled;
    }
}
