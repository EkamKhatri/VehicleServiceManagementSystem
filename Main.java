package com.service.main;

import com.service.manager.ServiceManager;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        ServiceManager manager = new ServiceManager();  // object that handles all operations
        manager.loadData();  // load all previously saved records

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {

            // Menu options
            System.out.println("\n1. Book Service");
            System.out.println("2. View All Records");
            System.out.println("3. Search by Customer Name");
            System.out.println("4. Save All (single file)");
            System.out.println("5. Backup All Records (separate files)");
            System.out.println("6. Exit");
            System.out.print("Choice: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());  // take menu input
            } catch (Exception e) {
                System.out.println("Invalid input.");
            }

            // Perform selected operation
            switch (choice) {
                case 1 -> manager.bookService();        // add new service record
                case 2 -> manager.showRecords();        // show all records
                case 3 -> {                              // search records
                    System.out.print("Enter customer name to search: ");
                    String name = sc.nextLine();
                    manager.searchByCustomerName(name);
                }
                case 4 -> manager.saveData();           // save data.dat file
                case 5 -> manager.saveEachCustomerToSeparateFiles(); // backup customer files
                case 6 -> running = false;              // exit loop
                default -> System.out.println("Invalid!");
            }
        }

        sc.close();
    }
}
