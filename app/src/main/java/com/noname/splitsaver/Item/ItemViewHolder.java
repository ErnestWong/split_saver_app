package com.noname.splitsaver.Item;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.Models.Transaction;
import com.noname.splitsaver.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

class ItemViewHolder extends RecyclerView.ViewHolder {

//    @BindView(R.id.amount_textView)
//    TextView amountTextView;

    private Item item;

    @BindView(R.id.item_name_editText)
    EditText nameEditText;

    @BindView(R.id.item_name_textView)
    TextView nameTextView;

    @BindView(R.id.item_name_viewSwitcher)
    ViewSwitcher nameViewSwitcher;

    ItemViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        nameEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        nameEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Log.d("ItemViewHolder", "in constructor");
    }

    void bindView(Item item) {
        this.item = item;
//        amountTextView.setText(Double.toString(item.getAmount()));
//        nameEditText.addTextChangedListener(numTextWatcher);
        Log.d("ItemViewHolder", "in bindView");
    }

    @OnEditorAction(R.id.item_name_editText)
    boolean onNameTextChanged(int actionId, KeyEvent event) {
        Log.d("itemViewHandler", "action: " + String.valueOf(actionId));
        String itemName = nameEditText.getText().toString().trim();
        if (actionId == EditorInfo.IME_ACTION_DONE && itemName.length() > 0) {
            Log.d("ItemViewHandler", "name: " + itemName);
            nameEditText.setEnabled(false);
            if (nameViewSwitcher.getCurrentView().equals(nameEditText)) {
                nameTextView.setText(itemName);
                nameViewSwitcher.showNext();
            }
            item.setName(itemName);
            // set item name to whatever is in the edittext
        }
        return true;
    }

    @OnTouch(R.id.item_name_textView)
    boolean onTouchTextView() {
        Log.d("itemViewHOlder", "touched textview");
        if (nameViewSwitcher.getCurrentView().equals(nameTextView)) {
            nameViewSwitcher.showNext();
            nameEditText.setEnabled(true);
        }
        return true;
    }
}
