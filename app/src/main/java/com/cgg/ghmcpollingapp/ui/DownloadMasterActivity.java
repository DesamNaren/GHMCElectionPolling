package com.cgg.ghmcpollingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.application.PollingApplication;
import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.cgg.ghmcpollingapp.databinding.ActivityMasterDownloadBinding;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandler;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.interfaces.DownloadMasterInterface;
import com.cgg.ghmcpollingapp.model.request.MasterDataRequest;
import com.cgg.ghmcpollingapp.model.response.master.MasterDataResponse;
import com.cgg.ghmcpollingapp.model.response.master.MasterPSData;
import com.cgg.ghmcpollingapp.model.response.master.MasterTimeSlotData;
import com.cgg.ghmcpollingapp.room.repository.DownloadMasterRepository;
import com.cgg.ghmcpollingapp.utils.CustomProgressDialog;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.cgg.ghmcpollingapp.viewmodel.DownloadMasterViewModel;

import java.util.List;

public class DownloadMasterActivity extends AppCompatActivity implements ErrorHandlerInterface, DownloadMasterInterface {

    private Context context;
    private DownloadMasterRepository downloadMasterRepository;
    private DownloadMasterViewModel downloadMasterViewModel;
    private CustomProgressDialog customProgressDialog;
    private ActivityMasterDownloadBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String sessionToken;
    private MasterDataResponse masterDataResponse;
    private String fromClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = DownloadMasterActivity.this;
        customProgressDialog = new CustomProgressDialog(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_master_download);

        sharedPreferences = PollingApplication.get(context).getPreferences();
        editor = PollingApplication.get(context).getPreferencesEditor();

        binding.header.title.setText(R.string.download_master);
        binding.header.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (getIntent() != null) {
            fromClass = getIntent().getStringExtra(AppConstants.FROM_CLASS);
        }

        sessionToken = sharedPreferences.getString(AppConstants.TOKEN_ID, "");
        downloadMasterViewModel = new DownloadMasterViewModel(this, getApplication());
        downloadMasterRepository = new DownloadMasterRepository(getApplication());

        LiveData<List<MasterPSData>> cListLiveData = downloadMasterViewModel.getMasterPSData();
        cListLiveData.observe(this, new Observer<List<MasterPSData>>() {
            @Override
            public void onChanged(List<MasterPSData> masterPSData) {
                cListLiveData.removeObservers(DownloadMasterActivity.this);
                if (masterPSData == null || masterPSData.size() <= 0) {
                    binding.btnPsMaster.setText(getString(R.string.download));
                } else {
                    LiveData<List<MasterTimeSlotData>> wListLiveData = downloadMasterViewModel.getMasterTimeSlotData();
                    wListLiveData.observe(DownloadMasterActivity.this, new Observer<List<MasterTimeSlotData>>() {
                        @Override
                        public void onChanged(List<MasterTimeSlotData> masterTimeSlotData) {
                            wListLiveData.removeObservers(DownloadMasterActivity.this);

                            if (masterTimeSlotData == null || masterTimeSlotData.size() <= 0) {
                                binding.btnPsMaster.setText(getString(R.string.download));
                            } else {
                                binding.btnPsMaster.setText(getString(R.string.re_download));
                            }

                        }
                    });

                }

            }
        });

        binding.btnPsMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkInternetConnection(context)) {
                    binding.btnPsMaster.setText(getString(R.string.download));
                    customProgressDialog.show();
                    MasterDataRequest masterDataRequest = new MasterDataRequest();
                    masterDataRequest.setTokenID(sessionToken);
                            downloadMasterViewModel.getMasterResponseCall(masterDataRequest);

                } else {
                    Utils.customErrorAlert(context, getResources().getString(R.string.app_name), getString(R.string.plz_check_int));
                }
            }
        });

    }

    @Override
    public void handleError(Throwable e, Context context) {
        customProgressDialog.dismiss();
        String errMsg = ErrorHandler.handleError(e, context);
        Utils.customErrorAlert(context, getString(R.string.app_name), errMsg);
    }

    @Override
    public void psDataCount(int count) {
        if (count > 0) {
            if (masterDataResponse != null && masterDataResponse.getMasterTimeSlotData() != null && masterDataResponse.getMasterTimeSlotData().size() > 0) {
                downloadMasterRepository.insertTimeSlotLocations(
                        DownloadMasterActivity.this, masterDataResponse.getMasterTimeSlotData());
            } else {
                Utils.customErrorAlert(DownloadMasterActivity.this, getString(R.string.app_name),
                        getString(R.string.something));
            }
        } else {
            customProgressDialog.dismiss();
            Utils.customErrorAlert(DownloadMasterActivity.this, getResources().getString(R.string.app_name),
                    getString(R.string.no_ps_data_found));
        }
    }

    @Override
    public void timeSlotDataCount(int count) {
        customProgressDialog.dismiss();
        if (count > 0) {
            binding.btnPsMaster.setText(getString(R.string.re_download));
            Utils.customMapSuccessAlert(DownloadMasterActivity.this, getString(R.string.app_name),
                    getString(R.string.master_message_success), fromClass);
        } else {
            Utils.customErrorAlert(DownloadMasterActivity.this, getResources().getString(R.string.app_name),
                    getString(R.string.no_time_slot_data_found));
        }
    }

    @Override
    public void masterResponse(MasterDataResponse masterDataResponse) {
        DownloadMasterActivity.this.masterDataResponse = masterDataResponse;
        if (masterDataResponse != null && masterDataResponse.getStatusCode() != null) {
            if (masterDataResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                Utils.customSessionAlert(DownloadMasterActivity.this, getString(R.string.app_name),
                        masterDataResponse.getResponseMessage());
            } else if (masterDataResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                if (masterDataResponse.getMasterPSData() != null && masterDataResponse.getMasterPSData().size() > 0) {
                    downloadMasterRepository.insertPSData
                            (DownloadMasterActivity.this, masterDataResponse.getMasterPSData());
                } else {
                    Utils.customErrorAlert(DownloadMasterActivity.this, getString(R.string.app_name),
                            getString(R.string.something));
                }
            } else if (masterDataResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                customProgressDialog.dismiss();
                Utils.customErrorAlert(context, getString(R.string.app_name),
                        masterDataResponse.getResponseMessage());
            } else {
                customProgressDialog.dismiss();
                Utils.customErrorAlert(context, getString(R.string.app_name),
                        getString(R.string.something));
            }
        } else {
            customProgressDialog.dismiss();
            Utils.customErrorAlert(context, getString(R.string.app_name),
                    getString(R.string.server_not));
        }
    }

    @Override
    public void handleError(String errMsg, Context context) {
        customProgressDialog.dismiss();
        Utils.customErrorAlert(context, getString(R.string.app_name), errMsg);
    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(fromClass) &&
                fromClass.equalsIgnoreCase(MapSectorActivity.class.getSimpleName())) {
            Intent newIntent = new Intent(this, MapSectorActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            finish();
        } else if (!TextUtils.isEmpty(fromClass) &&
                fromClass.equalsIgnoreCase(DashboardActivity.class.getSimpleName())) {
            Intent newIntent = new Intent(this, DashboardActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            finish();
        }else if (!TextUtils.isEmpty(fromClass) &&
                fromClass.equalsIgnoreCase(PSWiseEntryActivity.class.getSimpleName())) {
            Intent newIntent = new Intent(this, DashboardActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
            finish();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
    }
}
