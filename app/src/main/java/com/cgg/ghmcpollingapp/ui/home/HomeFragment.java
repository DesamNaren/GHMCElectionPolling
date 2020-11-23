package com.cgg.ghmcpollingapp.ui.home;

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

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

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