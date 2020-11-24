package com.cgg.ghmcpollingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.adapter.StatusListAdapter;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ps_wise_status);
        context = PSWiseStatusActivity.this;

        /*if (Utils.checkInternetConnection(DashboardActivity.this)) {
            viewModel.getApplicationListCall(request).observe(this, new Observer<ApplicationRes>() {
                @Override
                public void onChanged(ApplicationRes response) {

                    if (response != null && response.getStatusCode() != null) {
                        if (response.getStatusCode().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                            list = response.getData();
                            if (list != null && list.size() > 0) {
                                binding.tvPendCnt.setText("" + list.size());


                            }
                        } else if (response.getStatusCode().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                            Utils.customErrorAlert(Dashboard.this, getString(R.string.app_name), response.getStatusMessage());
                        } else {
                            Utils.customErrorAlert(DashboardActivity.this, getString(R.string.app_name), getString(R.string.something));
                        }

                    } else {
                        Utils.customErrorAlert(DashboardActivity.this, getString(R.string.app_name), getString(R.string.server_not));
                    }
                }
            });
        } else {
            Utils.customErrorAlert(DashboardActivity.this, getResources().getString(R.string.app_name),
                    getString(R.string.plz_check_int));
        }*/
//        adapter = new StatusListAdapter(context, statuslist);
//        binding.recyclerView.setAdapter(adapter);
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));

        binding.header.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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