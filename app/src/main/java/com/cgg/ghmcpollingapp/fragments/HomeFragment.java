package com.cgg.ghmcpollingapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.databinding.FragmentHomeBinding;
import com.cgg.ghmcpollingapp.ui.PSWiseEntryActivity;
import com.cgg.ghmcpollingapp.ui.PSWiseStatusActivity;
import com.cgg.ghmcpollingapp.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.cgg.ghmcpollingapp.databinding.FragmentHomeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

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