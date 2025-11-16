package com.service.parts;

public class BodyPart extends Part {

    public BodyPart(String name, double baseCost) {
        super(name, baseCost);
    }

    // Body repair adds ₹500 labor
    @Override
    public double calculateLaborCost() {
        return baseCost + 500;
    }
}
