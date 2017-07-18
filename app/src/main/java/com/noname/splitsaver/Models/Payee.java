package com.noname.splitsaver.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Payee implements Serializable {
    private String name;
    private String number;
    @SerializedName("linesItems")
    private List<Item> items;

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
        float total = 0;
        for (Item i : this.items) {
            total += i.getAmount();
        }
        return total;
    }
}
