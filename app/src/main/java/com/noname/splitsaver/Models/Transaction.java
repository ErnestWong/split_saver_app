package com.noname.splitsaver.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transaction {

    private String name;
    private double totalPrice;
    private Date createDate;
    private Date purchaseDate;
    private List<Item> items;


    public Transaction(String name, double totalPrice, Date createDate, Date purchaseDate) {
        this.name = name;
        this.totalPrice = totalPrice;
        this.createDate = createDate;
        this.purchaseDate = purchaseDate;
        items = new ArrayList<Item>();
    }

    public Transaction(String name, double totalPrice, Date createDate, Date purchaseDate, List<Item> items) {
        this.name = name;
        this.totalPrice = totalPrice;
        this.createDate = createDate;
        this.purchaseDate = purchaseDate;
        this.items = items;
    }

    public List<Item> addItem(Item item) {
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
