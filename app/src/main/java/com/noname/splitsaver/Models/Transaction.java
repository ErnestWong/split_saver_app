package com.noname.splitsaver.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Transaction implements Serializable {


    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("total")
    private float totalPrice;
    @SerializedName("createdAt")
    private Date createDate;
    @SerializedName("associatedUsers")
    private HashMap<String, Payee> payees;
    @SerializedName("lineItems")
    private List<Item> items;


    public Transaction(String name, float totalPrice) {
        this.name = name;
        this.totalPrice = totalPrice;
        items = new ArrayList<>();
    }

    public Transaction(String name, float totalPrice, List<Item> items) {
        this.name = name;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    public List<Item> addItem(Item item) {
        items.add(item);
        return items;
    }

    public String getName() {
        return name;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public List<Item> getItems() {
        return items;
    }

    public HashMap<String, Payee> getPayees() {
        return payees;
    }

    public void setPayees(HashMap<String, Payee> payees) {
        this.payees = payees;
    }

    public String getId() {
        return id;
    }
}
