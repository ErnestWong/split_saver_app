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
import butterknife.OnClick;

class TransactionViewHolder extends RecyclerView.ViewHolder {

    Transaction transaction;

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

    @OnClick (R.id.transaction_layout)
    void itemClick(){
        Transaction t = transaction;
        //Start activity and pass in transaction
        TransactionDetailsActivity.startActivity(name.getContext(), transaction);
    }

    void bindView(Transaction transaction) {
        name.setText(transaction.getName());
        totalPrice.setText(String.format("price $%.2f", transaction.getTotalPrice()));
        this.transaction=transaction;
//        createDate.setText(monthDayFormat.format(transaction.getCreateDate()));
    }
}
