package com.noname.splitsaver.Item;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.R;
import com.noname.splitsaver.Utils.DecimalDigitsInputFilter;
import com.noname.splitsaver.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

class ItemNameViewHolder extends ItemViewHolder {

    @BindView(R.id.amount_textView)
    TextView amountTextView;

    ItemNameViewHolder(View view, Context context) {
        super(view, context);
    }

    @Override
    void bindView(Item item) {
        super.bindView(item);
        amountTextView.setText(Utils.displayPrice(context, item.getAmount()));
    }
}

class ItemEmptyViewHolder extends ItemViewHolder {

    @BindView(R.id.amount_editText)
    EditText amountEditText;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String amountString = charSequence.toString();
            if (!amountString.isEmpty()) {
                float amount = Float.valueOf(amountString);
                item.setAmount(amount);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    ItemEmptyViewHolder(View view, Context context) {
        super(view, context);
    }

    @Override
    void bindView(final Item item) {
        super.bindView(item);
        amountEditText.addTextChangedListener(textWatcher);
        amountEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_name_editText)
    EditText nameEditText;

    Context context;
    Item item;

    ItemViewHolder(View view, Context context) {
        super(view);
        ButterKnife.bind(this, view);
        this.context = context;
    }

    void bindView(Item item) {
        this.item = item;
    }

    @OnTextChanged(R.id.item_name_editText)
    void onTextChanged(CharSequence text) {
        String itemName = text.toString().trim();
        if (!itemName.isEmpty()) {
            item.setName(itemName);
        }
    }

}
