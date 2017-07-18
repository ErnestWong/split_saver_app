package com.noname.splitsaver.Transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.nexmo.sdk.verify.client.VerifyClient;
import com.noname.splitsaver.Login.PinActivity;
import com.noname.splitsaver.Login.SignupActivity;
import com.noname.splitsaver.MainApplication;
import com.noname.splitsaver.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Daniel on 7/17/2017.
 */

public class TransactionDetailsActivity extends AppCompatActivity {

    private static final String TAG = "TransDetailsActivity";
    String transactionId;
    String json;

    public static void startActivity(Context context, String transactionId, String json) {
        Intent intent = new Intent(context, TransactionDetailsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_details_view);

        TextView total =(TextView)findViewById(R.id.TransactionTotal);
        total.append(getTotalFromJson(json));
        ButterKnife.bind(this);
    }

    @OnClick(R.id.send_reminder_btn)
    void onSendReminderClicked() {
        Log.d(TAG, "send reminder  button clicked");
        // TODO RecipientListActivity.startActivity(getApplicationContext(), transactionId, json);
    }

    // TODO parse json
    private String getTotalFromJson(String json) {
        return json;
    }
}
