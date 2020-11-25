package com.cgg.ghmcpollingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.databinding.ItemViewStatusBinding;
import com.cgg.ghmcpollingapp.databinding.ItemViewTimeSlotStatusBinding;
import com.cgg.ghmcpollingapp.model.response.report.ReportData;

import java.util.ArrayList;
import java.util.List;


public class StatusListTimeSlotAdapter extends RecyclerView.Adapter<StatusListTimeSlotAdapter.ItemHolder> implements Filterable {
    private Context context;
    private List<ReportData> list;
    private List<ReportData> filterList;


    public StatusListTimeSlotAdapter(Context context, List<ReportData> list) {
        this.context = context;
        this.list = list;
        filterList = new ArrayList<>();
        filterList.addAll(list);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewTimeSlotStatusBinding listItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_view_time_slot_status, parent, false);
        return new ItemHolder(listItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int i) {
        final ReportData dataModel = filterList.get(i);
        holder.listItemBinding.setStatusData(dataModel);
        holder.bind(dataModel);

        holder.listItemBinding.tvTime.setText(dataModel.gettIMESLOTNAME());
        holder.listItemBinding.tvPolledVotes.setText(dataModel.getvOTES());
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
                        if (pendingRequests.gettIMESLOTNAME().toLowerCase().contains(charString.toLowerCase())
                                ||
                                String.valueOf(pendingRequests.gettIMESLOTNAME()).toLowerCase().contains(charString.toLowerCase())) {
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

        ItemViewTimeSlotStatusBinding listItemBinding;

        ItemHolder(ItemViewTimeSlotStatusBinding listItemBinding) {
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
