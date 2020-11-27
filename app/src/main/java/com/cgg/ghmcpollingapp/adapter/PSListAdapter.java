package com.cgg.ghmcpollingapp.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cgg.ghmcpollingapp.R;
import com.cgg.ghmcpollingapp.databinding.ItemViewPsBinding;
import com.cgg.ghmcpollingapp.model.response.psList.PSListData;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


public class PSListAdapter extends RecyclerView.Adapter<PSListAdapter.ItemHolder> implements Filterable {
    private Context context;
    private List<PSListData> list;
    private List<PSListData> filterList;


    public PSListAdapter(Context context, List<PSListData> list) {
        this.context = context;
        this.list = list;
        filterList = new ArrayList<>();
        filterList.addAll(list);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewPsBinding listItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_view_ps, parent, false);
        return new ItemHolder(listItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int i) {
        final PSListData dataModel = filterList.get(i);
        holder.listItemBinding.setStatusData(dataModel);
        holder.bind(dataModel);

        holder.listItemBinding.psSlNo.setText(dataModel.getPollingStationID());
        holder.listItemBinding.tvTotalVotes.setText(dataModel.getVotePolled());

        holder.listItemBinding.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    holder.listItemBinding.etPolledVotes.setText("");
                }
                holder.listItemBinding.etPolledVotes.setEnabled(isChecked);
                dataModel.setCb_status(isChecked);

            }
        });

        RxTextView
                .textChangeEvents(holder.listItemBinding.etPolledVotes)
                .debounce(100, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TextViewTextChangeEvent>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull TextViewTextChangeEvent textViewTextChangeEvent) {
                        dataModel.setvOTES(textViewTextChangeEvent.text().toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        holder.listItemBinding.etPolledVotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dataModel.setvOTES(s.toString());
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
                    for (PSListData pendingRequests : list) {
                        if (String.valueOf(pendingRequests.getPollingStationID()).toLowerCase().contains(charString.toLowerCase())) {
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
                filterList = (ArrayList<PSListData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        ItemViewPsBinding listItemBinding;

        ItemHolder(ItemViewPsBinding listItemBinding) {
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
