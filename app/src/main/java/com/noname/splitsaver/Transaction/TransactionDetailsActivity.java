package com.noname.splitsaver.Transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.noname.splitsaver.Item.ItemRecyclerViewAdapter;
import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.Models.Transaction;
import com.noname.splitsaver.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Daniel on 7/17/2017.
 */

public class TransactionDetailsActivity extends AppCompatActivity {

    private static final String TAG = "TransDetailsActivity";
    private static Transaction transaction;
    private static Context context;

    @BindView(R.id.details_item_view)
    RecyclerView recyclerView;

    private List<Item> lineItems = new ArrayList<>();

    ItemRecyclerViewAdapter itemRecyclerViewAdapter;

    public static void startActivity(Context c, Transaction t) {
        transaction = t;
        context = c;
        Intent intent = new Intent(context, TransactionDetailsActivity.class);
        context.startActivity(intent);
    }

    private void setupRecyclerView() {
        lineItems = transaction.getItems();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(getApplicationContext(), lineItems);
        recyclerView.setAdapter(itemRecyclerViewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_details_view);

        //Append to name
        TextView name = (TextView) findViewById(R.id.TransactionName);
        String nameText = "Name: " + transaction.getName();
        name.setText(nameText);

        //setupRecyclerView();

        // Append to total
        TextView total =(TextView)findViewById(R.id.TransactionTotal);
        String totalText = "Total: " + transaction.getTotalPrice();
        total.setText(totalText);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.send_reminder_btn)
    void onSendReminderClicked() {
        Log.d(TAG, "send reminder  button clicked");
        RecipientListActivity.startActivity(context, transaction);
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
