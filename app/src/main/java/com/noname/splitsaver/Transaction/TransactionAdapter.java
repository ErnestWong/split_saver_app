package com.noname.splitsaver.Transaction;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.noname.splitsaver.Models.Transaction;
import com.noname.splitsaver.Network.NetworkManager;
import com.noname.splitsaver.Network.SplitSaverService;
import com.noname.splitsaver.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

class TransactionAdapter extends RecyclerView.Adapter {
    private static final String TAG = "TransactionAdapter";


    private List<Transaction> transactionList = new ArrayList<>();

    TransactionAdapter() {
        getData();
    }

    private static String baseUrl = "http://split-saver.herokuapp.com/";
    private static SplitSaverService service;


    private void getData() {

//        Retrofit retrofit = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(baseUrl)
//                .build();
//
//        service = retrofit.create(SplitSaverService.class);
//
//        Call<ResponseBody> result = service.getUserTransactions();
//        result.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if(response.isSuccessful()){
//                    Log.d(TAG, "RESPONSE IS"+response.body().toString());
//                }else{
//                    Log.d(TAG,"ERROR: "+response.message());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }


        //String json = retrofitService().getInfo().execute().body().string();



        Callback<ResponseBody> callback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String json = response.body().toString();
                    Log.d(TAG, "onResponse: " + json);
                    parseJson(json);
                } else {
                    Log.d(TAG, "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        };
        NetworkManager.getUserTransactions(callback);
    }

    private void parseJson(String json){
        //Parse and add json transaction data to transactionList
    }

//    private void populateDummyData() {
//        getData();
//        for (int i = 0; i < 100; i++) {
//            Date createDate = new Date();
//            Date purchaseDate = new Date(i, i % 12, i % 30);
//            transactionList.add(new Transaction("Transaction " + i, i, createDate, purchaseDate));
//        }
//    }

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
