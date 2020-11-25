package com.cgg.ghmcpollingapp.model.response.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("MPIN")
    @Expose
    private String mPIN;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("SectorName")
    @Expose
    private String sectorName;
    @SerializedName("SectorID")
    @Expose
    private String sectorID;
    @SerializedName("RoleID")
    @Expose
    private String roleID;
    @SerializedName("OTP")
    @Expose
    private String oTP;
    @SerializedName("DesignationID")
    @Expose
    private String designationID;
    @SerializedName("isSectorMapped")
    @Expose
    private String isSectorMapped;
    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("DeviceID")
    @Expose
    private String deviceID;
    @SerializedName("IPAddress")
    @Expose
    private String iPAddress;
    @SerializedName("ZoneID")
    @Expose
    private String zoneID;
    @SerializedName("CircleID")
    @Expose
    private String circleID;
    @SerializedName("WardID")
    @Expose
    private String wardID;

    @SerializedName("ZoneID1")
    @Expose
    private String zoneName;
    @SerializedName("CircleID1")
    @Expose
    private String circleName;
    @SerializedName("WardID1")
    @Expose
    private String wardName;
    @SerializedName("TokenID")
    @Expose
    private String tokenID;

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getSectorID() {
        return sectorID;
    }

    public void setSectorID(String sectorID) {
        this.sectorID = sectorID;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getOTP() {
        return oTP;
    }

    public void setOTP(String oTP) {
        this.oTP = oTP;
    }

    public String getDesignationID() {
        return designationID;
    }

    public void setDesignationID(String designationID) {
        this.designationID = designationID;
    }

    public String getIsSectorMapped() {
        return isSectorMapped;
    }

    public void setIsSectorMapped(String isSectorMapped) {
        this.isSectorMapped = isSectorMapped;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

}
