package com.noname.splitsaver;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.Models.Payee;
import com.noname.splitsaver.Models.Transaction;
import com.noname.splitsaver.Network.NetworkManager;
import com.noname.splitsaver.Payee.PayeeAutoCompleteAdapter;
import com.noname.splitsaver.Payee.PayeeRecyclerViewAdapter;
import com.noname.splitsaver.Utils.AssignmentListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AssignmentActivity extends AppCompatActivity implements AssignmentListener {

    public static final String EXTRA_TRANSACTION = "extraTransaction";
    private static final String TAG = "AssignmentActivity";

    @BindView(R.id.add_payee_auto)
    AutoCompleteTextView addPayeeAuto;

    @BindView(R.id.total_textView)
    TextView totalTextView;

    @BindView(R.id.payee_item_recyclerView)
    RecyclerView payeeItemRecyclerView;
    Callback<ResponseBody> postCreateDigitalReceiptCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: 200 - " + response.body());
                    Toast.makeText(getApplicationContext(), "Successfully created digital receipt.", Toast.LENGTH_SHORT).show();
                    MainActivity.startActivity(getApplicationContext());
                    finish();
                } else {
                    Log.e(TAG, "onResponse: " + response.body());
                    Toast.makeText(getApplicationContext(), "Create Digital Receipt Failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e(TAG, "onResponse: failed - " + response.errorBody());
                Toast.makeText(getApplicationContext(), "Create Digital Receipt Failed", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            t.printStackTrace();
        }
    };
    private Transaction transaction;
    private List<Payee> payees;
    private List<Item> items;
    private PayeeRecyclerViewAdapter payeeRecyclerViewAdapter;
    private float assignedAmount;

    public static void startActivity(Context context, Transaction transaction) {
        Intent intent = new Intent(context, AssignmentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_TRANSACTION, transaction);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @OnClick(R.id.add_payee_btn)
    void onAddPayeeClicked() {
        String phoneNumber = addPayeeAuto.getText().toString();
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Payee payee = new Payee("", formatNumber(phoneNumber));
            addPayee(payee);
        } else {
            Toast.makeText(getApplicationContext(), "Not a valid number", Toast.LENGTH_SHORT).show();
        }
    }

    private void addPayee(Payee payee) {
        if (payees.contains(payee)) {
            Toast.makeText(getApplicationContext(), "The selected payee is already added", Toast.LENGTH_SHORT).show();
        } else {
            payees.add(payee);
            payeeRecyclerViewAdapter.notifyDataSetChanged();
            addPayeeAuto.setText("");
        }
    }

    @OnClick(R.id.split_even_btn)
    void onSplitEvenClicked() {
        int numPayee = payees.size();
        Log.d(TAG, "onSplitEvenClicked: " + assignedAmount);
        float splitPrice = (transaction.getTotalPrice() - assignedAmount) / numPayee;
        if (splitPrice > 0) {
            for (Payee payee : payees) {
                payee.addItem(new Item(Item.SPLIT_EVENLY, splitPrice));
            }
            updateTotal();
        } else {
            Toast.makeText(getApplicationContext(), "Assigned total is full", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.send_transaction_btn)
    void onSendTransactionBtnClicked() {
        if (verifyTransaction()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Create Digital Receipt")
                    .setMessage("Do you want to create digital receipt and send reminders to payees?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendNetworkRequest();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
    }

    void sendNetworkRequest() {
        HashMap<String, Payee> payeeMap = new HashMap<>();
        for (Payee payee : payees) {
            int payeeTotal = 0;
            for (Item item : payee.getItems()) {
                payeeTotal += item.getAmount();
            }
            payee.setTotal(payeeTotal);
            payeeMap.put(payee.getNumber(), payee);
        }
        transaction.setPayees(payeeMap);
        NetworkManager.postCreateDigitalReceipt(postCreateDigitalReceiptCallback, transaction);
    }

    private boolean verifyTransaction() {
        if (payees.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No payees", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (Payee payee : payees) {
            if (payee.getItems().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Payee is not assigned any items", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.get(EXTRA_TRANSACTION) != null) {
            transaction = (Transaction) bundle.getSerializable(EXTRA_TRANSACTION);
            if (transaction != null) {
                items = new ArrayList<>(transaction.getItems());
            } else {
                Log.d("AssignmentActivity", "onCreate: no items found");
                finish();
            }
        }
        payees = new ArrayList<>();

        setupViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViews() {
        totalTextView.setText(getString(R.string.transaction_total, 0f, transaction.getTotalPrice()));
        PayeeAutoCompleteAdapter adapter = new PayeeAutoCompleteAdapter(this, getContactList());
        addPayeeAuto.setThreshold(1);
        addPayeeAuto.setAdapter(adapter);
        addPayeeAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Payee payee = (Payee) adapterView.getItemAtPosition(i);
                addPayee(payee);
            }
        });
        addPayeeAuto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    addPayeeAuto.showDropDown();
                }
            }
        });
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        payeeItemRecyclerView.setLayoutManager(layoutManager);
        payeeRecyclerViewAdapter = new PayeeRecyclerViewAdapter(payees, items, this);
        payeeItemRecyclerView.setAdapter(payeeRecyclerViewAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(payeeItemRecyclerView.getContext(),
                layoutManager.getOrientation());
        payeeItemRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private List<Payee> getContactList() {
        List<Payee> contactList = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String formattedNumber = formatNumber(phoneNumber);
                contactList.add(new Payee(name, formattedNumber));
            }
            cursor.close();
        }
        Collections.sort(contactList, new Comparator<Payee>() {
            @Override
            public int compare(Payee a, Payee b) {
                return a.getName().compareTo(b.getName());
            }
        });
        return contactList;
    }

    private String formatNumber(String number) {
        String normalized = PhoneNumberUtils.normalizeNumber(number);
        if (normalized.startsWith("+")) {
            normalized = normalized.replaceFirst("\\+", "");
        }
        if (normalized.startsWith("1")) {
            normalized = normalized.replaceFirst("1", "");
        }
        return normalized;
    }

    @Override
    public void updateTotal() {
        Log.d(TAG, "updateTotal: " + assignedAmount);
        payeeRecyclerViewAdapter.notifyDataSetChanged();
        assignedAmount = 0;
        for (Payee payee : payees) {
            for (Item item : payee.getItems()) {
                assignedAmount += item.getAmount();
            }
        }
        totalTextView.setText(getString(R.string.transaction_total, assignedAmount, transaction.getTotalPrice()));
    }
}