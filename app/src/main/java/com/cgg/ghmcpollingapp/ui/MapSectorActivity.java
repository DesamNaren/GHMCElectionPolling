package com.cgg.ghmcpollingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cgg.ghmcpollingapp.databinding.ActivityMapSectorBinding;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.cgg.ghmcpollingapp.viewmodel.MapSectorViewModel;
import com.google.android.material.snackbar.Snackbar;

public class MapSectorActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMapSectorBinding binding;
    private Context context;
    private String zoneName, cirName, wardName, secName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = MapSectorActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map_sector);

        binding.btnClear.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.headerLyout.imgBack.setOnClickListener(this);
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
                if (validateData()) {
                    MapSectorViewModel mapSectorViewModel = new MapSectorViewModel(context, getApplication());
                }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}