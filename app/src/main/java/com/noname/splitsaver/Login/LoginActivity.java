package com.noname.splitsaver.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.nexmo.sdk.verify.client.VerifyClient;
import com.noname.splitsaver.MainActivity;
import com.noname.splitsaver.MainApplication;
import com.noname.splitsaver.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.phone_editText)
    EditText phoneEditText;

    public static void startActivity(Context context) {
        boolean isLogged = MainApplication.isLoggedIn(context);
        if (isLogged) {
            MainActivity.startActivity(context);
        } else {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.skip_login)
    void onSkipLogin() {
        MainApplication.login(getApplicationContext());
        MainActivity.startActivity(getApplicationContext());
    }

    @OnClick(R.id.login_btn)
    void onCapturedVerify() {
        Log.d(TAG, "send button clicked");
        String phoneNumber = phoneEditText.getText().toString();
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Name or Phone Number Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        VerifyClient verifyClient = MainApplication.getVerifyClient();
        if (verifyClient != null) {
            try {
                verifyClient.getVerifiedUser("CA", "1" + phoneNumber);
                Log.d(TAG, "finished sending phone number");
                PinActivity.startActivityAsLogin(getApplicationContext(), phoneNumber);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            Log.e(TAG, "verifyClient null");
        }
    }

    @OnClick(R.id.signup_btn)
    void onSignupClicked() {
        SignupActivity.startActivity(getApplicationContext());
    }
}
