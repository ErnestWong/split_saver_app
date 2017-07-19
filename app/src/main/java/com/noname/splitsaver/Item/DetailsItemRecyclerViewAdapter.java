package com.noname.splitsaver.Item;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.R;
import com.noname.splitsaver.Utils.SplitListener;

import java.util.List;

public class DetailsItemRecyclerViewAdapter extends RecyclerView.Adapter implements SplitListener{

    private Context context;
    private List<Item> itemList;

    public DetailsItemRecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.detail_view_item, parent, false);
        return new DetailsItemViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DetailsItemViewHolder) holder).bindView(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public List<Item> getItemList() {
        return itemList;
    }

    @Override
    public void removeItem(Item item) {
        itemList.remove(item);
        notifyDataSetChanged();
    }
}
