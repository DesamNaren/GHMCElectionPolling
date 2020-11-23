package com.cgg.ghmcpollingapp.model.request.map_sector;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SectorMapRequest {

    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("ZoneID")
    @Expose
    private String zoneID;
    @SerializedName("CircleID")
    @Expose
    private String circleID;
    @SerializedName("WardID")
    @Expose
    private String wardID;
    @SerializedName("SectorID")
    @Expose
    private String sectorID;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getZoneID() {
        return zoneID;
    }

    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
    }

    public String getCircleID() {
        return circleID;
    }

    public void setCircleID(String circleID) {
        this.circleID = circleID;
    }

    public String getWardID() {
        return wardID;
    }

    public void setWardID(String wardID) {
        this.wardID = wardID;
    }

    public String getSectorID() {
        return sectorID;
    }

    public void setSectorID(String sectorID) {
        this.sectorID = sectorID;
    }

}
