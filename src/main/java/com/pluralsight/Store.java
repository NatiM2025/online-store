package com.pluralsight;

import javax.sound.midi.Soundbank;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.SortedMap;

/**
 * Starter code for the Online Store workshop.
 * Learners will complete the TODO sections to make the program work.
 * <p>
 * What is already done for you: the two lists, the Home Screen loop and
 * the method headers. Each TODO says what that method has to do.
 * Product.java is complete; products.csv sits at the project root.
 * You will need extra imports (for example java.io.BufferedReader,
 * java.io.FileReader, java.io.IOException) as you fill the TODOs in.
 */
public class Store {

    public static void main(String[] args) {

        // Create lists for inventory and the shopping cart
        ArrayList<Product> inventory = new ArrayList<>();
        ArrayList<Product> cart = new ArrayList<>();

        // Load inventory from the data file (pipe-delimited: SKU|Product Name|Price|Department)
        loadInventory("products.csv", inventory);
        // Main menu loop
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice != 3) {
            System.out.println("\nWelcome to the Online Store!");
            System.out.println("1. Show Products");
            System.out.println("2. Show Cart");
            System.out.println("3. Exit");
            System.out.print("Your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter 1, 2, or 3.");
                scanner.nextLine();                 // discard bad input
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();                     // clear newline

            switch (choice) {
                case 1 -> displayProducts(inventory, cart, scanner);
                case 2 -> displayCart(cart, scanner);
                case 3 -> System.out.println("Thank you for shopping with us!");
                default -> System.out.println("Invalid choice!");
            }
        }
        scanner.close();
    }

    /**
     * Reads product data from a file and populates the inventory list.
     * File format (pipe-delimited), the first line is a header row:
     * SKU|Product Name|Price|Department
     * <p>
     * Example line:
     * AV1051|JBL Bluetooth Speaker|89.95|Audio Video
     */

    public static void loadInventory(String fileName, ArrayList<Product> inventory) {
        // TODO: skip the header line, then read each line, split on "|",
        //       create a Product object, and add it to the inventory list
        try {
            BufferedReader bufferedReader =new BufferedReader(new FileReader(fileName));
            bufferedReader.readLine();
            System.out.println();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] token = line.split("\\|");
                String sku = token[0];
                String name = token[1];
                double price = Double.parseDouble(token[2]);
                String department = token[3];
                Product product =new Product(sku,name,price,department);
                inventory.add( product);
            }
        } catch (Exception e) {
            System.out.println("Something went wrong  " + e);
        }

    }
    /**
     * Displays all products and lets the user add one to the cart.
     * Typing X returns to the main menu.
     */
    public static void displayProducts(ArrayList<Product> inventory,
                                       ArrayList<Product> cart,
                                       Scanner scanner) {
        // TODO: show each product (id, name, price),
        //       prompt for an id, find that product, add to cart
        System.out.println("==== Products ====");
        System.out.println();
        for (Product product : inventory){
            System.out.println(product.getSku() + " " + product.getProductName() + " " + product.getDepartment() + " " + "$" + product.getPrice() );
        }
        boolean added = false;
        boolean exit = false;
        while(!exit) {
            System.out.println();
            System.out.println("chose product using the sku code or 'X' to exit\nYour choice: ");
            String userChoice = scanner.nextLine();
            if (!userChoice.equalsIgnoreCase("x")) {
                for (Product product : inventory) {
                    if (product.getSku().equals(userChoice) && product.getQuantity() == 0) {
                        cart.add(product);
                        product.setQuantity(product.getQuantity() + 1);
                        System.out.println(product.getQuantity() + " " + product.getProductName() + " added to your cart ");
                        added = true;
                    } else if(product.getSku().equals(userChoice) && product.getQuantity()  > 0) {
                        product.setQuantity(product.getQuantity() + 1);
                        System.out.println(product.getQuantity() + " " + product.getProductName() + " added to your cart ");
                    }
                }
                if(!added){System.out.println("No product on this sku: " + userChoice);}
            } else if (userChoice.equalsIgnoreCase("x")) {
                exit = true;

            }
        }
    }

    /**
     * Shows the contents of the cart, calculates the total,
     * and offers the option to check out.
     */
    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {
        // TODO:
        //   • list each product in the cart
        //   • compute the total cost
        //   • ask the user whether to check out (C) or return (X)
        //   • if C, call checkOut(cart, totalAmount, scanner)
        double totalPrice = 0;
            for (Product product : cart ){
                totalPrice += product.getPrice() * product.getQuantity();
                System.out.println(product.getQuantity() + " " + product.getProductName() + " $" + product.getPrice() );

            }
            System.out.printf("Total: $%.2f%n", totalPrice);
        System.out.println("(C) to check out or anything to exit");
           String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("c")){
                checkOut(cart,totalPrice,scanner);
        }

    }

    /**
     * Handles the checkout process:
     * 1. Confirm that the user wants to buy.
     * 2. Accept payment and calculate change.
     * 3. Display a simple receipt.
     * 4. Clear the cart.
     */
    public static void checkOut(ArrayList<Product> cart,
                                double totalAmount,
                                Scanner scanner) {
        // TODO: implement steps listed above

        System.out.printf("Your total will be: " + "$%.2f%n", totalAmount);
        System.out.println("Would you like to buy? (Y) to confirm or anything to cancel");
        String userInput = scanner.nextLine();
        double change;
        if (userInput.equalsIgnoreCase("y")){
            System.out.print("Pleas enter payment amount:");
            Double userPayment = scanner.nextDouble();
            scanner.nextLine();
            if (userPayment < totalAmount){
                System.out.println();
                System.out.printf("Sorry you need " + "$" + (totalAmount - userPayment) + " More to cover the total of " + "$%.2f%n", totalAmount);

            }else if (userPayment > totalAmount){
                change = userPayment-totalAmount;
                System.out.println("Thank you for shopping ");
                for (Product product : cart){
                    System.out.println(product.getQuantity() + " " + product.getProductName() + " " + product.getDepartment() + " " + "$" + product.getPrice() );
                }
                System.out.println();
                System.out.printf("Total Payment: $%.2f", totalAmount);
                System.out.printf(" With a change of: $%.2f%n", change);
                cart.clear();
            }else if (totalAmount <= 0 ){
                System.out.println("Empty cart!");

            }
            else {
                System.out.println();
                System.out.println("Thank you for shopping");
                System.out.println("You ordered: ");
                for (Product product : cart){
                    System.out.println(product.getSku() + " " + product.getProductName() + " " + product.getDepartment() + " " + "$" + product.getPrice() );
                }cart.clear();
            }
        }
    }

    /**
     * Searches a list for a product by its id.
     *
     * @return the matching Product, or null if not found
     */
    public static Product findProductById(String id, ArrayList<Product> inventory) {
        // TODO: loop over the list and compare ids
        return null;
    }
}
