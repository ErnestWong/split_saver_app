package com.noname.splitsaver.Network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SplitSaverService {

    @GET("users?page=2")
    Call<JsonObject> getAllTransactions();
}
