package com.noname.splitsaver.Models;

public class Payee {
    private String name;
    private String number;

    public Payee(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return this.name;
    }

    public String getNumber() {
        return this.number;
    }
}
