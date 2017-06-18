package com.noname.splitsaver.Transaction;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.noname.splitsaver.Models.Transaction;
import com.noname.splitsaver.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class TransactionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.transaction_name)
    TextView name;

    @BindView(R.id.transaction_price)
    TextView totalPrice;

    TransactionViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    void bindView(Transaction transaction) {
        name.setText(transaction.getName());
        totalPrice.setText(transaction.getTotalPrice());
    }
}
