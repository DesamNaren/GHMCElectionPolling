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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.application.PollingApplication;
import com.cgg.ghmcpollingapp.constants.AppConstants;
import com.cgg.ghmcpollingapp.databinding.ActivityDashboardBinding;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.model.response.login.LoginResponse;
import com.cgg.ghmcpollingapp.utils.Utils;
import com.cgg.ghmcpollingapp.viewmodel.LogoutViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

public class DashboardActivity extends AppCompatActivity implements ErrorHandlerInterface {

    private AppBarConfiguration mAppBarConfiguration;
    private LogoutViewModel logoutViewModel;
    private String mobNum;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        context = DashboardActivity.this;

        sharedPreferences = PollingApplication.get(context).getPreferences();
        editor = PollingApplication.get(context).getPreferencesEditor();
        mobNum = sharedPreferences.getString(AppConstants.MOBILE_NO, "");
        logoutViewModel = new LogoutViewModel(this, getApplication());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        MenuItem navHome = navigationView.getMenu().findItem(R.id.nav_my_home);
        navHome.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent newIntent = new Intent(DashboardActivity.this, DashboardActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
                //do as you want with the button click

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        MenuItem navPSEntry = navigationView.getMenu().findItem(R.id.nav_ps_entry);
        navPSEntry.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent newIntent = new Intent(DashboardActivity.this, PSWiseEntryActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
                //do as you want with the button click

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        MenuItem navPSReport = navigationView.getMenu().findItem(R.id.nav_ps_report);
        navPSReport.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent newIntent = new Intent(DashboardActivity.this,
                        PSWiseStatusActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
                //do as you want with the button click

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });


        MenuItem navDownloadMaster = navigationView.getMenu().findItem(R.id.nav_download_master);
        navDownloadMaster.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent newIntent = new Intent(DashboardActivity.this, DownloadMasterActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                newIntent.putExtra(AppConstants.FROM_CLASS, DashboardActivity.class.getSimpleName());
                startActivity(newIntent);

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        MenuItem navLogout = navigationView.getMenu().findItem(R.id.nav_log_out);
        navLogout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                onBackPressed();
                return true;
            }
        });

        String data = sharedPreferences.getString(AppConstants.LOGIN_RES, "");
        LoginResponse loginResponse = new Gson().fromJson(data, LoginResponse.class);

        if (loginResponse != null && loginResponse.getLoginData() != null && loginResponse.getLoginData().get(0) != null) {

            TextView tvUser = binding.navView.getHeaderView(0).findViewById(R.id.tv_user_name);
            if (loginResponse.getLoginData().get(0).getName() != null)
                tvUser.setText(loginResponse.getLoginData().get(0).getName());

            TextView tvDes = binding.navView.getHeaderView(0).findViewById(R.id.tv_user_mob);
            if (loginResponse.getLoginData().get(0).getMobileNo() != null)
                tvDes.setText(loginResponse.getLoginData().get(0).getMobileNo());
        } else {
            Utils.customErrorAlert(context, getString(R.string.app_name),
                    getString(R.string.something) + " while fetching login response onCreate");
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void customLogoutAlert(Activity activity, String msg) {
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
                dialogTitle.setText(getString(R.string.app_name));
                TextView dialogMessage = dialog.findViewById(R.id.dialog_message);
                dialogMessage.setText(msg);
                Button exit = dialog.findViewById(R.id.btDialogExit);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Intent newIntent = new Intent(activity, QuitAppActivity.class);
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(newIntent);
                        activity.finish();

//                        LogoutRequest logoutRequest = new LogoutRequest();
//                        logoutRequest.setMobileNo(mobNum);
//                        logoutViewModel.logoutCall(logoutRequest).observe(DashboardActivity.this,
//                                new Observer<LogoutResponse>() {
//                                    @Override
//                                    public void onChanged(LogoutResponse logoutResponse) {
//                                        editor.clear();
//                                        editor.commit();
//
//                                    }
//                                });
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
    public void onBackPressed() {
        customLogoutAlert(DashboardActivity.this,
                getString(R.string.logout_from_app));
    }

    @Override
    public void handleError(Throwable e, Context context) {

    }

    @Override
    public void handleError(String e, Context context) {

    }
}