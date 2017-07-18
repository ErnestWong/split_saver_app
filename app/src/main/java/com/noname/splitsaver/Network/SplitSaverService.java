package com.noname.splitsaver.Network;

import com.noname.splitsaver.Models.Transaction;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SplitSaverService {

    @FormUrlEncoded
    @POST("user/")
    Call<ResponseBody> postCreateUser(@Field("name") String name, @Field("phoneNumber") String phoneNumber);

    @FormUrlEncoded
    @POST("user/login")
    Call<ResponseBody> postLoginUser(@Field("phoneNumber") String phoneNumber);

    @FormUrlEncoded
    @POST("user/notify")
    Call<ResponseBody> postPaymentRequest(@Field("phoneNumber") String phoneNumber, @Field("digitalReceiptId") String digitalReceiptId);

    @POST("digitalreceipt/")
    Call<ResponseBody> postCreateDigitalReceipt(@Body Transaction transaction);
}
