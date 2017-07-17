package com.noname.splitsaver.Models;


public class Item {
    private String name;
    private double amount;
    private Payee payee;

    public Item(String name, double amount, Payee payee) {
        this.name = name;
        this.amount = amount;
        this.payee = payee;
    }

    public Item(double amount) {
        this.amount = amount;
    }

    public void setPayee(Payee payee) {
        this.payee = payee;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public Payee getPayee() { return payee; }

}
