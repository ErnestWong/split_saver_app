package com.noname.splitsaver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;
import com.noname.splitsaver.Network.NetworkManager;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PinActivity extends Activity {

    private static final String TAG = "PinActivity";

    private static final String EXTRA_NAME = "extraName";
    private static final String EXTRA_PHONE = "extraPhone";

    private String name;
    private String phoneNumber;

    public static void startActivity(Context context, String name, String phoneNumber) {
        Intent intent = new Intent(context, PinActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_PHONE, phoneNumber);
        context.startActivity(intent);
    }

    @OnClick(R.id.verify_sms_btn)
    void onCapturedSMS() {
        Log.d(TAG, "onCapturedSMS: ");
        Callback<JsonObject> callback = new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Log.d(TAG, "onResponse: " + response.body());
                        MainActivity.startActivity(getApplicationContext(), true);
                    } else {
                        Log.d(TAG, "onResponse: " + response.body());
                    }
                } else {
                    Log.d(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        };
        NetworkManager.postUser(callback, name, phoneNumber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        name = intent.getStringExtra(EXTRA_NAME);
        phoneNumber = intent.getStringExtra(EXTRA_PHONE);
    }
}
