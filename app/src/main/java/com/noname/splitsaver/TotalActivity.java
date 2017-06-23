package com.noname.splitsaver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.noname.splitsaver.Models.Transaction;
import com.noname.splitsaver.Transaction.TransactionActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class TotalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);
        ButterKnife.bind(this);

        final TextView totalTextView = (TextView)findViewById(R.id.amount_textview);
        Intent intent = getIntent();
        if (intent.hasExtra(ImageActivity.EXTRA_TOTAL_AMOUNT)) {
            totalTextView.setText(intent.getStringExtra(ImageActivity.EXTRA_TOTAL_AMOUNT));
        }
        ButterKnife.bind(this);
    }

    @OnClick(R.id.save_btn)
    void onCapturedSMS() {
        Log.d("TotalActivity", "save button clicked");
        TransactionActivity.startActivity(getApplicationContext());
    }
}
