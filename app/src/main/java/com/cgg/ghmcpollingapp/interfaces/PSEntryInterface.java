package com.cgg.ghmcpollingapp.interfaces;

import com.cgg.ghmcpollingapp.model.request.ps_entry.PSEntrySubmitRequest;
import com.cgg.ghmcpollingapp.model.response.psList.PSListResponse;
import com.cgg.ghmcpollingapp.model.response.ps_entry.PSEntryResponse;
import com.cgg.ghmcpollingapp.model.response.report.ReportResponse;

public interface PSEntryInterface {
    void getPSList(PSListResponse psEntryResponse);
    void submitPSEntry(PSEntryResponse psEntrySubmitResponse);
    void confirmAlert(PSEntrySubmitRequest psEntrySubmitRequest);
}
