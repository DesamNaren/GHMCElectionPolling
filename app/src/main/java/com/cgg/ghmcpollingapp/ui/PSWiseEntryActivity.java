package com.cgg.ghmcpollingapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.adapter.PSEntryConfirmAdapter;
import com.cgg.ghmcpollingapp.adapter.PSListAdapter;
import com.cgg.ghmcpollingapp.application.PollingApplication;
import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.cgg.ghmcpollingapp.databinding.ActivityPsWiseEntryBinding;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandler;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.interfaces.PSEntryInterface;
import com.cgg.ghmcpollingapp.model.request.psList.PSListRequest;
import com.cgg.ghmcpollingapp.model.request.ps_entry.PSEntrySubmitRequest;
import com.cgg.ghmcpollingapp.model.request.ps_entry.PSSubmitData;
import com.cgg.ghmcpollingapp.model.response.master.MasterTimeSlotData;
import com.cgg.ghmcpollingapp.model.response.psList.PSListData;
import com.cgg.ghmcpollingapp.model.response.psList.PSListResponse;
import com.cgg.ghmcpollingapp.model.response.ps_entry.PSEntryResponse;
import com.cgg.ghmcpollingapp.utils.CustomProgressDialog;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.cgg.ghmcpollingapp.viewmodel.PSEntryViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class PSWiseEntryActivity extends AppCompatActivity implements PSEntryInterface, ErrorHandlerInterface {

    private ActivityPsWiseEntryBinding binding;
    private String pollingStation, timeSlot;
    private String votes;
    private Context context;
    private PSEntryViewModel viewModel;
    List<String> pollingStations;
    List<MasterTimeSlotData> masterTimeSlotData;
    List<String> timeSlots;
    String psId, totalVotes, polledvotes;
    private CustomProgressDialog customProgressDialog;
    String zoneId, circleId, wardId, sectorId, zoneName, circleName, wardName, sectorName, tokenID, timeslotID;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayAdapter selectAdapter;
    ArrayList sellist;
    private PSListAdapter adapter;
    List<PSListData> psListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ps_wise_entry);
        context = PSWiseEntryActivity.this;
        customProgressDialog = new CustomProgressDialog(context);
        try {
            sharedPreferences = PollingApplication.get(context).getPreferences();
            editor = sharedPreferences.edit();
            zoneId = sharedPreferences.getString(AppConstants.ZONE_ID, "");
            circleId = sharedPreferences.getString(AppConstants.CIRCLE_ID, "");
            wardId = sharedPreferences.getString(AppConstants.WARD_ID, "");
            sectorId = sharedPreferences.getString(AppConstants.SECTOR_ID, "");
            zoneName = sharedPreferences.getString(AppConstants.ZONE_NAME, "");
            circleName = sharedPreferences.getString(AppConstants.CIRCLE_NAME, "");
            wardName = sharedPreferences.getString(AppConstants.WARD_NAME, "");
            sectorName = sharedPreferences.getString(AppConstants.SECTOR_NAME, "");
            tokenID = sharedPreferences.getString(AppConstants.TOKEN_ID, "");

            binding.tvSector.setText(sectorName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.header.title.setText(getResources().getString(R.string.ps_entry_title));
        viewModel = new PSEntryViewModel(this, getApplication());
        pollingStations = new ArrayList<>();
        masterTimeSlotData = new ArrayList<>();
        timeSlots = new ArrayList<>();
        psListData = new ArrayList<>();
        pollingStations.clear();

        setupSearchView(binding.search);

        binding.header.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()) {
                    if (Utils.checkInternetConnection(context)) {
                        try {
                            PSEntrySubmitRequest psEntrySubmitRequest = new PSEntrySubmitRequest();
                            psEntrySubmitRequest.setSectorID(sectorId);
                            psEntrySubmitRequest.setTokenID(tokenID);
                            psEntrySubmitRequest.setTimeSlotID(timeslotID);
                            List<PSSubmitData> tempPsListData = new ArrayList<>();
                            for (int x = 0; x < psListData.size(); x++) {
                                if (psListData.get(x).isCb_status()) {
                                    PSSubmitData data = new PSSubmitData();
                                    data.setVotePolled(psListData.get(x).getvOTES());
                                    data.setPollingStationID(psListData.get(x).getPollingStationID());
                                    tempPsListData.add(data);
                                }
                            }
                            psEntrySubmitRequest.setPsEntryRequests(tempPsListData);
                            ShowSubmitRegisterAlert(psEntrySubmitRequest);
                        } catch (Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } else {
                        Utils.customErrorAlert(context, context.getResources().getString(R.string.app_name), context.getString(R.string.plz_check_int));
                    }

                }
            }
        });


        binding.btnGetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateGetDatails()) {
//                    if (binding.btnGetDetails.getText().toString().trim().equals(getResources().getString(R.string.get_details))) {
//                        if (Utils.checkInternetConnection(context)) {
//                            binding.shimmerViewContainer.setVisibility(View.VISIBLE);
//                            binding.shimmerViewContainer.startShimmerAnimation();
//                            PSListRequest req = new PSListRequest();
//                            req.setSectorID(sectorId);
//                            req.setTokenID(tokenID);
//                            req.setTimeSlotId(timeslotID);
//
//                            viewModel.getPSList(req);
//
//                        } else {
//                            Utils.customErrorAlert(context, context.getResources().getString(R.string.app_name), context.getString(R.string.plz_check_int));
//                        }
//                    } else {
//                        Utils.customInfoAlert(PSWiseEntryActivity.this, context.getResources().getString(R.string.app_name), "Data will be lost. Do you want to exit?", editor);
//                    }
                }
            }
        });

        viewModel.getTimeSlots().observe(this, new Observer<List<MasterTimeSlotData>>() {
            @Override
            public void onChanged(List<MasterTimeSlotData> list) {
                if (list != null && list.size() > 0) {
                    masterTimeSlotData = list;
                    timeSlots.add(0, getString(R.string.select));

                    for (int i = 0; i < masterTimeSlotData.size(); i++) {
                        timeSlots.add(masterTimeSlotData.get(i).getTimeSlotName());
                    }

                    ArrayAdapter adapter = new ArrayAdapter<>(context, R.layout.spinner_layout,
                            timeSlots);
                    binding.spTimeSlot.setAdapter(adapter);
                } else {
                    Utils.customSyncAlertDownload(PSWiseEntryActivity.this,
                            getString(R.string.app_name),
                            getString(R.string.ps_download_message), PSWiseEntryActivity.class.getSimpleName());
                }

            }
        });
        binding.spTimeSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.llRecyclerView.setVisibility(View.GONE);
                binding.btnLayout.setVisibility(View.GONE);
                binding.noRecordsLl.setVisibility(View.VISIBLE);
                binding.noRecordsLl.setText(getString(R.string.sel_time_slot_to_proceed));
                timeslotID = "";
                if (binding.spTimeSlot.getSelectedItemPosition() != 0) {
                    if (Utils.checkInternetConnection(context)) {
                        timeslotID = String.valueOf(masterTimeSlotData.get(position - 1).getTimeSlotId());
                        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
                        binding.shimmerViewContainer.startShimmerAnimation();
                        PSListRequest req = new PSListRequest();
                        req.setSectorID(sectorId);
                        req.setTokenID(tokenID);
                        req.setTimeSlotId(timeslotID);


                        viewModel.getPSList(req);

                    } else {
                        binding.spTimeSlot.setSelection(0);
                        Utils.customErrorAlert(context, context.getResources().getString(R.string.app_name), context.getString(R.string.plz_check_int));
                    }
                } else {
                    binding.noRecordsLl.setText(getString(R.string.sel_time_slot_to_proceed));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    private boolean validateData() {
        if (psListData == null || psListData.size() == 0) {
            Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean flag = false;
        for (int i = 0; i < psListData.size(); i++) {
            if (psListData.get(i).isCb_status()) {
                flag = true;
                break;
            }
        }
        if (flag) {
            for (int i = 0; i < psListData.size(); i++) {
                if (psListData.get(i).isCb_status() && TextUtils.isEmpty(psListData.get(i).getvOTES())) {
                    showSnackBar(getString(R.string.sel_votes_polled_till_now));
                    return false;
                }
            }
        } else {
            showSnackBar(getString(R.string.sel_one_record));
            return false;
        }

        return true;
    }

    private boolean validateGetDatails() {
        Utils.hideKeyboard(context, binding.btnSubmit);

        if (TextUtils.isEmpty(timeslotID)) {
            showSnackBar(getString(R.string.select_time_slot));
            return false;
        }
        return true;
    }

    private void showSnackBar(String str) {
        Snackbar.make(binding.cl, str, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent newIntent = new Intent(this, DashboardActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
        finish();
    }

    @Override
    public void handleError(Throwable e, Context context) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.hide();
        String errMsg = ErrorHandler.handleError(e, context);
        Utils.customErrorAlert(context, getString(R.string.app_name), errMsg);
    }

    @Override
    public void handleError(String errMsg, Context context) {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.hide();
        Utils.customErrorAlert(context, getString(R.string.app_name), errMsg);
    }

    @Override
    public void getPSList(PSListResponse psListResponse) {
        binding.shimmerViewContainer.stopShimmerAnimation();
        binding.shimmerViewContainer.setVisibility(View.GONE);
        binding.llRecyclerView.setVisibility(View.GONE);
        binding.btnLayout.setVisibility(View.GONE);
        binding.noRecordsLl.setVisibility(View.GONE);
        try {
            customProgressDialog.hide();
            if (psListResponse != null && psListResponse.getStatusCode() != null) {
                if (psListResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
//                    binding.btnGetDetails.setText(getResources().getString(R.string.edit));
//                    binding.spTimeSlot.setEnabled(false);
                    binding.llRecyclerView.setVisibility(View.VISIBLE);
                    binding.btnLayout.setVisibility(View.VISIBLE);
                    psListData = psListResponse.getReportData();
                    adapter = new PSListAdapter(context, psListData);
                    binding.recyclerView.setAdapter(adapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));

                } else if (psListResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                    binding.noRecordsLl.setVisibility(View.VISIBLE);
                    if (psListResponse.getResponseMessage() != null)
                        binding.noRecordsLl.setText(psListResponse.getResponseMessage());
                    else
                        binding.noRecordsLl.setText(getString(R.string.no_records_found));
                    Utils.customErrorAlert(context, getString(R.string.app_name),
                            psListResponse.getResponseMessage());
                } else if (psListResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                    Utils.customSessionAlert(PSWiseEntryActivity.this, getString(R.string.app_name),
                            psListResponse.getResponseMessage());
                } else {
                    Utils.customErrorAlert(context, getString(R.string.app_name),
                            getString(R.string.something) + " No status code found in PsWiseStatus web service response");
                }
            } else {
                Utils.customErrorAlert(context, getString(R.string.app_name),
                        getString(R.string.server_not) + " : PsWiseStatus web service");
            }
        } catch (Exception e) {
//            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("TAG", "getPSList: " + e.getMessage());
            customProgressDialog.hide();
            e.printStackTrace();
        }

    }

    @Override
    public void submitPSEntry(PSEntryResponse psEntrySubmitResponse) {
        try {
            customProgressDialog.hide();
            if (psEntrySubmitResponse != null && psEntrySubmitResponse.getStatusCode() != null) {
                if (psEntrySubmitResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                    Utils.customSuccessAlert(PSWiseEntryActivity.this, getString(R.string.app_name), psEntrySubmitResponse.getResponseMessage());
                } else if (psEntrySubmitResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                    Utils.customErrorAlert(context, getString(R.string.app_name),
                            psEntrySubmitResponse.getResponseMessage());
                } else if (psEntrySubmitResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                    Utils.customSessionAlert(PSWiseEntryActivity.this, getString(R.string.app_name),
                            psEntrySubmitResponse.getResponseMessage());
                } else {
                    Utils.customErrorAlert(context, getString(R.string.app_name),
                            getString(R.string.something) + " No status code found in AddPollingVotesList web service response");
                }
            } else {
                Utils.customErrorAlert(context, getString(R.string.app_name),
                        getString(R.string.server_not) + " : AddPollingVotesList web service");
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            customProgressDialog.hide();
            e.printStackTrace();
        }
    }

    @Override
    public void confirmAlert(PSEntrySubmitRequest psEntrySubmitRequest) {
        try {
            customProgressDialog.show();
            viewModel.submitPSEntryCall(psEntrySubmitRequest);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private Dialog conDialog;

    private void ShowSubmitRegisterAlert(PSEntrySubmitRequest psEntrySubmitRequest) {
        try {
            conDialog = new Dialog(this);
            conDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (conDialog.getWindow() != null && conDialog.getWindow().getAttributes() != null) {
                conDialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                conDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                conDialog.setContentView(R.layout.ps_entry_confirm_alert);
                conDialog.setCancelable(true);
                TextView titleTv = conDialog.findViewById(R.id.alert_titleTv);
                titleTv.setText("Sector: " + sectorId);
                final RecyclerView recyclerView = conDialog.findViewById(R.id.rvVotes);
                PSEntryConfirmAdapter paddyConfirmAdapter = new PSEntryConfirmAdapter(context,
                        psEntrySubmitRequest.getPsEntryRequests(), psEntrySubmitRequest,
                        conDialog);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(paddyConfirmAdapter);

                if (!conDialog.isShowing())
                    conDialog.show();
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

}
