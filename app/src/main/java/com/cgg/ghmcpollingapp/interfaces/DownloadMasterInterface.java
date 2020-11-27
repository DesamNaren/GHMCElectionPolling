package com.cgg.ghmcpollingapp.interfaces;

import com.cgg.ghmcpollingapp.model.response.master.MasterDataResponse;

public interface DownloadMasterInterface {
    public void psDataCount(int count);
    public void timeSlotDataCount(int count);
    public void masterResponse(MasterDataResponse masterDataResponse);
}
