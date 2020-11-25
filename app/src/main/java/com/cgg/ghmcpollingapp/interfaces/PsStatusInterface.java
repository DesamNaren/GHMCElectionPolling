package com.cgg.ghmcpollingapp.interfaces;

import com.cgg.ghmcpollingapp.model.response.ps_entry.PSEntrySubmitResponse;
import com.cgg.ghmcpollingapp.model.response.report.ReportResponse;

public interface PsStatusInterface {
    void reportsResponse(ReportResponse reportResponse);
}
