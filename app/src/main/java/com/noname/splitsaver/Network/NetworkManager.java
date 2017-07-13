package com.noname.splitsaver.Network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {

    private static String baseUrl = "https://reqres.in/api/";
    private static SplitSaverService service;

    public static void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();

        service = retrofit.create(SplitSaverService.class);
    }

    public static void postUser(Callback<JsonObject> callback, String name, String phoneNumber) {
        Call<JsonObject> call = service.postUser(name, phoneNumber);
        call.enqueue(callback);
    }
}
