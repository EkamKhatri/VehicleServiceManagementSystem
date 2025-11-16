package com.service.records;

import com.service.customer.Customer;
import com.service.parts.Part;
import java.io.Serializable;

public class ServiceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Customer customer;      // stores customer object
    private String vehicleModel;    // model of vehicle
    private int mileage;            // vehicle mileage
    private Part part;              // chosen service part
    private double cost;            // final calculated cost
    private String dateTime;        // readable date & time of service

    public ServiceRecord(Customer customer, String vehicleModel, int mileage, Part part, boolean underWarranty) {

        this.customer = customer;
        this.vehicleModel = vehicleModel;
        this.mileage = mileage;
        this.part = part;

        // Generate readable timestamp
        java.time.LocalDateTime dt = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter f =
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.dateTime = dt.format(f);

        // Calculate cost
        double base = part.calculateLaborCost();
        if (underWarranty) cost = 0;                    // free under warranty
        else if (customer.isPremium()) cost = base * 0.9; // 10% discount
        else cost = base;
    }

    public Customer getCustomer() { return customer; }
    public String getDateTime() { return dateTime; }

    // Format how each record is displayed
    public String toString() {
        return customer.getName() + " | " + vehicleModel + " | " +
                (customer.isPremium() ? "Premium" : "Regular") +
                " | Cost: " + cost +
                " | Date & Time: " + dateTime;
    }
}
