package com.noname.splitsaver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.nexmo.sdk.NexmoClient;
import com.nexmo.sdk.core.client.ClientBuilderException;
import com.nexmo.sdk.verify.client.VerifyClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    @BindView(R.id.name_edit_text)
    EditText nameEditText;
    @BindView(R.id.phone_edit_text)
    EditText phoneEditText;

    private VerifyClient verifyClient;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        NexmoClient nexmoClient;
        try {
            nexmoClient = new NexmoClient.NexmoClientBuilder()
                    .context(getApplicationContext())
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
        Log.d(TAG, "send button clicked");
        if (verifyClient != null) {
            try {
                verifyClient.getVerifiedUser("CA", "16472892801");
                Log.d(TAG, "finished sending phone number");
                PinActivity.startActivity(getApplicationContext());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            Log.e(TAG, "verifyClient null");
        }

    }

}
