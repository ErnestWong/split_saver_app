package com.noname.splitsaver.Payee;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.Models.Payee;
import com.noname.splitsaver.R;
import com.noname.splitsaver.Utils.AssignmentListener;

import java.util.List;

public class PayeeRecyclerViewAdapter extends RecyclerView.Adapter {

    private List<Payee> payeeList;
    private List<Item> itemList;
    private AssignmentListener listener;

    public PayeeRecyclerViewAdapter(List<Payee> payeeList, List<Item> itemList, AssignmentListener listener) {
        this.payeeList = payeeList;
        this.itemList = itemList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_payee, parent, false);
        return new PayeeViewHolder(parent.getContext(), view, itemList, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PayeeViewHolder) holder).bindView(payeeList.get(position));
    }

    @Override
    public int getItemCount() {
        return payeeList.size();
    }
}
