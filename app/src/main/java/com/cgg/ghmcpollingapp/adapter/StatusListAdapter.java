package com.cgg.ghmcpollingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.databinding.ItemViewStatusBinding;
import com.cgg.ghmcpollingapp.model.response.report.ReportData;
import com.cgg.ghmcpollingapp.ui.PSWiseTimeSlotActivity;

import java.util.ArrayList;
import java.util.List;


public class StatusListAdapter extends RecyclerView.Adapter<StatusListAdapter.ItemHolder> implements Filterable {
    private Context context;
    private List<ReportData> list;
    private List<ReportData> filterList;


    public StatusListAdapter(Context context, List<ReportData> list) {
        this.context = context;
        this.list = list;
        filterList = new ArrayList<>();
        filterList.addAll(list);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewStatusBinding listItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_view_status, parent, false);
        return new ItemHolder(listItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int i) {
        final ReportData dataModel = filterList.get(i);
        holder.listItemBinding.setStatusData(dataModel);
        holder.bind(dataModel);

        holder.listItemBinding.psSlNo.setText(dataModel.getPollingstationno());
        holder.listItemBinding.psNo.setText(dataModel.getNameofthebuilding());
        holder.listItemBinding.totalReport.setText(dataModel.getTotalhourlyreport());
        holder.listItemBinding.pendingReport.setText(dataModel.getPendinghourlyreport());
        holder.listItemBinding.tvTotalVotes.setText(dataModel.gettOTALVOTES());
        holder.listItemBinding.tvPolledVotes.setText(dataModel.getvOTES());

        holder.listItemBinding.llData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PSWiseTimeSlotActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("PS_NO", dataModel.getPollingstationno())
                        .putExtra("PS_NAME", dataModel.getPollingstationno() + "-"
                                + dataModel.getNameofthebuilding()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return filterList != null && filterList.size() > 0 ? filterList.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                filterList.clear();
                if (charString.isEmpty()) {
                    filterList.addAll(list);
                } else {
                    for (ReportData pendingRequests : list) {
                        if (pendingRequests.getNameofthebuilding().toLowerCase().contains(charString.toLowerCase())
                                ||
                                String.valueOf(pendingRequests.getNameofthebuilding()).toLowerCase().contains(charString.toLowerCase())) {
                            filterList.add(pendingRequests);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterList = (ArrayList<ReportData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        ItemViewStatusBinding listItemBinding;

        ItemHolder(ItemViewStatusBinding listItemBinding) {
            super(listItemBinding.getRoot());
            this.listItemBinding = listItemBinding;
        }

        void bind(Object obj) {
            listItemBinding.executePendingBindings();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
