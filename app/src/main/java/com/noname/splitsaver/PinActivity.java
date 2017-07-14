package com.noname.splitsaver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.noname.splitsaver.Network.NetworkManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PinActivity extends Activity {

    private static final String TAG = "PinActivity";

    private static final String EXTRA_NAME = "extraName";
    private static final String EXTRA_PHONE = "extraPhone";

    @BindView(R.id.pin_edit_text)
    EditText pinEditText;

    Callback<ResponseBody> postUserCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: 200 - " + response.body());
                    SplitSaverApplication.login(getApplicationContext());
                    MainActivity.startActivity(getApplicationContext());
                    PinActivity.this.finish();
                } else {
                    Log.e(TAG, "onResponse: " + response.body());
                }
            } else {
                Log.e(TAG, "onResponse: failed - " + response.errorBody());
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            t.printStackTrace();
        }
    };
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
        String pin = pinEditText.getText().toString();
        if (!pin.equals("")) { //verify pin
            NetworkManager.postUser(postUserCallback, name, phoneNumber);
        }
        MainActivity.startActivity(getApplicationContext());
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
