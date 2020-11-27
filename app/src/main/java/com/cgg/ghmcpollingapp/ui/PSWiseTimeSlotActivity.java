package com.cgg.ghmcpollingapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.adapter.StatusListTimeSlotAdapter;
import com.cgg.ghmcpollingapp.application.PollingApplication;
import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.cgg.ghmcpollingapp.databinding.ActivityPsWiseTimeSlotStatusBinding;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandler;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.interfaces.PsStatusInterface;
import com.cgg.ghmcpollingapp.model.request.reports.ReportRequest;
import com.cgg.ghmcpollingapp.model.response.report.ReportResponse;
import com.cgg.ghmcpollingapp.model.status.StatusListData;
import com.cgg.ghmcpollingapp.utils.CustomProgressDialog;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.cgg.ghmcpollingapp.viewmodel.PSStatusViewModel;

import java.util.List;

public class PSWiseTimeSlotActivity extends AppCompatActivity implements
        ErrorHandlerInterface, PsStatusInterface {
    private Context context;
    private List<StatusListData> statuslist;
    private StatusListTimeSlotAdapter adapter;
    private ActivityPsWiseTimeSlotStatusBinding binding;
    private CustomProgressDialog customProgressDialog;
    String sectorId, tokenID;
    SharedPreferences sharedPreferences;
    PSStatusViewModel viewModel;
    private String psNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ps_wise_time_slot_status);
        context = PSWiseTimeSlotActivity.this;
        binding.header.title.setText(getResources().getString(R.string.ps_status_title));

        viewModel = new PSStatusViewModel(this, getApplication());
        customProgressDialog = new CustomProgressDialog(context);
        try {
            sharedPreferences = PollingApplication.get(context).getPreferences();
            sectorId = sharedPreferences.getString(AppConstants.SECTOR_ID, "");
            tokenID = sharedPreferences.getString(AppConstants.TOKEN_ID, "");
            if (getIntent() != null) {
                psNo = getIntent().getStringExtra("PS_NO");
                String psName = getIntent().getStringExtra("PS_NAME");
                binding.psNameTv.setText(psName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupSearchView(binding.search);

        if (Utils.checkInternetConnection(context)) {
            binding.shimmerViewContainer.setVisibility(View.VISIBLE);
            binding.shimmerViewContainer.startShimmerAnimation();

            ReportRequest req = new ReportRequest();
            req.setSectorID(sectorId);
            req.setPollingStationID(psNo);
            req.setTokenID(tokenID);

            viewModel.getReports(req);

        } else {
            Utils.customErrorAlert(context, context.getResources().getString(R.string.app_name), context.getString(R.string.plz_check_int));
        }


        binding.header.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void handleError(Throwable e, Context context) {
        binding.shimmerViewContainer.stopShimmerAnimation();
        binding.shimmerViewContainer.setVisibility(View.GONE);
        String errMsg = ErrorHandler.handleError(e, context);
        Utils.customErrorAlert(context, getString(R.string.app_name), errMsg);
    }

    @Override
    public void handleError(String errMsg, Context context) {
        binding.shimmerViewContainer.stopShimmerAnimation();
        binding.shimmerViewContainer.setVisibility(View.GONE);
        Utils.customErrorAlert(context, getString(R.string.app_name), errMsg);
    }

    @Override
    public void reportsResponse(ReportResponse reportResponse) {
        try {
            binding.shimmerViewContainer.stopShimmerAnimation();
            binding.shimmerViewContainer.setVisibility(View.GONE);

            if (reportResponse != null && reportResponse.getStatusCode() != null) {
                if (reportResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {

                    if (reportResponse.getReportData() != null && reportResponse.getReportData().size() > 0) {
                        binding.noRecordsLl.llData.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
//                        binding.search.setVisibility(View.VISIBLE);

                        adapter = new StatusListTimeSlotAdapter(context, reportResponse.getReportData());
                        binding.recyclerView.setAdapter(adapter);
                        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));

                    } else {
                        binding.noRecordsLl.llData.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
//                        binding.search.setVisibility(View.GONE);
                    }
                } else if (reportResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                    Utils.customErrorAlert(context, getString(R.string.app_name),
                            reportResponse.getResponseMessage());
                } else if (reportResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                    Utils.customSessionAlert(PSWiseTimeSlotActivity.this, getString(R.string.app_name),
                            reportResponse.getResponseMessage());
                } else {
                    Utils.customErrorAlert(context, getString(R.string.app_name),
                            getString(R.string.something) + " No status code found in time slot web service response");
                }
            } else {
                Utils.customErrorAlert(context, getString(R.string.app_name),
                        getString(R.string.server_not) + " : Reports web service");
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            customProgressDialog.hide();
            e.printStackTrace();
        }
    }

    private void setupSearchView(SearchView searchView) {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    if (adapter != null) {
                        adapter.getFilter().filter(newText);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }


        });
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search here");
    }
}