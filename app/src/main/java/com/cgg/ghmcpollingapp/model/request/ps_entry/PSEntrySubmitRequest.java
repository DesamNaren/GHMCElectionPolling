package com.cgg.ghmcpollingapp.model.request.ps_entry;

import com.cgg.ghmcpollingapp.model.response.psList.PSListData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PSEntrySubmitRequest {

    @SerializedName("SectorID")
    @Expose
    private String sectorID;
    @SerializedName("TokenID")
    @Expose
    private String tokenID;
    @SerializedName("AddingPollingList")
    @Expose
    private List<PSSubmitData> psEntryRequests = null;

    public String getSectorID() {
        return sectorID;
    }

    public void setSectorID(String sectorID) {
        this.sectorID = sectorID;
    }

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }


    public List<PSSubmitData> getPsEntryRequests() {
        return psEntryRequests;
    }

    public void setPsEntryRequests(List<PSSubmitData> psEntryRequests) {
        this.psEntryRequests = psEntryRequests;
    }
}