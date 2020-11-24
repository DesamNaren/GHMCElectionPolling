package com.cgg.ghmcpollingapp.interfaces;

import com.cgg.ghmcpollingapp.model.response.map_sector.SectorMapResponse;
import com.cgg.ghmcpollingapp.model.response.ps_entry.PSEntryResponse;
import com.cgg.ghmcpollingapp.model.response.ps_entry.PSEntrySubmitResponse;

public interface SectorMappingInterface {
    void mapSectorResponse(SectorMapResponse sectorMapResponse);
}
