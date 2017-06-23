package com.noname.splitsaver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.noname.splitsaver.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.nexmo.sdk.core.client.ClientBuilderException;
import com.nexmo.sdk.NexmoClient;
import com.nexmo.sdk.verify.client.VerifyClient;

public class SignupActivity extends AppCompatActivity {

    private VerifyClient verifyClient;
    private Context context;
    private EditText pinText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SignupActivity", "testing");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        pinText = (EditText)findViewById(R.id.pin_edit_text);

        context = getApplicationContext();
        NexmoClient nexmoClient;

        try {
            nexmoClient = new NexmoClient.NexmoClientBuilder()
                    .context(context)
                    .applicationId("674418ef-7dd1-4fdb-bf16-cc645b2eb9cf") //your App key
                    .sharedSecretKey("4d6547d61010730") //your App secret
                    .build();
            verifyClient = new VerifyClient(nexmoClient);
        } catch (ClientBuilderException e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.send_sms_btn)
    void onCapturedVerify() {
        Log.d("SignupActivity", "send button clicked");
        if (verifyClient != null) {
            try {
                //verifyClient.getVerifiedUser("CA", "16476798322");
                Log.d("SignupActivity", "finished sending phone number");
                PinActivity.startActivity(getApplicationContext());
            } catch (Exception e) {
                Log.e("Signup failure", e.getMessage());
            }
        } else {
            Log.e("SignupActivity", "verifyClient null");
        }

    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
