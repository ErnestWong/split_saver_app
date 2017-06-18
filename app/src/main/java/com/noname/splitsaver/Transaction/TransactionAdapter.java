package com.noname.splitsaver.Transaction;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noname.splitsaver.Models.Transaction;
import com.noname.splitsaver.R;

import java.util.ArrayList;
import java.util.List;

class TransactionAdapter extends RecyclerView.Adapter {

    private List<Transaction> transactionList = new ArrayList<>();

    TransactionAdapter() {
        populateDummyData();
    }

    private void populateDummyData() {
        for (int i = 0; i < 100; i++) {
            transactionList.add(new Transaction("Dummy Transaction " + i, "Dummy Price " + i));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_view, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TransactionViewHolder) holder).bindView(transactionList.get(position));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

}
