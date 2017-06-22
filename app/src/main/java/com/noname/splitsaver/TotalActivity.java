package com.noname.splitsaver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.ButterKnife;


public class TotalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);

        final TextView totalTextView = (TextView)findViewById(R.id.amount_textview);
        Intent intent = getIntent();
        if (intent.hasExtra(ImageActivity.EXTRA_TOTAL_AMOUNT)) {
            totalTextView.setText(intent.getStringExtra(ImageActivity.EXTRA_TOTAL_AMOUNT));
        }
        ButterKnife.bind(this);
    }
}
