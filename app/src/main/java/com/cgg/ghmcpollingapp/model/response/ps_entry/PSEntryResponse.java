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
    @SerializedName("listofDtls")
    @Expose
    private List<PSEntryData> psEntryData = null;

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

    public List<PSEntryData> getPsEntryData() {
        return psEntryData;
    }

    public void setPsEntryData(List<PSEntryData> psEntryData) {
        this.psEntryData = psEntryData;
    }
}
