package com.cgg.ghmcpollingapp.interfaces;

import com.cgg.ghmcpollingapp.model.response.ps_entry.PSEntryResponse;
import com.cgg.ghmcpollingapp.model.response.ps_entry.PSEntrySubmitResponse;

public interface SectorMappingInterface {
    void submitPSEntry(PSEntrySubmitResponse psEntrySubmitResponse);
}