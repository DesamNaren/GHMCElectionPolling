package com.cgg.ghmcpollingapp.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.databinding.ActivityLoginBinding;
import com.cgg.ghmcpollingapp.error_handler.ErrorHandlerInterface;
import com.cgg.ghmcpollingapp.model.login.LoginRequest;
import com.cgg.ghmcpollingapp.model.login.LoginResponse;
import com.cgg.ghmcpollingapp.network.TLService;
import com.cgg.ghmcpollingapp.utils.CustomProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginResponse> responseMutableLiveData;
    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> mobileNum = new MutableLiveData<>();
    private Context context;
    private ActivityLoginBinding binding;
    private ErrorHandlerInterface errorHandlerInterface;
    private CustomProgressDialog customProgressDialog;

    public LoginViewModel(Context context) {
        this.context = context;
        customProgressDialog = new CustomProgressDialog(context);
        errorHandlerInterface = (ErrorHandlerInterface) context;
    }

    public LoginViewModel(ActivityLoginBinding binding, Context context) {
        this.binding = binding;
        this.context = context;
        customProgressDialog = new CustomProgressDialog(context);
        errorHandlerInterface = (ErrorHandlerInterface) context;
    }

    public LiveData<LoginResponse> getLoginCall() {
        responseMutableLiveData = new MutableLiveData<>();
        return responseMutableLiveData;
    }


    public void callLoginAPI(LoginRequest loginRequest) {
//        Utils.hideKeyboard(context, binding.btnSubmit);
        customProgressDialog.show();
        TLService tlService = TLService.Factory.create();
        tlService.getLoginResponse(loginRequest)
                .enqueue(new Callback<LoginResponse>() {
                             @Override
                             public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                                 customProgressDialog.dismiss();
                                 if (response.isSuccessful() && response.body() != null) {
                                     LoginResponse list = response.body();
                                     if (list != null)
                                         responseMutableLiveData.setValue(list);
                                 } else {
                                     errorHandlerInterface.handleError(context.getString(R.string.server_not), context);
                                 }
                             }

                             @Override
                             public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                                 customProgressDialog.dismiss();
                                 errorHandlerInterface.handleError(t, context);
                             }
                         }
                );
    }
}
