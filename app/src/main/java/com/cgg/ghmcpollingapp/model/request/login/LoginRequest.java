package com.cgg.ghmcpollingapp.model.request.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("DeviceID")
    @Expose
    private String deviceID;
    @SerializedName("IPAddress")
    @Expose
    private String iPAddress;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getIPAddress() {
        return iPAddress;
    }

    public void setIPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

}
