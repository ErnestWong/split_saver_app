package com.noname.splitsaver.Transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.noname.splitsaver.Login.PinActivity;
import com.noname.splitsaver.MainActivity;
import com.noname.splitsaver.MainApplication;
import com.noname.splitsaver.Models.Payee;
import com.noname.splitsaver.Models.Transaction;
import com.noname.splitsaver.Network.NetworkManager;
import com.noname.splitsaver.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Daniel on 7/17/2017.
 */

public class RecipientListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "RecipientListActivity";
    private static Transaction transaction;
    private static String transactionId = "";

    public static void startActivity(Context context, Transaction t) {
        transaction = t;
        transactionId = t.getId();
        Intent intent = new Intent(context, RecipientListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_send_view);

        ArrayList<String> payees = convertToString(transaction.getPayees());

        ListAdapter payeeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, payees);
        ListView payeeListView = (ListView) findViewById(R.id.payeeList);

        payeeListView.setAdapter(payeeAdapter);

        payeeListView.setOnItemClickListener(this);
        ButterKnife.bind(this);
    }

    Callback<ResponseBody> postPaymentRequest = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                Log.d(TAG, "onResponse: "+ response.code()+" - " + response.body());
            } else {
                Log.e(TAG, "onResponse: failed - " + response.errorBody());
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            t.printStackTrace();
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String p = (String) parent.getItemAtPosition(position);
        String number = p.split(",")[0];
        // Send to server
        Log.d(TAG, "Sending post reminder with: payee: "+ number +", transactionId: " + transactionId);
        NetworkManager.postPaymentRequest(postPaymentRequest, number, transactionId);
        Toast.makeText(this,"Sending reminder to " + number , Toast.LENGTH_LONG).show();

    }

    @OnClick(R.id.send_all_btn)
    void onSendReminderClicked() {
        Log.d(TAG, "send all reminder  button clicked");
        NetworkManager.postBulkPaymentRequest(postPaymentRequest, transactionId);
        Toast.makeText(this,"Sending to all for transaction id: " + transactionId , Toast.LENGTH_LONG).show();
    }

    private ArrayList<String> convertToString(HashMap<String, Payee> payees) {
        ArrayList<String> strings = new ArrayList<>();
        for (String key :  payees.keySet()) {
            Payee p = payees.get(key);
            strings.add(key + ", $" + p.getTotal());
        }
        return strings;
    }

    private ArrayList<String> getPayeesFromJSON(JSONObject json) {
        ArrayList<String> payees = new ArrayList<>();
        try {
            JSONObject users = json.getJSONObject("associatedUsers");
            Iterator<?> keys = users.keys();

            while(keys.hasNext()) {
                String key = (String)keys.next();
                if (users.get(key) instanceof JSONObject) {
                    payees.add(key); // name not received in the server response
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payees;
    }
}
