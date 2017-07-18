package com.noname.splitsaver.Transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.noname.splitsaver.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Daniel on 7/17/2017.
 */

public class TransactionDetailsActivity extends AppCompatActivity {

    private static final String TAG = "TransDetailsActivity";
    private static String transactionId;
    private static String transactionJson;

    public static void startActivity(Context context, String id, String json) {
        transactionId = id;
        transactionJson = json;
        Intent intent = new Intent(context, TransactionDetailsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_details_view);

        //Append to name
        TextView name = (TextView) findViewById(R.id.TransactionName);
        String nameText = "Name: " + getFieldFromJson(transactionJson, "name");
        name.setText(nameText);

        // Append to total
        TextView total =(TextView)findViewById(R.id.TransactionTotal);
        String totalText = "Total: " + getFieldFromJson(transactionJson, "total");
        total.setText(totalText);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.send_reminder_btn)
    void onSendReminderClicked() {
        Log.d(TAG, "send reminder  button clicked");
        RecipientListActivity.startActivity(getApplicationContext(), transactionId, transactionJson);
    }

    private String getFieldFromJson(String json, String fieldName) {
        String result = "";
        try {
            JSONObject transactionObj = new JSONObject(json);
            result = transactionObj.getString(fieldName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
