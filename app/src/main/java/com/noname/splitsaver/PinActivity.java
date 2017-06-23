package com.noname.splitsaver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class PinActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.verify_sms_btn)
    void onCapturedSMS() {
        Log.d("PinActivity", "verify button clicked");
        MainActivity.startActivity(getApplicationContext(), true);
    }

    public static void startActivity(Context context) {
        Log.d("PinActivity", "trying to start");
        Intent intent = new Intent(context, PinActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
