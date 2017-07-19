package com.noname.splitsaver.Payee;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.noname.splitsaver.Item.ItemAutoCompleteAdapter;
import com.noname.splitsaver.Item.ItemListViewAdapter;
import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.Models.Payee;
import com.noname.splitsaver.R;
import com.noname.splitsaver.Utils.AssignmentListener;
import com.noname.splitsaver.Utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

class PayeeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.payee_name_textView)
    TextView nameTextView;

    @BindView(R.id.payee_item_listView)
    ListView listView;

    @BindView(R.id.item_autoTextView)
    AutoCompleteTextView autoCompleteTextView;

    private Context context;
    private Payee payee;
    private List<Item> itemList;
    private ItemListViewAdapter listViewAdapter;
    private AssignmentListener listener;

    PayeeViewHolder(Context context, View view, List<Item> itemList, AssignmentListener listener) {
        super(view);
        ButterKnife.bind(this, view);
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
    }

//    @OnClick(R.id.payee_add_item_btn)
//    void onAddItemClicked() {
//        autoCompleteTextView.getText().toString();
//        listViewAdapter.addNewItem();
//        listener.updateTotal();
//        autoCompleteTextView.clearListSelection();
//    }

    void bindView(Payee payee) {
        this.payee = payee;
        nameTextView.setText(payee.getName());
        setupListView();
        setupAutoComplete();
    }

    private void setupListView() {
        listViewAdapter = new ItemListViewAdapter(context, payee.getItems());
        listView.setAdapter(listViewAdapter);
        Utils.setListViewHeightBasedOnChildren(listView);
    }

    private void setupAutoComplete() {
        final ItemAutoCompleteAdapter adapter = new ItemAutoCompleteAdapter(context, itemList);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item item = (Item) adapterView.getItemAtPosition(i);
                itemList.remove(item);
                payee.addItem(item);
                autoCompleteTextView.setText("");
                listener.updateTotal();
            }
        });
        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    autoCompleteTextView.showDropDown();
                }
            }
        });
    }
}