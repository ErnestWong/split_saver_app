package com.noname.splitsaver.Network;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {

    private static String baseUrl = "http://split-saver.herokuapp.com/";
    private static SplitSaverService service;

    public static void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();

        service = retrofit.create(SplitSaverService.class);
    }

    public static void postCreateUser(Callback<ResponseBody> callback, String name, String phoneNumber) {
        Call<ResponseBody> call = service.postCreateUser(name, phoneNumber);
        call.enqueue(callback);
    }

    public static void postLoginUser(Callback<ResponseBody> callback, String phoneNumber) {
        Call<ResponseBody> call = service.postLoginUser(phoneNumber);
        call.enqueue(callback);
    }

    public static void getUserTransactions(Callback<ResponseBody> callback){
        Call<ResponseBody> call = service.getUserTransactions();
        call.enqueue(callback);
    }
}
