package com.cgg.ghmcpollingapp.model.request.reports;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportRequest {

    @SerializedName("SectorID")
    @Expose
    private String sectorID;
    @SerializedName("PollingStationID")
    @Expose
    private String pollingStationID;
    @SerializedName("TokenID")
    @Expose
    private String tokenID;

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public String getSectorID() {
        return sectorID;
    }

    public void setSectorID(String sectorID) {
        this.sectorID = sectorID;
    }

    public String getPollingStationID() {
        return pollingStationID;
    }

    public void setPollingStationID(String pollingStationID) {
        this.pollingStationID = pollingStationID;
    }
}
