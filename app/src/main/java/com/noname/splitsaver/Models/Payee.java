package com.noname.splitsaver.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Payee implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("phone_number")
    private String number;
    @SerializedName("linesItems")
    private List<Item> items;
    @SerializedName("total")
    private float total;

    public Payee(String name, String number) {
        this.name = name;
        this.number = number;
        items = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public String getNumber() {
        return this.number;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public float getTotal() {
        return this.total;
    }
}
