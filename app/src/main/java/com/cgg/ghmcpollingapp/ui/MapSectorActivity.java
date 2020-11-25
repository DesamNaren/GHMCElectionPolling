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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.application.PollingApplication;
import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.cgg.ghmcpollingapp.databinding.ActivityMapSectorBinding;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandler;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.interfaces.SectorMappingInterface;
import com.cgg.ghmcpollingapp.model.request.logout.LogoutRequest;
import com.cgg.ghmcpollingapp.model.request.map_sector.SectorMapRequest;
import com.cgg.ghmcpollingapp.model.response.login.LoginResponse;
import com.cgg.ghmcpollingapp.model.response.logout.LogoutResponse;
import com.cgg.ghmcpollingapp.model.response.map_sector.SectorMapResponse;
import com.cgg.ghmcpollingapp.room.repository.PollingMasterRep;
import com.cgg.ghmcpollingapp.utils.CustomProgressDialog;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.cgg.ghmcpollingapp.viewmodel.LogoutViewModel;
import com.cgg.ghmcpollingapp.viewmodel.MapSectorViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MapSectorActivity extends AppCompatActivity implements View.OnClickListener, ErrorHandlerInterface, SectorMappingInterface {

    private ActivityMapSectorBinding binding;
    private Context context;
    private String zoneName, zoneId, cirName, circleId, wardName, wardId, secName, secId, mobNo, userId;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LogoutViewModel logoutViewModel;
    PollingMasterRep pollingMasterRep;
    MapSectorViewModel mapSectorViewModel;
    List<String> zones;
    List<String> circles;
    List<String> wards;
    List<String> sectors;
    CustomProgressDialog customProgressDialog;
    ArrayAdapter selectAdapter;
    ArrayList sellist;
    LoginResponse loginResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = MapSectorActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map_sector);
        logoutViewModel = new LogoutViewModel(this, getApplication());
        mapSectorViewModel = new MapSectorViewModel(this, getApplication());
        customProgressDialog = new CustomProgressDialog(context);

        sharedPreferences = PollingApplication.get(context).getPreferences();
        editor = PollingApplication.get(context).getPreferencesEditor();
        mobNo = sharedPreferences.getString(AppConstants.MOBILE_NO, "");
        binding.btnClear.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.headerLyout.imgBack.setOnClickListener(this);
        pollingMasterRep = new PollingMasterRep(getApplication());
        zones = new ArrayList<>();
        circles = new ArrayList<>();
        wards = new ArrayList<>();
        sectors = new ArrayList<>();
        String data = sharedPreferences.getString(AppConstants.LOGIN_RES, "");
        LoginResponse loginResponse = new Gson().fromJson(data, LoginResponse.class);

        if (loginResponse != null && loginResponse.getLoginData() != null && loginResponse.getLoginData().get(0) != null) {

            userId = loginResponse.getLoginData().get(0).getUserID();
        } else {
            Utils.customErrorAlert(context, getString(R.string.app_name),
                    getString(R.string.something) + " while fetching login response onCreate");
        }
        sellist = new ArrayList();
        sellist.add(getString(R.string.select));
        selectAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, sellist);
        mapSectorViewModel.getZones().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> zones) {
                MapSectorActivity.this.zones = zones;
                if (zones != null && zones.size() > 0) {
                    zones.add(0, getString(R.string.select));
                    ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, zones);
                    binding.zoneSpinner.setAdapter(adapter);
                } else {
                    Utils.callSnackBar(binding.cl, getString(R.string.no_zones_found));
                }
            }
        });
        binding.zoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding.zoneSpinner.getSelectedItemPosition() == 0) {
                    zoneName = "";
                    zoneId = "";
                    cirName = "";
                    circleId = "";
                    wardName = "";
                    wardId = "";
                    secName = "";
                    secId = "";
                    circles.clear();
                    wards.clear();
                    sectors.clear();
                    binding.circleSpinner.setAdapter(selectAdapter);
                    binding.wardSpinner.setAdapter(selectAdapter);
                    binding.sectorSpinner.setAdapter(selectAdapter);
                } else {
                    cirName = "";
                    circleId = "";
                    wardName = "";
                    wardId = "";
                    secName = "";
                    secId = "";
                    circles.clear();
                    wards.clear();
                    sectors.clear();
                    binding.wardSpinner.setAdapter(selectAdapter);
                    binding.sectorSpinner.setAdapter(selectAdapter);
                    customProgressDialog.show();
                    zoneName = binding.zoneSpinner.getSelectedItem().toString().trim();
                    mapSectorViewModel.getZoneId(zoneName).observe(MapSectorActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String zoneId) {
                            MapSectorActivity.this.zoneId = zoneId;
                            mapSectorViewModel.getCircles(zoneId).observe(MapSectorActivity.this, new Observer<List<String>>() {
                                @Override
                                public void onChanged(List<String> circles) {
                                    customProgressDialog.dismiss();
                                    MapSectorActivity.this.circles = circles;
                                    if (circles != null && circles.size() > 0) {
                                        circles.add(0, getString(R.string.select));
                                        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, circles);
                                        binding.circleSpinner.setAdapter(adapter);
                                    } else {
                                        Utils.callSnackBar(binding.cl, getString(R.string.no_circles_found));
                                    }
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.circleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding.circleSpinner.getSelectedItemPosition() == 0) {
                    cirName = "";
                    circleId = "";
                    wardName = "";
                    wardId = "";
                    secName = "";
                    secId = "";
                    wards.clear();
                    sectors.clear();
                    binding.wardSpinner.setAdapter(selectAdapter);
                    binding.sectorSpinner.setAdapter(selectAdapter);
                } else {
                    wardName = "";
                    wardId = "";
                    secName = "";
                    secId = "";
                    wards.clear();
                    sectors.clear();
                    binding.sectorSpinner.setAdapter(selectAdapter);
                    customProgressDialog.show();
                    cirName = binding.circleSpinner.getSelectedItem().toString().trim();
                    mapSectorViewModel.getCircleId(cirName, zoneId).observe(MapSectorActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String circleId) {
                            MapSectorActivity.this.circleId = circleId;
                            mapSectorViewModel.getWards(zoneId, circleId).observe(MapSectorActivity.this, new Observer<List<String>>() {
                                @Override
                                public void onChanged(List<String> wards) {
                                    customProgressDialog.dismiss();
                                    MapSectorActivity.this.wards = wards;
                                    if (wards != null && wards.size() > 0) {
                                        wards.add(0, getString(R.string.select));
                                        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, wards);
                                        binding.wardSpinner.setAdapter(adapter);
                                    } else {
                                        Utils.callSnackBar(binding.cl, getString(R.string.no_wards_found));
                                    }
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.wardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding.wardSpinner.getSelectedItemPosition() == 0) {
                    wardName = "";
                    wardId = "";
                    secName = "";
                    secId = "";
                    sectors.clear();
                    binding.sectorSpinner.setAdapter(selectAdapter);
                } else {
                    secName = "";
                    secId = "";
                    sectors.clear();
                    customProgressDialog.show();
                    wardName = binding.wardSpinner.getSelectedItem().toString().trim();
                    mapSectorViewModel.getWardId(wardName, zoneId, circleId).observe(MapSectorActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String wardId) {
                            MapSectorActivity.this.wardId = wardId;
                            mapSectorViewModel.getSectors(zoneId, circleId, wardId).observe(MapSectorActivity.this, new Observer<List<String>>() {
                                @Override
                                public void onChanged(List<String> sectors) {
                                    customProgressDialog.dismiss();
                                    MapSectorActivity.this.sectors = sectors;
                                    if (sectors != null && sectors.size() > 0) {
                                        sectors.add(0, getString(R.string.select));
                                        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, sectors);
                                        binding.sectorSpinner.setAdapter(adapter);
                                    } else {
                                        Utils.callSnackBar(binding.cl, getString(R.string.no_sectors_found));
                                    }
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.sectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding.sectorSpinner.getSelectedItemPosition() == 0) {
                    secName = "";
                } else {
                    customProgressDialog.show();
                    secName = binding.sectorSpinner.getSelectedItem().toString().trim();
                    mapSectorViewModel.getSectorId(secName, zoneId, circleId, wardId).observe(MapSectorActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String sectorId) {
                            customProgressDialog.dismiss();
                            MapSectorActivity.this.secId = sectorId;
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()) {
                    if (Utils.checkInternetConnection(context)) {
                        customProgressDialog.show();
                        SectorMapRequest sectorMapRequest = new SectorMapRequest();
                        sectorMapRequest.setZoneID(zoneId);
                        sectorMapRequest.setCircleID(circleId);
                        sectorMapRequest.setWardID(wardId);
                        sectorMapRequest.setSectorID(secId);
                        sectorMapRequest.setMobileNo(mobNo);
                        sectorMapRequest.setUserID(userId);
                        mapSectorViewModel.mapSector(sectorMapRequest);
                    } else {
                        Utils.customErrorAlert(context, context.getResources().getString(R.string.app_name), context.getString(R.string.plz_check_int));
                    }
                } else {
                    Utils.customErrorAlert(context, context.getResources().getString(R.string.app_name), "Not getting polling station id");
                }
            }
        });

        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
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
                        logoutRequest.setMobileNo(mobNo);
                        logoutViewModel.logoutCall(logoutRequest).observe(MapSectorActivity.this, new Observer<LogoutResponse>() {
                            @Override
                            public void onChanged(LogoutResponse logoutResponse) {
                                if (logoutResponse != null && logoutResponse.getStatusCode() != null) {
                                    if (logoutResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                                        //visible time slot and load spinner
                                        editor.clear();
                                        editor.commit();
                                        Intent newIntent = new Intent(activity, LoginActivity.class);
                                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        activity.startActivity(newIntent);
                                        activity.finish();
                                    } else if (logoutResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                                        Utils.customErrorAlert(context, getString(R.string.app_name),
                                                logoutResponse.getResponseMessage());
                                    } else {
                                        Utils.customErrorAlert(context, getString(R.string.app_name),
                                                getString(R.string.something) + " No status code found in mark attendance web service response");
                                    }
                                } else {
                                    Utils.customErrorAlert(context, getString(R.string.app_name),
                                            getString(R.string.server_not) + " : Mark attendance web service");
                                }

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
    public void mapSectorResponse(SectorMapResponse sectorMapResponse) {
        try {
            customProgressDialog.hide();
            if (sectorMapResponse != null && sectorMapResponse.getStatusCode() != null) {
                if (sectorMapResponse.getStatusCode() == AppConstants.SUCCESS_CODE) {
                    //visible time slot and load spinner
                    editor.putString(AppConstants.ZONE_ID, zoneId);
                    editor.putString(AppConstants.CIRCLE_ID, circleId);
                    editor.putString(AppConstants.WARD_ID, wardId);
                    editor.putString(AppConstants.SECTOR_ID, secId);
                    editor.putString(AppConstants.ZONE_NAME, zoneName);
                    editor.putString(AppConstants.CIRCLE_NAME, cirName);
                    editor.putString(AppConstants.WARD_NAME, wardName);
                    editor.putString(AppConstants.SECTOR_NAME, secName);
                    editor.commit();
                    Utils.customSuccessAlert(MapSectorActivity.this, getString(R.string.app_name), sectorMapResponse.getResponseMessage());

                } else if (sectorMapResponse.getStatusCode() == AppConstants.FAILURE_CODE) {
                    Utils.customErrorAlert(context, getString(R.string.app_name),
                            sectorMapResponse.getResponseMessage());
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