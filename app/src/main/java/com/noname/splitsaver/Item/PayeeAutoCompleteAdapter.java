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

import com.noname.splitsaver.Models.Payee;
import com.noname.splitsaver.R;

import java.util.ArrayList;
import java.util.List;


public class PayeeAutoCompleteAdapter extends ArrayAdapter<Payee> {

    private Context context;
    private List<Payee> payees;
    private List<Payee> suggestions;
    private Filter payeeFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Payee payee = (Payee) resultValue;
            return payee.getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                for (Payee payee : payees) {
                    if (payee.getName().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        suggestions.add(payee);
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
                List<Payee> results = (List<Payee>) filterResults.values;
                for (Payee payee : results) {
                    add(payee);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };

    public PayeeAutoCompleteAdapter(@NonNull Context context, @NonNull List<Payee> payees) {
        super(context, android.R.layout.simple_list_item_1, payees);
        this.context = context;
        this.payees = new ArrayList<>(payees);
        this.suggestions = new ArrayList<>(payees);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.payee_autocomplete_view, parent, false);
        }
        setupViews(convertView, position);
        return convertView;
    }

    private void setupViews(View view, int position) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name_textView);
        TextView phoneNumberTextView = (TextView) view.findViewById(R.id.phone_number_textView);
        Payee payee = getItem(position);
        if (payee != null) {
            nameTextView.setText(payee.getName());
            phoneNumberTextView.setText(payee.getNumber());
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return payeeFilter;
    }
}
