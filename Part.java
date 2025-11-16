package com.service.parts;

import java.io.Serializable;

public class Part implements Serializable {

    protected String name;      // name of the service part
    protected double baseCost;  // base cost before labor charges

    public Part(String name, double baseCost) {
        this.name = name;
        this.baseCost = baseCost;
    }

    // Base labor cost (overridden in subclasses)
    public double calculateLaborCost() {
        return baseCost;
    }
}
