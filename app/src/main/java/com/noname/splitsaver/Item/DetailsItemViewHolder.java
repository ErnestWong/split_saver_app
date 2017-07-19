package com.noname.splitsaver.Item;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.R;
import com.noname.splitsaver.Utils.DecimalDigitsInputFilter;
import com.noname.splitsaver.Utils.SplitListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

class DetailsItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_name_Text)
    TextView nameText;

    @BindView(R.id.amount_Text)
    TextView amountText;

    private Context context;

    DetailsItemViewHolder(View view, Context context) {
        super(view);
        ButterKnife.bind(this, view);
        this.context = context;

    }

    void bindView(Item item) {
        String name = item.getName();
        if (name != null && !name.isEmpty()) {
            nameText.setText(name);
        }
        amountText.setText(context.getString(R.string.format_price_no_tag, item.getAmount()));
    }
}
