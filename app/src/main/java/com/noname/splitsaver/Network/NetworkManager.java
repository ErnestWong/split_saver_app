package com.noname.splitsaver.Network;

import com.noname.splitsaver.Models.Transaction;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {

    private static String baseUrl = "http://split-saver.herokuapp.com/";
    private static SplitSaverService service;

    public static void init() {
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    List<Cookie> cookies;

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        this.cookies = cookies;
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        if (cookies != null)
                            return cookies;
                        return new ArrayList<>();
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(client)
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

    public static void postCreateDigitalReceipt(Callback<ResponseBody> callback, Transaction transaction) {
        Call<ResponseBody> call = service.postCreateDigitalReceipt(transaction);
        call.enqueue(callback);
    }

    public static void getDigitalReceipts(Callback<List<Transaction>> callback) {
        Call<List<Transaction>> call = service.getDigitalReceipts();
        call.enqueue(callback);
    }
}
