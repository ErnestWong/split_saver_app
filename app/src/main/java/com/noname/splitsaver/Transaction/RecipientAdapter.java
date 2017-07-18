package com.noname.splitsaver.Transaction;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import com.noname.splitsaver.Models.Payee;

import java.util.ArrayList;
import java.util.List;

class RecipientAdapter extends ArrayAdapter<Payee> {
    private Context context;
    private ArrayList<Payee> payees;
    private static LayoutInflater inflater = null;

    public RecipientAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Payee> objects) {
        super(context, resource, objects);
        this.payees = objects;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return payees.size();
    }
}
