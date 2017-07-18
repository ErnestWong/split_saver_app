package com.noname.splitsaver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.noname.splitsaver.Item.ItemRecyclerViewAdapter;
import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.Models.Transaction;
import com.noname.splitsaver.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTouch;


public class SplitActivity extends Activity {

    public static final String EXTRA_TOTAL = "extraTotalAmount";
    public static final String EXTRA_ITEM_AMOUNTS = "extraItemAmounts";

    @BindView(R.id.receipt_name_viewSwitcher)
    ViewSwitcher receiptNameViewSwitcher;

    @BindView(R.id.receipt_name_editText)
    EditText receiptNameEditText;

    @BindView(R.id.receipt_name_textView)
    TextView receiptNameTextView;

    @BindView(R.id.scroll_view)
    ScrollView scrollView;

    @BindView(R.id.item_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.total_textView)
    TextView totalTextView;

    @BindView(R.id.next_btn)
    Button nextButton;

    ItemRecyclerViewAdapter itemRecyclerViewAdapter;
    private float total = 0;
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
        totalTextView.setText(Utils.displayPrice(getApplicationContext(), total));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);
        ButterKnife.bind(this);

        populateFields();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(getApplicationContext(), lineItems);
        recyclerView.setAdapter(itemRecyclerViewAdapter);
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
        if (receiptNameViewSwitcher.getCurrentView().equals(receiptNameTextView)) {
            receiptNameViewSwitcher.showNext();
            receiptNameEditText.setEnabled(true);
        }
        return true;
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
        String transactionName = receiptNameEditText.getText().toString().trim();

        if (transactionName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a receipt name", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (itemListNameEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a name for each line item", Toast.LENGTH_SHORT).show();
            return null;
        }

        return new Transaction(transactionName, total, itemRecyclerViewAdapter.getItemList());
    }

    private boolean itemListNameEmpty() {
        for (Item i : itemRecyclerViewAdapter.getItemList()) {
            if (i.getName() == null || i.getName().equals("")) {
                return true;
            }
        }
        return false;
    }
}
