package com.noname.splitsaver.Models;

import java.util.ArrayList;
import java.util.Date;

public class Transaction {

    private String name;
    private double totalPrice;
    private Date createDate;
    private Date purchaseDate;
    private ArrayList<Item> items;


    public Transaction(String name, double totalPrice, Date createDate, Date purchaseDate) {
        this.name = name;
        this.totalPrice = totalPrice;
        this.createDate = createDate;
        this.purchaseDate = purchaseDate;
        items = new ArrayList<Item>();
    }

    public ArrayList<Item> addItem(Item item) {
        items.add(item);
        return items;
    }

    public String getName() {
        return name;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }
}
