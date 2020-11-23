package com.cgg.ghmcpollingapp.model.request.mpin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateMPINRequest {
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("MPIN")
    @Expose
    private String mPIN;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMPIN() {
        return mPIN;
    }

    public void setMPIN(String mPIN) {
        this.mPIN = mPIN;
    }

}
