package com.service.customer;

import java.io.Serializable;

public class Customer implements Serializable {

    private String name;       // customer name
    private boolean premium;   // premium or regular customer

    public Customer(String name, boolean premium) {
        this.name = name;
        this.premium = premium;
    }

    public String getName() { return name; }
    public boolean isPremium() { return premium; }
}
