package com.cgg.ghmcpollingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.adapter.StatusListAdapter;
import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.cgg.ghmcpollingapp.databinding.ActivityPsWiseStatusBinding;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandler;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.interfaces.PsStatusInterface;
import com.cgg.ghmcpollingapp.model.request.ps_entry.PSEntryRequest;
import com.cgg.ghmcpollingapp.model.request.reports.ReportRequest;
import com.cgg.ghmcpollingapp.model.response.report.ReportResponse;
import com.cgg.ghmcpollingapp.model.status.StatusListData;
import com.cgg.ghmcpollingapp.utils.CustomProgressDialog;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.cgg.ghmcpollingapp.viewmodel.PSStatusViewModel;

import java.util.List;

public class PSWiseStatusActivity extends AppCompatActivity implements ErrorHandlerInterface, PsStatusInterface {
    private Context context;
    private List<StatusListData> statuslist;
    private StatusListAdapter adapter;
    private ActivityPsWiseStatusBinding binding;
    private CustomProgressDialog customProgressDialog;
    PSStatusViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ps_wise_status);
        context = PSWiseStatusActivity.this;
        customProgressDialog = new CustomProgressDialog(context);


        if (Utils.checkInternetConnection(context)) {
            customProgressDialog.show();
            ReportRequest req = new ReportRequest();
//            req.setSectorID(sectorId);
//            req.setPollingStationID(psId);
//            req.setTokenID(tokenID);

            viewModel.getReports(req);
        } else {
            Utils.customErrorAlert(context, context.getResources().getString(R.string.app_name), context.getString(R.string.plz_check_int));
        }
//        adapter = new StatusListAdapter(context, statuslist);
//        binding.recyclerView.setAdapter(adapter);
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));

        binding.header.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent newIntent = new Intent(this, PSWiseStatusActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
        finish();
    }

    @Override
    public void handleError(Throwable e, Context context) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.hide();
        binding.shimmerViewContainer.stopShimmerAnimation();
        binding.shimmerViewContainer.setVisibility(View.GONE);
        String errMsg = ErrorHandler.handleError(e, context);
        Utils.customErrorAlert(context, getString(R.string.app_name), errMsg);
    }

    @Override
    public void handleError(String errMsg, Context context) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.hide();
        binding.shimmerViewContainer.stopShimmerAnimation();
        binding.shimmerViewContainer.setVisibility(View.GONE);
        Utils.customErrorAlert(context, getString(R.string.app_name), errMsg);
    }

    @Override
    public void reportsResponse(ReportResponse reportResponse) {
        try {
            customProgressDialog.hide();
            if (reportResponse != null && reportResponse.getStatusCode() != null) {
                if (reportResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                    //visible time slot and load spinner
//                    timeSlots.add(0, getString(R.string.select));
//                    timeSlots.add(psEntryResponse.getTIMESLOTNAME());
//                    ArrayAdapter adapter = new ArrayAdapter<>(context, R.layout.spinner_layout,
//                            timeSlots);
//                    binding.spTimeSlot.setAdapter(adapter);
//                    if (!TextUtils.isEmpty(psEntryResponse.getVOTES())) {
//                        polledvotes = psEntryResponse.getVOTES();
//                        binding.tvPolledVotes.setText(polledvotes);
//                    } else {
//                        polledvotes = "0";
//                    }
                } else if (reportResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                    Utils.customErrorAlert(context, getString(R.string.app_name),
                            reportResponse.getResponseMessage());
                } else if (reportResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                    Utils.customSessionAlert(PSWiseStatusActivity.this, getString(R.string.app_name),
                            reportResponse.getResponseMessage());
                } else {
                    Utils.customErrorAlert(context, getString(R.string.app_name),
                            getString(R.string.something) + " No status code found in time slot web service response");
                }
            } else {
                Utils.customErrorAlert(context, getString(R.string.app_name),
                        getString(R.string.server_not) + " : Mark attendance web service");
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            customProgressDialog.hide();
            e.printStackTrace();
        }
    }
}