package com.cgg.ghmcpollingapp.model.request.ps_entry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PSEntrySubmitRequest {

    @SerializedName("username")
    @Expose
    private String userName;
    @SerializedName("MPIN")
    @Expose
    private String MPIN;
    @SerializedName("pollingStationId")
    @Expose
    private String pollingStationId;
    @SerializedName("timeSlotId")
    @Expose
    private String timeSlotId;
    @SerializedName("votesPolled")
    @Expose
    private String votesPolled;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("ipAddress")
    @Expose
    private String ipAddress;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMPIN() {
        return MPIN;
    }

    public void setMPIN(String MPIN) {
        this.MPIN = MPIN;
    }

    public String getPollingStationId() {
        return pollingStationId;
    }

    public void setPollingStationId(String pollingStationId) {
        this.pollingStationId = pollingStationId;
    }

    public String getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(String timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public String getVotesPolled() {
        return votesPolled;
    }

    public void setVotesPolled(String votesPolled) {
        this.votesPolled = votesPolled;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
