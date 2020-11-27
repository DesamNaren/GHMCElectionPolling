package com.cgg.ghmcpollingapp.model.response.master;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MasterDataResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("ListPsDtls")
    @Expose
    private List<MasterPSData> masterPSData = null;
    @SerializedName("ListTsDtls")
    @Expose
    private List<MasterTimeSlotData> masterTimeSlotData = null;

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


    public List<MasterPSData> getMasterPSData() {
        return masterPSData;
    }

    public void setMasterPSData(List<MasterPSData> masterPSData) {
        this.masterPSData = masterPSData;
    }

    public List<MasterTimeSlotData> getMasterTimeSlotData() {
        return masterTimeSlotData;
    }

    public void setMasterTimeSlotData(List<MasterTimeSlotData> masterTimeSlotData) {
        this.masterTimeSlotData = masterTimeSlotData;
    }
}