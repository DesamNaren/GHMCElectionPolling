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

    @SerializedName("TokenID")
    @Expose
    private String TokenID;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getmPIN() {
        return mPIN;
    }

    public void setmPIN(String mPIN) {
        this.mPIN = mPIN;
    }

    public String getTokenID() {
        return TokenID;
    }

    public void setTokenID(String tokenID) {
        TokenID = tokenID;
    }
}
