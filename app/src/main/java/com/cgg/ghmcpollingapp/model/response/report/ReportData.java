package com.cgg.ghmcpollingapp.model.response.report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportData {

    @SerializedName("pollingstationno")
    @Expose
    private String pollingstationno;
    @SerializedName("nameofthebuilding")
    @Expose
    private String nameofthebuilding;
    @SerializedName("totalhourlyreport")
    @Expose
    private String totalhourlyreport;
    @SerializedName("pendinghourlyreport")
    @Expose
    private String pendinghourlyreport;
    @SerializedName("TIMESLOT_ID")
    @Expose
    private String tIMESLOTID;
    @SerializedName("TIMESLOT_NAME")
    @Expose
    private String tIMESLOTNAME;
    @SerializedName("VOTES")
    @Expose
    private String vOTES;    @SerializedName("TOTALVOTES")
    @Expose
    private String tOTALVOTES;


    public String gettOTALVOTES() {
        return tOTALVOTES;
    }

    public void settOTALVOTES(String tOTALVOTES) {
        this.tOTALVOTES = tOTALVOTES;
    }

    public String getPollingstationno() {
        return pollingstationno;
    }

    public void setPollingstationno(String pollingstationno) {
        this.pollingstationno = pollingstationno;
    }

    public String getNameofthebuilding() {
        return nameofthebuilding;
    }

    public void setNameofthebuilding(String nameofthebuilding) {
        this.nameofthebuilding = nameofthebuilding;
    }

    public String getTotalhourlyreport() {
        return totalhourlyreport;
    }

    public void setTotalhourlyreport(String totalhourlyreport) {
        this.totalhourlyreport = totalhourlyreport;
    }

    public String getPendinghourlyreport() {
        return pendinghourlyreport;
    }

    public void setPendinghourlyreport(String pendinghourlyreport) {
        this.pendinghourlyreport = pendinghourlyreport;
    }

    public String gettIMESLOTID() {
        return tIMESLOTID;
    }

    public void settIMESLOTID(String tIMESLOTID) {
        this.tIMESLOTID = tIMESLOTID;
    }

    public String gettIMESLOTNAME() {
        return tIMESLOTNAME;
    }

    public void settIMESLOTNAME(String tIMESLOTNAME) {
        this.tIMESLOTNAME = tIMESLOTNAME;
    }

    public String getvOTES() {
        return vOTES;
    }

    public void setvOTES(String vOTES) {
        this.vOTES = vOTES;
    }
}
