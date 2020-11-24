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
import androidx.lifecycle.Observer;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.databinding.ActivityPsWiseEntryBinding;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.cgg.ghmcpollingapp.viewmodel.MapSectorViewModel;
import com.cgg.ghmcpollingapp.viewmodel.PSEntryViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class PSWiseEntryActivity extends AppCompatActivity {

    private ActivityPsWiseEntryBinding binding;
    private String pollingStation, timeSlot;
    private String votes;
    private Context context;
    private PSEntryViewModel viewModel;
    List<String> pollingStations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ps_wise_entry);
        context = PSWiseEntryActivity.this;
        viewModel = new PSEntryViewModel(this, getApplication());
        pollingStations=new ArrayList<>();

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                votes = binding.etVotes.getText().toString().trim();
                if (validateData()) {
                    Toast.makeText(PSWiseEntryActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.header.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewModel.getpollingStations().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> ps) {
                pollingStations = ps;

                ArrayAdapter adapter = new ArrayAdapter<>(context, R.layout.spinner_layout,
                        pollingStations);
                binding.spPollingStation.setAdapter(adapter);
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
}