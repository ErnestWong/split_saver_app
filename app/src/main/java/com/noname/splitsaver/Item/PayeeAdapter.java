package com.noname.splitsaver.Item;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.Models.Payee;
import com.noname.splitsaver.R;

import java.util.List;

public class PayeeAdapter extends RecyclerView.Adapter {

    private List<Payee> payeeList;
    private List<Item> itemList;
    private AssignmentListener listener;

    public PayeeAdapter(List<Payee> payeeList, List<Item> itemList, AssignmentListener listener) {
        this.payeeList = payeeList;
        this.itemList = itemList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payee_view, parent, false);
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
