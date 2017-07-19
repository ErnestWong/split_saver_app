package com.noname.splitsaver.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Item implements Serializable {
    public static final String SPLIT_EVENLY = "Split Evenly";
    public static final String CUSTOM_ITEM = "Custom Item";

    @SerializedName("name")
    private String name;
    @SerializedName("value")
    private float amount;

    public Item() {
    }

    public Item(float amount) {
        this.amount = amount;
    }

    public Item(String name, float amount) {
        this.name = name;
        this.amount = amount;
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
}
