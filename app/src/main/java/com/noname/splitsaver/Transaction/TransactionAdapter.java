package com.noname.splitsaver.Transaction;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noname.splitsaver.Models.Transaction;
import com.noname.splitsaver.Network.NetworkManager;
import com.noname.splitsaver.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class TransactionAdapter extends RecyclerView.Adapter {

    private List<Transaction> transactionList = new ArrayList<>();

    TransactionAdapter() {
        getData();
        populateDummyData();
    }

    private void getData() {
        Callback<List<Transaction>> callback = new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if (response.isSuccessful()) {
                    List<Transaction> transactionList = response.body();
                    Log.d("DAVID", "onResponse: " + response.body());
                } else {
                    Log.d("DAVID", "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                t.printStackTrace();
            }
        };
        NetworkManager.getDigitalReceipts(callback);
    }

    private void populateDummyData() {
        for (int i = 0; i < 100; i++) {
            transactionList.add(new Transaction("Transaction " + i, i));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_transaction, parent, false);
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
