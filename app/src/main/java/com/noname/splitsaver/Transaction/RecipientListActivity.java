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
import com.noname.splitsaver.Network.NetworkManager;
import com.noname.splitsaver.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Daniel on 7/17/2017.
 */

public class RecipientListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "RecipientListActivity";
    private static String transactionId = "";
    private static JSONObject transactionJson;

    public static void startActivity(Context context, String id, String json) {
        transactionId = id;
        try {
            transactionJson = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(context, RecipientListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_send_view);

        ArrayList<String> payees = getPayeesFromJSON(transactionJson);

        ListAdapter payeeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, payees);
        ListView payeeListView = (ListView) findViewById(R.id.payeeList);

        payeeListView.setAdapter(payeeAdapter);

        payeeListView.setOnItemClickListener(this);
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
        String payee = (String) parent.getItemAtPosition(position);
        // Send to server
        NetworkManager.postPaymentRequest(postPaymentRequest, payee, transactionId);
        Toast.makeText(this,"Sending reminder to " + payee , Toast.LENGTH_LONG).show();

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
