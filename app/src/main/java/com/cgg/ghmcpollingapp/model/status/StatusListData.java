package com.cgg.ghmcpollingapp.model.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusListData {

    @SerializedName("APPLICATION_ID")
    @Expose
    private String aPPLICATIONID;
    @SerializedName("NAME")
    @Expose
    private String nAME;
    @SerializedName("FATHER_HUSBAND_NAME")
    @Expose
    private String fATHERHUSBANDNAME;

    public String getAPPLICATIONID() {
        return aPPLICATIONID;
    }

    public void setAPPLICATIONID(String aPPLICATIONID) {
        this.aPPLICATIONID = aPPLICATIONID;
    }

    public String getNAME() {
        return nAME;
    }

    public void setNAME(String nAME) {
        this.nAME = nAME;
    }

    public String getFATHERHUSBANDNAME() {
        return fATHERHUSBANDNAME;
    }

    public void setFATHERHUSBANDNAME(String fATHERHUSBANDNAME) {
        this.fATHERHUSBANDNAME = fATHERHUSBANDNAME;
    }

}
