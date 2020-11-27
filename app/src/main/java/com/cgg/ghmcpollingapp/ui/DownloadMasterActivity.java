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
import com.cgg.ghmcpollingapp.model.response.login.LoginResponse;
import com.cgg.ghmcpollingapp.room.repository.DownloadMasterRepository;
import com.cgg.ghmcpollingapp.source.PollingEntity;
import com.cgg.ghmcpollingapp.utils.CustomProgressDialog;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.cgg.ghmcpollingapp.viewmodel.DownloadMasterViewModel;

import java.util.Arrays;
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
    private LoginResponse loginResponse;
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
                finish();
            }
        });

        if (getIntent() != null) {
            fromClass = getIntent().getStringExtra(AppConstants.FROM_CLASS);
        }

        sessionToken = sharedPreferences.getString(AppConstants.TOKEN_ID, "");
        downloadMasterViewModel = new DownloadMasterViewModel(this, getApplication());
        downloadMasterRepository = new DownloadMasterRepository(getApplication());

        LiveData<List<PollingEntity>> cListLiveData = downloadMasterViewModel.getMasterPSData();
        cListLiveData.observe(this, new Observer<List<PollingEntity>>() {
            @Override
            public void onChanged(List<PollingEntity> pollingEntities) {
                cListLiveData.removeObservers(DownloadMasterActivity.this);
                if (pollingEntities == null || pollingEntities.size() <= 0) {
                    binding.btnPsMaster.setText(getString(R.string.download));
                } else {
                    LiveData<List<PollingEntity>> wListLiveData = downloadMasterViewModel.getMasterTimeSlotData();
                    wListLiveData.observe(DownloadMasterActivity.this, new Observer<List<PollingEntity>>() {
                        @Override
                        public void onChanged(List<PollingEntity> pollingEntities1) {
                            wListLiveData.removeObservers(DownloadMasterActivity.this);

                            if (pollingEntities1 == null || pollingEntities1.size() <= 0) {
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
                    LiveData<LoginResponse> coordniatesMasterResponseLiveData = downloadMasterViewModel.getMasterResponse(sessionToken);
                    coordniatesMasterResponseLiveData.observe(DownloadMasterActivity.this, new Observer<LoginResponse>() {
                        @Override
                        public void onChanged(LoginResponse loginResponse) {
                            DownloadMasterActivity.this.loginResponse = loginResponse;
                            coordniatesMasterResponseLiveData.removeObservers(DownloadMasterActivity.this);
                            if (loginResponse != null && loginResponse.getStatusCode() != null) {
                                if (loginResponse.getStatusCode() == AppConstants.SESSION_CODE) {
                                    Utils.customSessionAlert(DownloadMasterActivity.this, getString(R.string.app_name),
                                            loginResponse.getResponseMessage());
                                } else if (loginResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                                    if (loginResponse.getLoginData() != null && loginResponse.getLoginData().size() > 0) {
                                        downloadMasterRepository.insertPSData
                                                (DownloadMasterActivity.this, Arrays.asList(new PollingEntity()));
                                    } else {
                                        Utils.customErrorAlert(DownloadMasterActivity.this, getString(R.string.app_name),
                                                getString(R.string.something));
                                    }
                                } else if (loginResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                                    customProgressDialog.hide();
                                    Utils.customErrorAlert(context, getString(R.string.app_name),
                                            loginResponse.getResponseMessage());
                                } else {
                                    customProgressDialog.hide();
                                    Utils.customErrorAlert(context, getString(R.string.app_name),
                                            getString(R.string.something));
                                }
                            } else {
                                customProgressDialog.hide();
                                Utils.customErrorAlert(context, getString(R.string.app_name),
                                        getString(R.string.server_not));
                            }
                        }
                    });
                } else {
                    Utils.customErrorAlert(context, getResources().getString(R.string.app_name), getString(R.string.plz_check_int));
                }
            }
        });

    }

    @Override
    public void handleError(Throwable e, Context context) {
        customProgressDialog.hide();
        String errMsg = ErrorHandler.handleError(e, context);
        Utils.customErrorAlert(context, getString(R.string.app_name), errMsg);
    }

    @Override
    public void psDataCount(int count) {
        if (count > 0) {
            if (loginResponse != null && loginResponse.getLoginData() != null && loginResponse.getLoginData().size() > 0) {
                downloadMasterRepository.insertTimeSlotLocations(
                        DownloadMasterActivity.this, Arrays.asList(new PollingEntity()));
            } else {
                Utils.customErrorAlert(DownloadMasterActivity.this, getString(R.string.app_name),
                        getString(R.string.something));
            }
        } else {
            customProgressDialog.hide();
            Utils.customErrorAlert(DownloadMasterActivity.this, getResources().getString(R.string.app_name),
                    getString(R.string.no_ps_data_found));
        }
    }

    @Override
    public void timeSlotDataCount(int count) {
        customProgressDialog.hide();
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
    public void handleError(String errMsg, Context context) {
        customProgressDialog.hide();
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
        } else {
            finish();
        }
    }
}
