package com.cgg.ghmcpollingapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.application.PollingApplication;
import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.cgg.ghmcpollingapp.databinding.FragmentHomeBinding;
import com.cgg.ghmcpollingapp.model.response.login.LoginResponse;
import com.cgg.ghmcpollingapp.ui.PSWiseEntryActivity;
import com.cgg.ghmcpollingapp.ui.PSWiseStatusActivity;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.google.gson.Gson;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private SharedPreferences sharedPreferences;
    String zoneName, circleName, wardName, sectorName, wardId;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        sharedPreferences = PollingApplication.get(getActivity()).getPreferences();
        String data = sharedPreferences.getString(AppConstants.LOGIN_RES, "");
        LoginResponse loginResponse = new Gson().fromJson(data, LoginResponse.class);

        if (loginResponse != null && loginResponse.getLoginData() != null && loginResponse.getLoginData().get(0) != null) {

            if (loginResponse.getLoginData().get(0).getName() != null)
                binding.profileLl.tvUserName.setText(loginResponse.getLoginData().get(0).getName());

            if (loginResponse.getLoginData().get(0).getMobileNo() != null)
                binding.profileLl.tvUserContact.setText(loginResponse.getLoginData().get(0).getMobileNo());


            zoneName = sharedPreferences.getString(AppConstants.ZONE_NAME, "");
            circleName = sharedPreferences.getString(AppConstants.CIRCLE_NAME, "");
            wardName = sharedPreferences.getString(AppConstants.WARD_NAME, "");
            wardId = sharedPreferences.getString(AppConstants.WARD_ID, "");
            sectorName = sharedPreferences.getString(AppConstants.SECTOR_NAME, "");

            binding.tvZone.setText(zoneName);
            binding.tvCircle.setText(circleName);
            binding.tvWard.setText(wardName + " (" + wardId + ")");
            binding.tvSector.setText(sectorName);

        } else {
            Utils.customErrorAlert(getActivity(), getString(R.string.app_name),
                    getString(R.string.something) + " while fetching login response onCreate");
        }

        binding.llPsWiseEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PSWiseEntryActivity.class));
            }
        });

        binding.llPsWiseStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PSWiseStatusActivity.class));
            }
        });


        return binding.getRoot();
    }
}