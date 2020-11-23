package com.cgg.ghmcpollingapp.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.adapter.StatusListAdapter;
import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.cgg.ghmcpollingapp.databinding.ActivityPsReportBinding;
import com.cgg.ghmcpollingapp.databinding.ActivityPsWiseStatusBinding;
import com.cgg.ghmcpollingapp.model.status.StatusListData;
import com.cgg.ghmcpollingapp.utils.Utils;

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
        adapter = new StatusListAdapter(context, statuslist);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));

    }
}