package com.noname.splitsaver.Transaction;

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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Daniel on 7/17/2017.
 */

public class RecipientListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "RecipientListActivity";
    String transactionId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_send_view);

        Intent i = getIntent();
        transactionId = i.getStringExtra("transactionId");

        try {
            JSONObject obj = new JSONObject(i.getStringExtra("json"));
            ArrayList<Payee> payees = getPayeesFromJSON(obj);

            ListAdapter payeeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, payees);
            ListView payeeListView = (ListView) findViewById(R.id.payeeList);

            payeeListView.setAdapter(payeeAdapter);

            payeeListView.setOnItemClickListener(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
        Payee payee = (Payee) parent.getItemAtPosition(position);
        // Send to server
        NetworkManager.postPaymentRequest(postPaymentRequest, payee.getNumber(), transactionId);
        Toast.makeText(this,"Sending reminder to "+ payee.getName() + " at " + payee.getNumber() , Toast.LENGTH_LONG).show();

    }

    private ArrayList<Payee> getPayeesFromJSON(JSONObject json) {
        ArrayList<Payee> payees = new ArrayList<>();
        // TODO parse json object here
        return payees;
    }
}
