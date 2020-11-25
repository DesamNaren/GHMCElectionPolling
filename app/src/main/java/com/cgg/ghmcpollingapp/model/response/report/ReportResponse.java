package com.cgg.ghmcpollingapp.model.response.report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;


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
}
