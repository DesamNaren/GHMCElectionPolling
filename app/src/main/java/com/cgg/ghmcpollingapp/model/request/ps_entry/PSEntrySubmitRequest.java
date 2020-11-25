package com.cgg.ghmcpollingapp.model.request.ps_entry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PSEntrySubmitRequest {

    @SerializedName("TokenID")
    @Expose
    private String tokenId;
    @SerializedName("PollingStationID")
    @Expose
    private String pollingStationId;
    @SerializedName("VotePolled")
    @Expose
    private String votesPolled;
    @SerializedName("SectorID")
    @Expose
    private String SectorID;


    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getPollingStationId() {
        return pollingStationId;
    }

    public void setPollingStationId(String pollingStationId) {
        this.pollingStationId = pollingStationId;
    }

    public String getVotesPolled() {
        return votesPolled;
    }

    public void setVotesPolled(String votesPolled) {
        this.votesPolled = votesPolled;
    }

    public String getSectorID() {
        return SectorID;
    }

    public void setSectorID(String sectorID) {
        SectorID = sectorID;
    }
}
