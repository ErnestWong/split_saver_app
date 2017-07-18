package com.noname.splitsaver.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Item implements Serializable {
    public static final int TYPE_ITEM_EVEN = 0;
    public static final int TYPE_ITEM_NAME = 1;
    public static final int TYPE_ITEM_EMPTY = 2;
    public static final String SPLIT_EVENLY = "Split Evenly";
    public static final String CUSTOM_ITEM = "Custom Item";

    @SerializedName("name")
    private String name;
    @SerializedName("value")
    private float amount;
    private int type;

    public Item() {
        type = TYPE_ITEM_EMPTY;
    }

    public Item(float amount) {
        this.amount = amount;
        type = TYPE_ITEM_NAME;
    }

    public Item(String name, float amount) {
        this.name = name;
        this.amount = amount;
        type = TYPE_ITEM_EVEN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Payee getPayee() { return payee; }

    public int getType() {
        return type;
    }
}
