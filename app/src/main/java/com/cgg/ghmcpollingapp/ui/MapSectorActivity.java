package com.cgg.ghmcpollingapp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.application.PollingApplication;
import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.cgg.ghmcpollingapp.databinding.ActivityMapSectorBinding;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.model.request.logout.LogoutRequest;
import com.cgg.ghmcpollingapp.model.response.logout.LogoutResponse;
import com.cgg.ghmcpollingapp.room.repository.PollingMasterRep;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.cgg.ghmcpollingapp.viewmodel.LogoutViewModel;
import com.cgg.ghmcpollingapp.viewmodel.MapSectorViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MapSectorActivity extends AppCompatActivity implements View.OnClickListener, ErrorHandlerInterface {

    private ActivityMapSectorBinding binding;
    private Context context;
    private String zoneName, cirName, wardName, secName;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LogoutViewModel logoutViewModel;
    private String mobNum;
    PollingMasterRep pollingMasterRep;
    MapSectorViewModel mapSectorViewModel;
    List<String> zones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = MapSectorActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map_sector);
        logoutViewModel = new LogoutViewModel(this, getApplication());
        mapSectorViewModel = new MapSectorViewModel(this, getApplication());

        sharedPreferences = PollingApplication.get(context).getPreferences();
        editor = PollingApplication.get(context).getPreferencesEditor();
        mobNum = sharedPreferences.getString(AppConstants.MOBILE_NO, "");
        binding.btnClear.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.headerLyout.imgBack.setOnClickListener(this);
        pollingMasterRep=new PollingMasterRep(getApplication());
        zones=new ArrayList<>();


        mapSectorViewModel.getZones().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> zones) {
                MapSectorActivity.this.zones=zones;
            }
        });

    }

    private boolean validateData() {
        Utils.hideKeyboard(context, binding.btnSubmit);

        if (binding.zoneSpinner.getSelectedItem() != null) {
            zoneName = binding.zoneSpinner.getSelectedItem().toString().trim();
        }
        if (binding.circleSpinner.getSelectedItem() != null) {
            cirName = binding.circleSpinner.getSelectedItem().toString().trim();
        }
        if (binding.wardSpinner.getSelectedItem() != null) {
            wardName = binding.wardSpinner.getSelectedItem().toString().trim();
        }
        if (binding.sectorSpinner.getSelectedItem() != null) {
            secName = binding.sectorSpinner.getSelectedItem().toString().trim();
        }
        if (TextUtils.isEmpty(zoneName) || zoneName.contains(getString(R.string.select))) {
            binding.llZone.requestFocus();
            showSnackBar(getString(R.string.sel_zone));
            return false;
        }

        if (TextUtils.isEmpty(cirName) || cirName.contains(getString(R.string.select))) {
            binding.llCircle.requestFocus();
            showSnackBar(getString(R.string.sel_circle));
            return false;
        }

        if (TextUtils.isEmpty(wardName) || wardName.contains(getString(R.string.select))) {
            binding.llWard.requestFocus();
            showSnackBar(getString(R.string.sel_ward));
            return false;
        }

        if (TextUtils.isEmpty(secName) || secName.contains(getString(R.string.select))) {
            binding.llSector.requestFocus();
            showSnackBar(getString(R.string.sel_sector));
            return false;
        }
        return true;
    }

    private void showSnackBar(String str) {
        Snackbar.make(binding.cl, str, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear:
                Intent intent = getIntent();
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.btn_submit:
//                if (validateData()) {
//                    MapSectorViewModel mapSectorViewModel = new MapSectorViewModel(context, getApplication());
//                }
                startActivity(new Intent(context, PSWiseEntryActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        customCancelAlert(MapSectorActivity.this, getResources().getString(R.string.app_name_release),
                getString(R.string.cancel_mapping_process), editor);
    }

    private void customCancelAlert(Activity activity, String title, String
            msg, SharedPreferences.Editor editor) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null && dialog.getWindow().getAttributes() != null) {
                dialog.getWindow().getAttributes().windowAnimations = R.style.exitdialog_animation1;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.custom_alert_exit);
                dialog.setCancelable(false);
                TextView versionTitle = dialog.findViewById(R.id.version_tv);
                versionTitle.setText("Version: " + Utils.getVersionName(activity));
                TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
                dialogTitle.setText(title);
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);
                Button exit = dialog.findViewById(R.id.btDialogExit);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        LogoutRequest logoutRequest = new LogoutRequest();
                        logoutRequest.setMobileNo(mobNum);
                        logoutViewModel.logoutCall(logoutRequest).observe(MapSectorActivity.this, new Observer<LogoutResponse>() {
                            @Override
                            public void onChanged(LogoutResponse logoutResponse) {
                                editor.clear();
                                editor.commit();
                                Intent newIntent = new Intent(activity, LoginActivity.class);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(newIntent);
                                activity.finish();
                            }
                        });


                    }
                });

                Button cancel = dialog.findViewById(R.id.btDialogCancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });

                if (!dialog.isShowing())
                    dialog.show();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleError(Throwable e, Context context) {

    }

    @Override
    public void handleError(String e, Context context) {

    }
}