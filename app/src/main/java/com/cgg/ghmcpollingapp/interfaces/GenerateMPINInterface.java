package com.cgg.ghmcpollingapp.interfaces;

import com.cgg.ghmcpollingapp.model.response.master.MasterDataResponse;
import com.cgg.ghmcpollingapp.model.response.mpin.MPINResponse;

public interface GenerateMPINInterface {
    public void generateMPINResponse(MPINResponse mpinResponse);
}
