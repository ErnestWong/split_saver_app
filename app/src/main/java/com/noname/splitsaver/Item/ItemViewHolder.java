package com.noname.splitsaver.Item;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.Models.Transaction;
import com.noname.splitsaver.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;

class ItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.amount_textView)
    TextView amountTextView;

    @BindView(R.id.name_editText)
    TextView nameEditText;

    ItemViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    void bindView(Item item) {
        amountTextView.setText(Double.toString(item.getAmount()));
    }

    @OnEditorAction(R.id.name_editText)
    boolean onNameEditorAction(int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
            String name = nameEditText.getText().toString();
            Log.d("ItemViewHandler", "name: " + name);
            // set item name to whatever is in the edittext
        }
        return true;
    }
}
