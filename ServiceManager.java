package com.service.manager;

import com.service.records.ServiceRecord;
import com.service.customer.Customer;
import com.service.parts.*;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServiceManager {

    private List<ServiceRecord> records = new ArrayList<>(); // stores all service records in memory
    private static final String ALL_DATA_FILE = "data.dat";  // main file for full backup
    private static final String CUSTOMER_DIR = "customers";  // folder for per-customer files

    public ServiceManager() {
        try {
            Files.createDirectories(Paths.get(CUSTOMER_DIR)); // create folder if missing
        } catch (IOException e) {
            System.out.println("Warning: Could not create customers folder.");
        }
    }

    // Load all previous records from data.dat
    public void loadData() {
        File f = new File(ALL_DATA_FILE);
        if (!f.exists()) {
            System.out.println("No previous record found. Starting fresh.");
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            Object obj = in.readObject();
            if (obj instanceof ArrayList) {
                records = (ArrayList<ServiceRecord>) obj;   // load into memory
            }
            System.out.println("Previous records loaded.");
        } catch (Exception e) {
            System.out.println("Error loading previous data.");
        }
    }

    // Add a new service record
    public void bookService() {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.print("Customer name: ");
            String name = sc.nextLine().trim();

            System.out.print("Vehicle model: ");
            String model = sc.nextLine().trim();

            System.out.print("Mileage: ");
            int mileage = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Is customer premium? (y/n): ");
            boolean isPremium = sc.nextLine().trim().equalsIgnoreCase("y");

            System.out.print("Is vehicle under warranty? (y/n): ");
            boolean isUnderWarranty = sc.nextLine().trim().equalsIgnoreCase("y");

            Customer c = new Customer(name, isPremium); // create customer object

            // Choose part type
            System.out.println("\nChoose Service Type:");
            System.out.println("1. Engine Service");
            System.out.println("2. Body Repair");
            System.out.print("Choice: ");
            int partChoice = Integer.parseInt(sc.nextLine().trim());

            Part p = (partChoice == 1) ?
                    new EnginePart("Engine Service", 5000) :
                    new BodyPart("Body Repair", 3000);

            // Create service record object
            ServiceRecord r = new ServiceRecord(c, model, mileage, p, isUnderWarranty);

            records.add(r);                 // add to memory list
            saveSingleCustomerRecord(r);    // append to customer file

            System.out.println("Service booked successfully!");
            System.out.println("Record added to " + name + ".dat");
            System.out.println(r);          // print record

        } catch (Exception e) {
            System.out.println("Invalid input!");
        }
    }

    // Display all stored records
    public void showRecords() {
        System.out.println("\n--- Service Records (" + records.size() + ") ---");
        for (ServiceRecord r : records) {
            System.out.println(r);
        }
    }

    // Search using partial or full customer name
    public void searchByCustomerName(String query) {
        String q = query.toLowerCase().trim();
        List<ServiceRecord> found = new ArrayList<>();

        for (ServiceRecord r : records) {
            if (r.getCustomer().getName().toLowerCase().contains(q)) {
                found.add(r);
            }
        }

        if (found.isEmpty()) {
            System.out.println("No match for: " + query);
        } else {
            System.out.println("Found " + found.size() + " record(s):");
            for (ServiceRecord r : found) System.out.println(r);
        }
    }

    // Save all records to data.dat
    public void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ALL_DATA_FILE))) {
            out.writeObject(new ArrayList<>(records));   // write whole list
            System.out.println("All records saved to " + ALL_DATA_FILE);
        } catch (Exception e) {
            System.out.println("Error saving data.");
        }
    }

    // Loop through all records and regenerate all customer files
    public void saveEachCustomerToSeparateFiles() {
        for (ServiceRecord r : records) {
            try {
                saveSingleCustomerRecord(r); // append for each customer
            } catch (Exception ignored) {}
        }
        System.out.println("Backup completed.");
    }

    // Append customer's new record to their .dat file
    private void saveSingleCustomerRecord(ServiceRecord r) throws IOException {

        // Clean filename
        String safeName = r.getCustomer().getName().replaceAll("[^a-zA-Z0-9_-]", "_");

        String filename = CUSTOMER_DIR + "/" + safeName + ".dat"; // one file per customer

        List<ServiceRecord> list = new ArrayList<>();

        File f = new File(filename);

        // If customer file exists, load past history
        if (f.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
                Object obj = in.readObject();
                if (obj instanceof List) {
                    list = (List<ServiceRecord>) obj;  // load previous service history
                }
            } catch (Exception e) {
                System.out.println("Could not read existing file for " + safeName);
            }
        }

        list.add(r);  // append new record

        // Save updated list back to file
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(list);
        }
    }
}
