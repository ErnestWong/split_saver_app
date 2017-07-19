package com.noname.splitsaver.Transaction;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.Models.Transaction;
import com.noname.splitsaver.Network.NetworkManager;
import com.noname.splitsaver.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Date;
import android.widget.TextView;
import android.view.View.OnClickListener;

class TransactionAdapter extends RecyclerView.Adapter {

    private List<Transaction> transactionList = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private static final String TAG = "PinActivity";


    TransactionAdapter() {
        getData();

    }

    public interface OnItemClickListener {
        public void onClick(View view, int position);
    }




    private void getData() {
        Callback<List<Transaction>> callback = new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if (response.isSuccessful()) {
                    List<Transaction> tList = response.body();
                    Collections.reverse(tList);
                    transactions= tList;


                    populateData(tList);
                    Log.d("DAVID", "onResponse price: " + transactionList.get(0).getTotalPrice());
                } else {
                    Log.d("DAVID", "onResponse Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                t.printStackTrace();
            }
        };
        NetworkManager.getDigitalReceipts(callback);
    }

    private void populateData(List<Transaction> transactions) {
        for(int i=0; i<transactions.size(); i++){
            Transaction t = transactions.get(i);
            transactionList.add(new Transaction("Transaction "+t.getId(),t.getTotalPrice()));
            notifyItemChanged(i);
        }
        notifyItemChanged(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TransactionViewHolder) holder).bindView(transactions.get(position));
    }


    @Override
    public int getItemCount() {
        return transactionList.size();
    }

}
