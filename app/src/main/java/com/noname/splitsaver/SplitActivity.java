package com.noname.splitsaver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SplitActivity extends Activity {

    public static final String EXTRA_TOTAL = "extraTotalAmount";

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

    public static void startActivity(Context context, float total) {
        Intent intent = new Intent(context, SplitActivity.class);
        intent.putExtra(EXTRA_TOTAL, total);
        context.startActivity(intent);
    }

    private void populateFields() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_TOTAL)) {
            total = intent.getFloatExtra(EXTRA_TOTAL, 0);
            lineItem = total;
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
    }

    @OnClick(R.id.save_btn)
    void onCapturedSMS() {
        String name = nameEditText.getText().toString();
        List<Float> lineItems = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            lineItems.add(lineItem);
        }

    }
}
