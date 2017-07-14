package com.noname.splitsaver.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.noname.splitsaver.MainActivity;
import com.noname.splitsaver.MainApplication;
import com.noname.splitsaver.Network.NetworkManager;
import com.noname.splitsaver.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PinActivity extends Activity {

    private static final int TYPE_SIGNUP = 1;
    private static final int TYPE_LOGIN = 2;
    private static final String TAG = "PinActivity";
    private static final String EXTRA_NAME = "extraName";
    private static final String EXTRA_PHONE = "extraPhone";
    private static final String EXTRA_TYPE = "extraType";

    @BindView(R.id.pin_edit_text)
    EditText pinEditText;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private String name;
    private String phoneNumber;
    private int type;

    Callback<ResponseBody> postLoginUserCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: 200 - " + response.body());
                    MainApplication.login(getApplicationContext());
                    progressBar.setVisibility(View.GONE);
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

    Callback<ResponseBody> postCreateUserCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: 200 - " + response.body());
                    NetworkManager.postLoginUser(postLoginUserCallback, phoneNumber);
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

    public static void startActivityAsSignup(Context context, String name, String phoneNumber) {
        Intent intent = new Intent(context, PinActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_PHONE, phoneNumber);
        intent.putExtra(EXTRA_TYPE, TYPE_SIGNUP);
        context.startActivity(intent);
    }

    public static void startActivityAsLogin(Context context, String phoneNumber) {
        Intent intent = new Intent(context, PinActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_PHONE, phoneNumber);
        intent.putExtra(EXTRA_TYPE, TYPE_LOGIN);
        context.startActivity(intent);
    }

    @OnClick(R.id.verify_sms_btn)
    void onCapturedSMS() {
        Log.d(TAG, "onCapturedSMS: ");
        String pin = pinEditText.getText().toString();
        if (pin.equals("")) { //verify pin
            Toast.makeText(this, "Pin is empty", Toast.LENGTH_SHORT).show();
        } else {
            if (type == TYPE_SIGNUP) {
                NetworkManager.postCreateUser(postCreateUserCallback, name, phoneNumber);
            } else if (type == TYPE_LOGIN) {
                NetworkManager.postLoginUser(postLoginUserCallback, phoneNumber);
            }
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        ButterKnife.bind(this);

        progressBar.setVisibility(View.GONE);

        populateData();
    }

    private void populateData() {
        Intent intent = getIntent();
        name = intent.getStringExtra(EXTRA_NAME);
        phoneNumber = intent.getStringExtra(EXTRA_PHONE);
        type = intent.getIntExtra(EXTRA_TYPE, 0);
    }
}
