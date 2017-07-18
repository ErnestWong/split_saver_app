package com.noname.splitsaver.Item;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.Models.Payee;
import com.noname.splitsaver.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

class PayeeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.payee_name_textView)
    TextView nameTextView;

    @BindView(R.id.payee_item_listView)
    ListView listView;

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

    @OnClick(R.id.payee_add_item_btn)
    void onAddItemClicked() {
        listViewAdapter.addNewItem();
    }

    private void setupListView() {
        listViewAdapter = new ItemListViewAdapter(context, itemList, payee.getItems(), listener);
        listView.setAdapter(listViewAdapter);
    }

    void bindView(Payee payee) {
        this.payee = payee;
        nameTextView.setText(payee.getName());
        setupListView();
    }
}
