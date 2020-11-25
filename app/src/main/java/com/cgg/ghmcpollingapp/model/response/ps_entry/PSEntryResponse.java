package com.cgg.ghmcpollingapp.model.response.ps_entry;

import com.cgg.ghmcpollingapp.model.response.login.LoginData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PSEntryResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("TIMESLOT_ID")
    @Expose
    private String tIMESLOTID;
    @SerializedName("TIMESLOT_NAME")
    @Expose
    private String tIMESLOTNAME;
    @SerializedName("SectorID")
    @Expose
    private String sectorID;
    @SerializedName("VOTES")
    @Expose
    private String vOTES;
    @SerializedName("PollingStationID")
    @Expose
    private String pollingStationID;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getTIMESLOTID() {
        return tIMESLOTID;
    }

    public void setTIMESLOTID(String tIMESLOTID) {
        this.tIMESLOTID = tIMESLOTID;
    }

    public String getTIMESLOTNAME() {
        return tIMESLOTNAME;
    }

    public void setTIMESLOTNAME(String tIMESLOTNAME) {
        this.tIMESLOTNAME = tIMESLOTNAME;
    }

    public String getSectorID() {
        return sectorID;
    }

    public void setSectorID(String sectorID) {
        this.sectorID = sectorID;
    }

    public String getVOTES() {
        return vOTES;
    }

    public void setVOTES(String vOTES) {
        this.vOTES = vOTES;
    }

    public String getPollingStationID() {
        return pollingStationID;
    }

    public void setPollingStationID(String pollingStationID) {
        this.pollingStationID = pollingStationID;
    }
}
