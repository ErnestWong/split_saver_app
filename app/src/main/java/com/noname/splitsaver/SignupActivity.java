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
import com.nexmo.sdk.core.client.ClientBuilderException;
import com.nexmo.sdk.NexmoClient;
import com.nexmo.sdk.verify.client.VerifyClient;

public class SignupActivity extends AppCompatActivity {

    Button smsBtn;
    Button verifyBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        smsBtn=(Button)findViewById(R.id.smsButton);
        verifyBtn=(Button)findViewById(R.id.verify);

        Context context = getApplicationContext();
        NexmoClient nexmoClient=null;

        try {
            nexmoClient = new NexmoClient.NexmoClientBuilder()
                    .context(context)
                    .applicationId("674418ef-7dd1-4fdb-bf16-cc645b2eb9cf") //your App key
                    .sharedSecretKey("4d6547d61010730") //your App secret
                    .build();
        } catch (ClientBuilderException e) {
            e.printStackTrace();
        }

        final VerifyClient verifyClient = new VerifyClient(nexmoClient);

        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameText= (EditText)findViewById(R.id.name);
                EditText phoneText=(EditText)findViewById(R.id.phone);
                Log.d("I",nameText.toString());
                Log.d("I",phoneText.toString());

                verifyClient.getVerifiedUser("CA", "12267898040");
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText pinText=(EditText)findViewById(R.id.pin);
                verifyClient.checkPinCode(pinText.toString());
                MainActivity.startActivity(getApplicationContext());
            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
