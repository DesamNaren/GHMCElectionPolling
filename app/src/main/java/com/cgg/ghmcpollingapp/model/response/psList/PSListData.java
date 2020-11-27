package com.cgg.ghmcpollingapp.model.response.psList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PSListData {

    @SerializedName("TOTALVOTES")
    @Expose
    private String votePolled;

    @SerializedName("pollingstationno")
    @Expose
    private String pollingStationID;
    @SerializedName("nameofthebuilding")
    @Expose
    private String psName;

    private String vOTES;
    private boolean cb_status = false;


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

    public String getPsName() {
        return psName;
    }

    public void setPsName(String psName) {
        this.psName = psName;
    }

    public String getvOTES() {
        return vOTES;
    }

    public void setvOTES(String vOTES) {
        this.vOTES = vOTES;
    }

    public boolean isCb_status() {
        return cb_status;
    }

    public void setCb_status(boolean cb_status) {
        this.cb_status = cb_status;
    }
}
