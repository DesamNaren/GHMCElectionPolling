package com.cgg.ghmcpollingapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.interfaces.PSEntryInterface;
import com.cgg.ghmcpollingapp.model.request.ps_entry.PSEntrySubmitRequest;
import com.cgg.ghmcpollingapp.model.request.ps_entry.PSSubmitData;
import com.cgg.ghmcpollingapp.utils.CustomProgressDialog;
import com.cgg.ghmcpollingapp.utils.Utils;

import java.util.List;

public class PSEntryConfirmAdapter
        extends RecyclerView.Adapter<PSEntryConfirmAdapter.RejectionHolder> {

    private final Context context;
    private final List<PSSubmitData> psEntrySubmitRequest;
    private final PSEntrySubmitRequest psEntrySubmitRequestMain;
    private final Dialog dialog;
    private final PSEntryInterface psEntryInterface;

    public PSEntryConfirmAdapter(Context context, List<PSSubmitData> psEntrySubmitRequest,
                                 PSEntrySubmitRequest psEntrySubmitRequestMain,
                                 Dialog dialog) {
        this.context = context;
        this.psEntrySubmitRequest = psEntrySubmitRequest;
        this.psEntrySubmitRequestMain = psEntrySubmitRequestMain;
        this.dialog = dialog;
        psEntryInterface = (PSEntryInterface) context;
    }

    @NonNull
    @Override
    public RejectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ps_entry_confirm_row, parent, false);
        return new RejectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectionHolder holder, int position) {

        try {
            PSSubmitData request = psEntrySubmitRequest.get(position);
            holder.psNum.setText(request.getPollingStationID());
            holder.count.setText(request.getVotePolled());

            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            holder.proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Utils.checkInternetConnection(context)) {
                        try {
                            dialog.dismiss();
                            psEntryInterface.confirmAlert(psEntrySubmitRequestMain);
                        } catch (Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } else {
                        Utils.customErrorAlert(context, context.getResources().getString(R.string.app_name), context.getString(R.string.plz_check_int));
                    }

                }
            });

            if (holder.getAdapterPosition()+1 == psEntrySubmitRequest.size()) {
                holder.btnLayout.setVisibility(View.VISIBLE);
            }else {
                holder.btnLayout.setVisibility(View.GONE);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return psEntrySubmitRequest!= null ? psEntrySubmitRequest.size() : 0;
    }

    static class RejectionHolder extends RecyclerView.ViewHolder {

        private final TextView psNum;
        private final TextView count;

        private final Button cancel;
        private final Button proceed;
        private LinearLayout btnLayout;

        RejectionHolder(@NonNull View itemView) {
            super(itemView);
            psNum = itemView.findViewById(R.id.psNum);
            count = itemView.findViewById(R.id.count);
            cancel = itemView.findViewById(R.id.btn_cancel);
            proceed = itemView.findViewById(R.id.btn_submit);
            btnLayout = itemView.findViewById(R.id.btnLayout);
        }
    }
}
