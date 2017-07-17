package com.noname.splitsaver.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.nexmo.sdk.verify.client.VerifyClient;
import com.noname.splitsaver.MainApplication;
import com.noname.splitsaver.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    @BindView(R.id.name_editText)
    EditText nameEditText;
    @BindView(R.id.phone_editText)
    EditText phoneEditText;

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
    }

    @OnClick(R.id.signup_btn)
    void onCapturedVerify() {
        Log.d(TAG, "send button clicked");
        String name = nameEditText.getText().toString();
        String phoneNumber = phoneEditText.getText().toString();
        Log.d(TAG, "Phone # is "+phoneNumber);

        if (name.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Name or Phone Number Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        VerifyClient verifyClient = MainApplication.getVerifyClient();
        if (verifyClient != null) {
            try {
                verifyClient.getVerifiedUser("CA", "1" + phoneNumber);
                Log.d(TAG, "finished sending phone number");
                PinActivity.startActivityAsSignup(getApplicationContext(), name, phoneNumber);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            Log.e(TAG, "verifyClient null");
        }

    }

}
