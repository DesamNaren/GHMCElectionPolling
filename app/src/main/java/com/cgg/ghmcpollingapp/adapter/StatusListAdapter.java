package com.cgg.ghmcpollingapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.databinding.ItemViewStatusBinding;
import com.cgg.ghmcpollingapp.model.status.StatusListData;
import com.cgg.ghmcpollingapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class StatusListAdapter extends RecyclerView.Adapter<StatusListAdapter.ItemHolder>
        implements Filterable {
    private Context context;
    private List<StatusListData> list;
    private List<StatusListData> mFilteredList;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public StatusListAdapter(Context context, List<StatusListData> list) {
        this.context = context;
        this.list = list;
        mFilteredList = list;
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
        final StatusListData dataModel = mFilteredList.get(i);
        holder.listItemBinding.setStatusData(dataModel);
        holder.bind(dataModel);

        holder.listItemBinding.psNo.setText(dataModel.getAPPLICATIONID());
        holder.listItemBinding.totalReport.setText(dataModel.getNAME());
        holder.listItemBinding.pendingReport.setText(dataModel.getFATHERHUSBANDNAME());

    }

    @Override
    public int getItemCount() {
        return mFilteredList != null && mFilteredList.size() > 0 ? mFilteredList.size() : 0;
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = list;
                } else {
                    try {
                        ArrayList<StatusListData> filteredList = new ArrayList<>();
                        for (StatusListData detailsData : list) {
                            if (!TextUtils.isEmpty(detailsData.getAPPLICATIONID())
                                    && detailsData.getAPPLICATIONID().toLowerCase().contains(charString.toLowerCase())
                                    ||
                                    !TextUtils.isEmpty(detailsData.getNAME()) &&
                                            detailsData.getNAME().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(detailsData);
                            }
                        }
                        mFilteredList = filteredList;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<StatusListData>) filterResults.values;
                notifyDataSetChanged();

                getFilteredData();
            }
        };
    }

    public List<StatusListData> getFilteredData() {
        return mFilteredList;
    }


}
