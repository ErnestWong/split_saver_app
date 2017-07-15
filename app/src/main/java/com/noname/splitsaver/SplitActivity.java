package com.noname.splitsaver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.Arrays;
import com.noname.splitsaver.Item.ItemAdapter;
import com.noname.splitsaver.Models.Item;


public class SplitActivity extends Activity {

    public static final String EXTRA_TOTAL = "extraTotalAmount";
    public static final String EXTRA_ITEM_AMOUNTS = "extraItemAmounts";

    @BindView(R.id.item_recycler_view)
    RecyclerView recyclerView;

    ItemAdapter itemAdapter;

    @BindView(R.id.name_editText)
    EditText nameEditText;

    @BindView(R.id.num_items_editText)
    EditText numEditText;

    @BindView(R.id.line_item_textView)
    TextView lineItemTextView;

    @BindView(R.id.total_textView)
    TextView totalTextView;

    private int numItems = 0;
    private float total = 0;
    private float[] lineItemAmounts;
    private List<Item> lineItems = new  ArrayList<Item>();
    private float lineItem = 0;

    private TextWatcher numTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence != null && charSequence.length() > 0) {
                numItems = Integer.parseInt(charSequence.toString());
                lineItem = total / numItems;
                lineItemTextView.setText(String.valueOf(lineItem));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public static void startActivity(Context context, float total, ArrayList<Float> itemAmounts) {
        Intent intent = new Intent(context, SplitActivity.class);
        intent.putExtra(EXTRA_TOTAL, total);
        intent.putExtra(EXTRA_ITEM_AMOUNTS, toPrimitive(itemAmounts));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void populateFields() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_TOTAL)) {
            total = intent.getFloatExtra(EXTRA_TOTAL, 0);
            lineItem = total;
        }
        if (intent.hasExtra(EXTRA_ITEM_AMOUNTS)) {
            lineItemAmounts = intent.getFloatArrayExtra(EXTRA_ITEM_AMOUNTS);
            for (int i = 0; i < lineItemAmounts.length; i++) {
                lineItems.add(new Item(lineItemAmounts[i]));
            }
        }
        lineItemTextView.setText(String.valueOf(total));
        totalTextView.setText(String.valueOf(lineItem));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);
        ButterKnife.bind(this);

        populateFields();

        numEditText.addTextChangedListener(numTextWatcher);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(lineItems);
        recyclerView.setAdapter(itemAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @OnClick(R.id.save_btn)
    void onCapturedSMS() {
        String name = nameEditText.getText().toString();
        List<Float> lineItems = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            lineItems.add(lineItem);
        }

    }

    private static float[] toPrimitive(ArrayList<Float> list) {
        float[] result = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }
}
