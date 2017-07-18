package com.noname.splitsaver.Transaction;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.noname.splitsaver.Models.Transaction;
import com.noname.splitsaver.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

class TransactionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.transaction_name)
    TextView name;

    @BindView(R.id.transaction_price)
    TextView totalPrice;

    @BindView(R.id.transaction_create_date)
    TextView createDate;

    private SimpleDateFormat monthDayFormat = new SimpleDateFormat("MMM d", Locale.CANADA);

    TransactionViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    void bindView(Transaction transaction) {
        name.setText(transaction.getName());
        totalPrice.setText(String.format("price $%.2f", transaction.getTotalPrice()));
//        createDate.setText(monthDayFormat.format(transaction.getCreateDate()));
    }
}
