package com.cgg.ghmcpollingapp.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MasterDataRequest {
    @SerializedName("TokenID")
    @Expose
    private String tokenID;

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }
}
