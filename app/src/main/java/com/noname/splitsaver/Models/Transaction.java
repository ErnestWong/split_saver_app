package com.noname.splitsaver.Models;

public class Transaction {

    private String name;
    private String totalPrice;

    public Transaction(String name, String totalPrice) {
        this.name = name;
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public String getTotalPrice() {
        return totalPrice;
    }
}
