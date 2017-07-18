package com.noname.splitsaver.Item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;

import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.R;

import java.util.ArrayList;
import java.util.List;


class ItemListViewAdapter extends BaseAdapter {

    private Context context;
    private List<AutoCompleteTextView> autoCompleteTextViewList;
    private List<Item> itemList;
    private List<Item> payeeItems;
    private AssignmentListener listener;

    ItemListViewAdapter(Context context, List<Item> itemList, List<Item> payeeItems, AssignmentListener listener) {
        this.context = context;
        this.itemList = new ArrayList<>(itemList);
        this.payeeItems = payeeItems;
        this.listener = listener;
        autoCompleteTextViewList = new ArrayList<>();
        autoCompleteTextViewList.add(new AutoCompleteTextView(context));
    }

    @Override
    public int getCount() {
        return autoCompleteTextViewList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_autocomplete_listview, viewGroup, false);
        }
        setupAutoComplete(view);
        return view;
    }

    private void setupAutoComplete(View view) {
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.item_autoTextView);
        ItemAutoCompleteAdapter adapter = new ItemAutoCompleteAdapter(context, itemList);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                payeeItems.add((Item) adapterView.getItemAtPosition(i));
                listener.updateTotal();
            }
        });
    }

    void addNewItem(){
        autoCompleteTextViewList.add(new AutoCompleteTextView(context));
        notifyDataSetChanged();
    }
}
