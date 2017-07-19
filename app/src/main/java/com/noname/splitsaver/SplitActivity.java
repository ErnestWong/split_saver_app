package com.noname.splitsaver;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.noname.splitsaver.Item.ItemRecyclerViewAdapter;
import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.Models.Transaction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SplitActivity extends AppCompatActivity {

    public static final String EXTRA_TOTAL = "extraTotalAmount";
    public static final String EXTRA_ITEM_AMOUNTS = "extraItemAmounts";

    @BindView(R.id.receipt_name_editText)
    EditText receiptNameEditText;

    @BindView(R.id.item_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.total_textView)
    TextView totalTextView;

    ItemRecyclerViewAdapter itemRecyclerViewAdapter;
    private float total;
    private List<Item> lineItems = new ArrayList<>();

    public static void startActivity(Context context, float total, ArrayList<Float> itemAmounts) {
        Intent intent = new Intent(context, SplitActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_TOTAL, total);
        intent.putExtra(EXTRA_ITEM_AMOUNTS, toPrimitive(itemAmounts));
        context.startActivity(intent);
    }

    private static float[] toPrimitive(ArrayList<Float> list) {
        float[] result = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    private void populateFields() {
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_TOTAL)) {
            total = intent.getFloatExtra(EXTRA_TOTAL, 0);
        }
        if (intent.hasExtra(EXTRA_ITEM_AMOUNTS)) {
            float[] lineItemAmounts = intent.getFloatArrayExtra(EXTRA_ITEM_AMOUNTS);
            for (float lineItemAmount : lineItemAmounts) {
                lineItems.add(new Item(lineItemAmount));
            }
            if (lineItems.isEmpty()) {
                lineItems.add(new Item());
            }
        }
        totalTextView.setText(getString(R.string.split_total, total));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        populateFields();
        setupRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(getApplicationContext(), lineItems);
        recyclerView.setAdapter(itemRecyclerViewAdapter);
    }

    @OnClick(R.id.add_item_btn)
    void onAddItemClicked() {
        lineItems.add(new Item());
        itemRecyclerViewAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.next_btn)
    void onNextClicked() {
        Transaction transaction = createTransaction();
        if (transaction != null) {
            AssignmentActivity.startActivity(getApplicationContext(), transaction);
        }
    }

    private Transaction createTransaction() {
        String receiptName = receiptNameEditText.getText().toString();

        if (receiptName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a receipt name", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (!verifyLineItems()) {
            return null;
        }

        return new Transaction(receiptName, total, itemRecyclerViewAdapter.getItemList());
    }

    private boolean verifyLineItems() {
        float amount = 0;
        for (Item item : lineItems) {
            if (item.getName() == null || item.getName().equals("")) {
                Toast.makeText(getApplicationContext(), "Please enter a name for each line item", Toast.LENGTH_SHORT).show();
                return false;
            }
            amount += item.getAmount();
        }
        if (amount < total) {
            final float remainder = total - amount;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Items amount does not add up to total")
                    .setMessage("Do you want to add an item for the leftover amount?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            lineItems.add(new Item("Other", remainder));
                            itemRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
            return false;
        } else if (amount > total) {
            Toast.makeText(getApplicationContext(), "Items amount is greater than total!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
