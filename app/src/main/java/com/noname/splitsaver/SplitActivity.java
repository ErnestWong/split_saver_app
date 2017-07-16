package com.noname.splitsaver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTouch;

import com.noname.splitsaver.Item.ItemAdapter;
import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.Models.Transaction;


public class SplitActivity extends Activity {

    public static final String EXTRA_TOTAL = "extraTotalAmount";
    public static final String EXTRA_ITEM_AMOUNTS = "extraItemAmounts";

    @BindView(R.id.item_recycler_view)
    RecyclerView recyclerView;

    ItemAdapter itemAdapter;

    @BindView(R.id.num_items_editText)
    EditText numEditText;

    @BindView(R.id.line_item_textView)
    TextView lineItemTextView;

    @BindView(R.id.total_textView)
    TextView totalTextView;

    @BindView(R.id.receipt_name_viewSwitcher)
    ViewSwitcher receiptNameViewSwitcher;

    @BindView(R.id.receipt_name_editText)
    EditText receiptNameEditText;

    @BindView(R.id.receipt_name_textView)
    TextView receiptNameTextView;

    @BindView(R.id.item_split_total_layout)
    LinearLayout itemSplitTotalLayout;

    @BindView(R.id.even_split_total_layout)
    LinearLayout evenSplitTotalLayout;

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

        if (lineItems.size() == 0) {
            itemSplitTotalLayout.setVisibility(View.GONE);
        } else {
            evenSplitTotalLayout.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);
        ButterKnife.bind(this);

        populateFields();

        receiptNameEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        receiptNameEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
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

    @OnEditorAction(R.id.receipt_name_editText)
    boolean onReceiptNameTextChanged(int actionId, KeyEvent event) {
        Log.d("SplitActivity", "action: " + String.valueOf(actionId));
        String receiptName = receiptNameEditText.getText().toString();
        if (actionId == EditorInfo.IME_ACTION_DONE && receiptName.length() > 0) {
            Log.d("SplitActivity", "receipt name: " + receiptName);
            receiptNameEditText.setEnabled(false);
            if (receiptNameViewSwitcher.getCurrentView().equals(receiptNameEditText)) {
                receiptNameTextView.setText(receiptName);
                receiptNameViewSwitcher.showNext();
            }
            // set item name to whatever is in the edittext
        }
        return true;
    }

    @OnTouch(R.id.receipt_name_textView)
    boolean onTouchTextView() {
        Log.d("SplitActivity", "touched receipt textview");
        if (receiptNameViewSwitcher.getCurrentView().equals(receiptNameTextView)) {
            receiptNameViewSwitcher.showNext();
            receiptNameEditText.setEnabled(true);
        }
        return true;
    }

    @OnClick(R.id.save_btn)
    void onCapturedSMS() {
        Log.d("splitactivity", "pressed save button");
        Transaction transaction = createTransaction();
    }

    private Transaction createTransaction() {
        String transactionName = receiptNameEditText.getText().toString().trim();
        String transactionTotalString = numEditText.getText().toString().trim();
        Log.d("SplitActivity", "receipt name: " + transactionName);
        Log.d("SplitActivity", "receipt itotal: " + transactionTotalString);

        if (transactionName.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a receipt name", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (transactionTotalString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a receipt total", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (itemListNameEmpty()) {
             Toast.makeText(getApplicationContext(), "Please enter a name for each line item", Toast.LENGTH_SHORT).show();
             return null;
        }
        double  transactionTotal = Double.parseDouble(transactionTotalString);
        Date current = new Date();
        return new Transaction(transactionName, transactionTotal, current, current, itemAdapter.getItemList());
    }

    private boolean itemListNameEmpty() {
        for (Item i : itemAdapter.getItemList()) {
            if (i.getName() == null || i.getName().equals("")) {
                return true;
            }
        }
        return false;
    }

    private static float[] toPrimitive(ArrayList<Float> list) {
        float[] result = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }
}
