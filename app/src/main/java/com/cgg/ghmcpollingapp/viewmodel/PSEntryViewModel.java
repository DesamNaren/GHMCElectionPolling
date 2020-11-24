package com.cgg.ghmcpollingapp.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cgg.ghmcpollingapp.room.repository.PollingMasterRep;

import java.util.List;

public class PSEntryViewModel extends AndroidViewModel {
    private PollingMasterRep pollingMasterRep;
//    private MutableLiveData<ViewTaskResponse> viewTaskResponseMutableLiveData;
//    private LogEditInterface viewTaskInterface;

//    private Context context;
//    private ErrorHandlerInterface errorHandlerInterface;

    private LiveData<List<String>> pollingStations;

    public PSEntryViewModel(Context context, Application application) {
        super(application);
//        this.context = context;
//        viewTaskResponseMutableLiveData = new MutableLiveData<>();
//        errorHandlerInterface = (ErrorHandlerInterface) context;
//        viewTaskInterface = (LogEditInterface) context;
        pollingMasterRep=new PollingMasterRep(application);
        pollingStations=new MutableLiveData<>();

    }


    public LiveData<List<String>> getpollingStations() {
        if (pollingStations != null) {
            pollingStations = pollingMasterRep.getPollingStations();
        }
        return pollingStations;
    }
//    public void getPSDetails(AttendanceRequest attendanceRequest) {
//        Gson gson = new Gson();
//        String str = gson.toJson(attendanceRequest);
//        VirtuoService virtuoService = VirtuoService.Factory.create();
//        virtuoService.submitAttendance(attendanceRequest).enqueue(new Callback<AttendanceResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<AttendanceResponse> call, @NonNull Response<AttendanceResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
////                    attendanceInterface.submitAttendance(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<AttendanceResponse> call, @NonNull Throwable t) {
//                errorHandlerInterface.handleError(t, context);
//            }
//        });
//    }

//    public void submitAttendanceCall(AttendanceRequest attendanceRequest) {
//        Gson gson = new Gson();
//        String str = gson.toJson(attendanceRequest);
//        VirtuoService virtuoService = VirtuoService.Factory.create();
//        virtuoService.submitAttendance(attendanceRequest).enqueue(new Callback<AttendanceResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<AttendanceResponse> call, @NonNull Response<AttendanceResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
////                    attendanceInterface.submitAttendance(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<AttendanceResponse> call, @NonNull Throwable t) {
//                errorHandlerInterface.handleError(t, context);
//            }
//        });
//    }

}



