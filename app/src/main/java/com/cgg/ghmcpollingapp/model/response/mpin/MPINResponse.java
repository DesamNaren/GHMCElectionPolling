package com.cgg.ghmcpollingapp.model.response.mpin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MPINResponse {

    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("ResponseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("listofDtls")
    @Expose
    private Object listofDtls;

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

    public Object getListofDtls() {
        return listofDtls;
    }

    public void setListofDtls(Object listofDtls) {
        this.listofDtls = listofDtls;
    }

}
