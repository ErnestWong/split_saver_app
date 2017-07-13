package com.noname.splitsaver.Network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SplitSaverService {

    @FormUrlEncoded
    @POST("user/createUser")
    Call<JsonObject> postUser(@Field("name") String name, @Field("phoneNumber") String phoneNumber);
}
