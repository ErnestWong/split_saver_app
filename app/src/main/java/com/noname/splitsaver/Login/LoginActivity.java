package com.noname.splitsaver.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nexmo.sdk.verify.client.VerifyClient;
import com.nexmo.sdk.verify.event.SearchListener;
import com.nexmo.sdk.verify.event.UserStatus;
import com.nexmo.sdk.verify.event.VerifyError;
import com.noname.splitsaver.MainActivity;
import com.noname.splitsaver.MainApplication;
import com.noname.splitsaver.R;

import java.io.IOException;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.phone_editText)
    EditText phoneEditText;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

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

        progressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.login_btn)
    void onCapturedVerify() {
        Log.d(TAG, "send button clicked");
        final String phoneNumber = phoneEditText.getText().toString();
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Phone Number Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        VerifyClient verifyClient = MainApplication.getVerifyClient();
        if (verifyClient != null) {
            try {
                verifyClient.getUserStatus("CA", "1" + phoneNumber, new SearchListener() {
                    @Override
                    public void onUserStatus(UserStatus userStatus) {
                        switch (userStatus) {

                            case USER_VERIFIED: {
                                PinActivity.startActivityAsLogin(getApplicationContext(), phoneNumber);
                            }case USER_UNKNOWN:{
                                //Toast();
                            }
                        }
                        //Toast();
                    }

                    @Override
                    public void onError(VerifyError errorCode, String errorMessage) {
                        Log.d(TAG, "onError ErrorCode" + errorCode + ":" + errorMessage);
                    }

                    @Override
                    public void onException(IOException exception) {
                        Log.e(TAG, "onException: ", exception);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            Log.e(TAG, "verifyClient null");
        }
        //Toast.makeText(this, "Unverified Phone #. Please SignUp", Toast.LENGTH_SHORT).show();

    }
    public void Toast(){
        Toast.makeText(this, "Unverified Phone #. Please SignUp", Toast.LENGTH_SHORT).show();
        return;

    }

    @OnClick(R.id.signup_btn)
    void onSignupClicked() {
        SignupActivity.startActivity(getApplicationContext());
    }
}
