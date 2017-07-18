package com.noname.splitsaver.Network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface SplitSaverService {

    @FormUrlEncoded
    @POST("user/")
    Call<ResponseBody> postCreateUser(@Field("name") String name, @Field("phoneNumber") String phoneNumber);

    @FormUrlEncoded
    @POST("user/login")
    Call<ResponseBody> postLoginUser(@Field("phoneNumber") String phoneNumber);

//    @FormUrlEncoded
    @GET("/digitalReciept/")
    Call<ResponseBody> getUserTransactions();


}
