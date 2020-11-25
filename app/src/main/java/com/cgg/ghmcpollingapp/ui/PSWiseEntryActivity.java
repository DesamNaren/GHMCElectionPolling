package com.cgg.ghmcpollingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.application.PollingApplication;
import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.cgg.ghmcpollingapp.databinding.ActivityPsWiseEntryBinding;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandler;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.interfaces.PSEntryInterface;
import com.cgg.ghmcpollingapp.model.request.ps_entry.PSEntryRequest;
import com.cgg.ghmcpollingapp.model.request.ps_entry.PSEntrySubmitRequest;
import com.cgg.ghmcpollingapp.model.response.ps_entry.PSEntryResponse;
import com.cgg.ghmcpollingapp.model.response.ps_entry.PSEntrySubmitResponse;
import com.cgg.ghmcpollingapp.source.PollingEntity;
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
    List<String> pollingStations, pollingIDs;
    List<PollingEntity> pollingEntities;
    List<String> timeSlots;
    String psId, totalVotes, polledvotes;
    private CustomProgressDialog customProgressDialog;
    String zoneId, circleId, wardId, sectorId, zoneName, circleName, wardName, sectorName, tokenID;
    SharedPreferences sharedPreferences;
    ArrayAdapter selectAdapter;
    ArrayList sellist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ps_wise_entry);
        context = PSWiseEntryActivity.this;
        customProgressDialog = new CustomProgressDialog(context);
        try {
            sharedPreferences = PollingApplication.get(context).getPreferences();
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

        viewModel = new PSEntryViewModel(this, getApplication());
        pollingStations = new ArrayList<>();
        pollingIDs = new ArrayList<>();
        pollingEntities = new ArrayList<>();
        timeSlots = new ArrayList<>();
        pollingStations.clear();

        sellist = new ArrayList();
        sellist.add(getString(R.string.select));
        selectAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, sellist);
        binding.spTimeSlot.setAdapter(selectAdapter);
        binding.spPollingStation.setAdapter(selectAdapter);
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                votes = binding.etVotes.getText().toString().trim();
                if (validateData()) {
                    if (Utils.checkInternetConnection(context)) {
                        customProgressDialog.show();
                        PSEntrySubmitRequest psEntrySubmitRequest = new PSEntrySubmitRequest();
                        psEntrySubmitRequest.setPollingStationId(psId);
                        psEntrySubmitRequest.setVotesPolled(votes);
                        psEntrySubmitRequest.setSectorID(sectorId);
                        psEntrySubmitRequest.setTokenId(tokenID);
                        viewModel.submitPSEntryCall(psEntrySubmitRequest);
                    } else {
                        PSWiseEntryActivity.this.psId = "";
                        binding.spPollingStation.setSelection(0);
                        Utils.customErrorAlert(context, context.getResources().getString(R.string.app_name), context.getString(R.string.plz_check_int));
                    }
                }
            }
        });

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

        viewModel.getPollingStations(zoneId, circleId, wardId, sectorId).observe(this, new Observer<List<PollingEntity>>() {
            @Override
            public void onChanged(List<PollingEntity> ps) {
                if (ps != null && ps.size() > 0) {
                    pollingEntities.addAll(ps);
                    pollingStations.add(0, getString(R.string.select));
                    pollingIDs.add(0, getString(R.string.select));

                    for (int i = 0; i < pollingEntities.size(); i++) {
                        pollingStations.add(pollingEntities.get(i).getPs_name());
                        pollingIDs.add(pollingEntities.get(i).getPs_no());
                    }

                    ArrayAdapter adapter = new ArrayAdapter<>(context, R.layout.spinner_layout,
                            pollingStations);
                    binding.spPollingStation.setAdapter(adapter);
                } else {
                    Utils.customErrorAlert(context, context.getResources().getString(R.string.app_name), getString(R.string.no_pollings_found));
                }

            }
        });


        binding.spPollingStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String psName = binding.spPollingStation.getSelectedItem().toString();
                if (binding.spPollingStation.getSelectedItemId() != 0) {
                    String psId = pollingIDs.get(position);
                    viewModel.getPsVotes(psId, zoneId, circleId, wardId, sectorId)
                            .observe(PSWiseEntryActivity.this, new Observer<PollingEntity>() {
                                @Override
                                public void onChanged(PollingEntity pollingEntity) {
                                    if (pollingEntity != null) {
//                                        PSWiseEntryActivity.this.psId = pollingEntity.getPs_no();
                                        PSWiseEntryActivity.this.totalVotes = pollingEntity.getPs_total_cnt();
                                        timeSlots.clear();
                                        polledvotes = "0";
                                        if (Utils.checkInternetConnection(context)) {
                                            customProgressDialog.show();
                                            PSEntryRequest req = new PSEntryRequest();
                                            req.setSectorID(sectorId);
                                            req.setPollingStationID(psId);
                                            req.setTokenID(tokenID);

                                            viewModel.getTimeslotDetails(req);
                                        } else {
                                            PSWiseEntryActivity.this.psId = "";
                                            binding.spPollingStation.setSelection(0);
                                            Utils.customErrorAlert(context, context.getResources().getString(R.string.app_name), context.getString(R.string.plz_check_int));
                                        }
                                    } else {
                                        Utils.customErrorAlert(context, context.getResources().getString(R.string.app_name), "Not getting polling station id");
                                    }
                                }
                            });
                } else {
                    totalVotes = "";
                }
                binding.tvTotalVotes.setText(totalVotes);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private boolean validateData() {
        Utils.hideKeyboard(context, binding.btnSubmit);

        if (binding.spPollingStation.getSelectedItem() != null) {
            pollingStation = binding.spPollingStation.getSelectedItem().toString().trim();
        }
        if (binding.spTimeSlot.getSelectedItem() != null) {
            timeSlot = binding.spTimeSlot.getSelectedItem().toString().trim();
        }
        if (TextUtils.isEmpty(pollingStation) || pollingStation.contains(getString(R.string.select))) {
            binding.llPollingstation.requestFocus();
            showSnackBar(getString(R.string.select_polling_station));
            return false;
        }
        if (TextUtils.isEmpty(timeSlot) || timeSlot.contains(getString(R.string.select))) {
            binding.llTimeslot.requestFocus();
            showSnackBar(getString(R.string.select_time_slot));
            return false;
        }
        if (TextUtils.isEmpty(votes)) {
            binding.etVotes.requestFocus();
            showSnackBar(getString(R.string.votes_polled_during_selected_hour));
            return false;
        }
        if (Integer.parseInt(votes) + Integer.parseInt(polledvotes) > Integer.parseInt(totalVotes)) {
            binding.etVotes.requestFocus();
            showSnackBar(getString(R.string.exceeded));
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
    public void getPSDetails(PSEntryResponse psEntryResponse) {
        try {
            customProgressDialog.hide();
            if (psEntryResponse != null && psEntryResponse.getStatusCode() != null) {
                if (psEntryResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                    //visible time slot and load spinner
                    timeSlots.add(0, getString(R.string.select));
                    timeSlots.add(psEntryResponse.getTIMESLOTNAME());
                    ArrayAdapter adapter = new ArrayAdapter<>(context, R.layout.spinner_layout,
                            timeSlots);
                    binding.spTimeSlot.setAdapter(adapter);
                    if (TextUtils.isEmpty(psEntryResponse.getVOTES())) {
                        polledvotes = psEntryResponse.getVOTES();
                        binding.tvPolledVotes.setText(polledvotes);
                    }
                } else if (psEntryResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                    Utils.customErrorAlert(context, getString(R.string.app_name),
                            psEntryResponse.getResponseMessage());
                } else if (psEntryResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                    Utils.customSessionAlert(PSWiseEntryActivity.this, getString(R.string.app_name),
                            psEntryResponse.getResponseMessage());
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

    @Override
    public void submitPSEntry(PSEntrySubmitResponse psEntrySubmitResponse) {
        try {
            customProgressDialog.hide();
            if (psEntrySubmitResponse != null && psEntrySubmitResponse.getStatusCode() != null) {
                if (psEntrySubmitResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                    Intent newIntent = new Intent(this, DashboardActivity.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(newIntent);
                    finish();
                } else if (psEntrySubmitResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                    Utils.customErrorAlert(context, getString(R.string.app_name),
                            psEntrySubmitResponse.getResponseMessage());
                } else if (psEntrySubmitResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                    Utils.customSessionAlert(PSWiseEntryActivity.this, getString(R.string.app_name),
                            psEntrySubmitResponse.getResponseMessage());
                } else {
                    Utils.customErrorAlert(context, getString(R.string.app_name),
                            getString(R.string.something) + " No status code found in mark attendance web service response");
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