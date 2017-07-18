package com.noname.splitsaver.Transaction;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.noname.splitsaver.Models.Transaction;
import com.noname.splitsaver.Network.NetworkManager;
import com.noname.splitsaver.R;

import java.util.ArrayList;
import java.util.Date;
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
//        Callback<JsonObject> callback = new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (response.isSuccessful()) {
//                    String jsonString = response.body().toString();
//                    Log.d("DAVID", "onResponse: " + jsonString);
//                } else {
//                    Log.d("DAVID", "onResponse: " + response.errorBody());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                t.printStackTrace();
//            }
//        };
//        NetworkManager.getAllItems(callback);
    }

    private void populateDummyData() {
        for (int i = 0; i < 100; i++) {
            transactionList.add(new Transaction("Transaction " + i, i));
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
