package com.noname.splitsaver.Network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SplitSaverService {

    @FormUrlEncoded
    @POST("user/createUser")
    Call<ResponseBody> postUser(@Field("name") String name, @Field("phoneNumber") String phoneNumber);
}
