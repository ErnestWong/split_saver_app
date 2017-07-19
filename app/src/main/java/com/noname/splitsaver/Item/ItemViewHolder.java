package com.noname.splitsaver.Item;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.R;
import com.noname.splitsaver.Utils.DecimalDigitsInputFilter;
import com.noname.splitsaver.Utils.SplitListener;
import com.noname.splitsaver.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

class ItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_name_editText)
    EditText nameEditText;

    @BindView(R.id.amount_editText)
    EditText amountEditText;

    private Context context;
    private Item item;
    private SplitListener listener;

    ItemViewHolder(View view, Context context, SplitListener listener) {
        super(view);
        ButterKnife.bind(this, view);
        this.context = context;
        this.listener = listener;

        amountEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
    }

    void bindView(Item item) {
        this.item = item;
        String name = item.getName();
        if (name != null && !name.isEmpty()) {
            nameEditText.setText(name);
        }
        amountEditText.setText(Utils.displayPrice(item.getAmount()));
    }

    @OnTextChanged(R.id.item_name_editText)
    void onNameTextChanged(CharSequence charSequence) {
        String itemName = charSequence.toString().trim();
        if (!itemName.isEmpty()) {
            item.setName(itemName);
        }
    }

    @OnTextChanged(R.id.amount_editText)
    void onAmountTextChanged(CharSequence charSequence) {
        String amountString = charSequence.toString();
        if (!amountString.isEmpty()) {
            float amount = Float.valueOf(amountString);
            item.setAmount(amount);
        }
    }

    @OnClick(R.id.remove_item_btn)
    void onRemoveClicked() {
        listener.removeItem(item);
    }
}
