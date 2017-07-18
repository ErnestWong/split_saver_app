package com.noname.splitsaver.Item;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.R;
import com.noname.splitsaver.Utils.Utils;

import java.util.List;


public class ItemListViewAdapter extends ArrayAdapter<Item> {

    public ItemListViewAdapter(Context context, List<Item> itemList) {
        super(context, android.R.layout.simple_list_item_1, itemList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_listview_item, parent, false);
        }
        setupViews(convertView, position);
        return convertView;
    }

    private void setupViews(View view, int position) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name_textView);
        TextView amountTextView = (TextView) view.findViewById(R.id.amount_textView);
        Item item = getItem(position);
        if (item != null) {
            nameTextView.setText(item.getName());
            amountTextView.setText(Utils.displayPrice(getContext(), item.getAmount()));
        }
    }
}
