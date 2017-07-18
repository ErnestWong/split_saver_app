package com.noname.splitsaver.Item;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noname.splitsaver.Models.Item;
import com.noname.splitsaver.R;

import java.util.List;

import static com.noname.splitsaver.Models.Item.TYPE_ITEM_EMPTY;
import static com.noname.splitsaver.Models.Item.TYPE_ITEM_NAME;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Item> itemAmountList;

    public ItemRecyclerViewAdapter(Context context, List<Item> itemAmountList) {
        this.context = context;
        this.itemAmountList = itemAmountList;
    }

    @Override
    public int getItemViewType(int position) {
        return itemAmountList.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_ITEM_NAME:
                view = inflater.inflate(R.layout.view_item, parent, false);
                viewHolder = new ItemNameViewHolder(view, context);
                break;
            case TYPE_ITEM_EMPTY:
                view = inflater.inflate(R.layout.view_item_edit, parent, false);
                viewHolder = new ItemEmptyViewHolder(view, context);
                break;
            default:
                view = inflater.inflate(R.layout.view_item_edit, parent, false);
                viewHolder = new ItemEmptyViewHolder(view, context);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_ITEM_NAME:
                ((ItemNameViewHolder) holder).bindView(itemAmountList.get(position));
                break;
            case TYPE_ITEM_EMPTY:
                ((ItemEmptyViewHolder) holder).bindView(itemAmountList.get(position));
                break;
            default:
                ((ItemViewHolder) holder).bindView(itemAmountList.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return itemAmountList.size();
    }

    public List<Item> getItemList() {
        return itemAmountList;
    }
}
