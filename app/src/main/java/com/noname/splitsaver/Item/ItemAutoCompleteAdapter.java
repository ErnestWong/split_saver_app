package com.noname.splitsaver.Item;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.R;
import com.noname.splitsaver.Utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class ItemAutoCompleteAdapter extends ArrayAdapter<Item> {

    private List<Item> items;
    private List<Item> suggestions;
    private Filter itemFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Item item = (Item) resultValue;
            return item.getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                for (Item item : items) {
                    if (item.getName().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        suggestions.add(item);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults != null && filterResults.count > 0) {
                clear();
                List<Item> results = (List<Item>) filterResults.values;
                for (Item item : results) {
                    add(item);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };

    public ItemAutoCompleteAdapter(@NonNull Context context, @NonNull List<Item> items) {
        super(context, android.R.layout.simple_list_item_1, new ArrayList<>(items));
        this.items = new ArrayList<>(items);
        this.suggestions = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_autocomplete_item, parent, false);
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

    @NonNull
    @Override
    public Filter getFilter() {
        return itemFilter;
    }
}
