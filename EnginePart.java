package com.service.parts;

public class EnginePart extends Part {

    public EnginePart(String name, double baseCost) {
        super(name, baseCost);
    }

    // Engine service adds extra ₹1000 labor
    @Override
    public double calculateLaborCost() {
        return baseCost + 1000;
    }
}
