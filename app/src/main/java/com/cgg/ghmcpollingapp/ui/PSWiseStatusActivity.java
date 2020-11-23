package com.cgg.ghmcpollingapp.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.adapter.StatusListAdapter;
import com.cgg.ghmcpollingapp.databinding.ActivityPsReportBinding;
import com.cgg.ghmcpollingapp.databinding.ActivityPsWiseStatusBinding;
import com.cgg.ghmcpollingapp.model.status.StatusListData;

import java.util.List;

public class PSWiseStatusActivity extends AppCompatActivity {
    private Context context;
    private List<StatusListData> statuslist;
    private StatusListAdapter adapter;
    private ActivityPsWiseStatusBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_ps_wise_status);
        context = PSWiseStatusActivity.this;

        adapter = new StatusListAdapter(context, statuslist);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));

    }
}