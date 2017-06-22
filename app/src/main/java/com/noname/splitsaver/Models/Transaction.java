package com.noname.splitsaver.Models;

import java.util.Date;

public class Transaction {

    private String name;
    private String totalPrice;
    private Date createDate;
    private Date purchaseDate;

    public Transaction(String name, String totalPrice, Date createDate, Date purchaseDate) {
        this.name = name;
        this.totalPrice = totalPrice;
        this.createDate = createDate;
        this.purchaseDate = purchaseDate;
    }

    public String getName() {
        return name;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }
}
