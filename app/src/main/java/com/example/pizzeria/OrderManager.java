package com.example.pizzeria;

import android.content.Context;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * The OrderManager class manages pizza orders within the pizzeria system.
 * It handles operations such as adding, removing, and placing orders,
 * calculating prices, and saving orders to a file. It also maintains a list of all orders.
 * @author jorgeluis
 */
public class OrderManager {
    private int orderNumber = 1;
    private Order currentOrder = new Order(orderNumber, new ArrayList<>());
    private List<Order> allOrders = new ArrayList<>();
    private static final double SALES_TAX_RATE = 0.06625;

    /**
     * Adds a pizza to the current order.
     * @param pizza The pizza to be added.
     */
    public void addToCurrentOrder(Pizza pizza) {
        currentOrder.addPizza(pizza);
    }

    /**
     * Removes a pizza from the current order.
     * @param pizza The pizza to be removed.
     */
    public void removeFromCurrentOrder(Pizza pizza) {
        currentOrder.removePizza(pizza);
    }

    public void clearCurrentOrder() {
        if (currentOrder != null) {
            // Directly reset the current order with a new instance
            currentOrder = new Order(orderNumber, new ArrayList<>());  // Create a new order, effectively clearing the previous contents

            // Optionally, log this action for debug purposes:
            // Log.d("OrderManager", "Current order cleared and reset.");
        }
    }

    /**
     * Places the current order, adding it to the list of all orders.
     */
    public void placeCurrentOrder() {
        allOrders.add(currentOrder);
        orderNumber++;
        currentOrder = new Order(orderNumber, new ArrayList<>());
    }

    /**
     * Retrieves an order by its order number.
     * @param orderNumber The order number to search for.
     * @return The order with the specified order number, or null if not found.
     */
    public Order getOrderFromNumber(int orderNumber) {
        for (Order order : allOrders) {
            if (order.getOrderNumber() == orderNumber) {
                return order;
            }
        }
        return null;
    }

    /**
     * Retrieves a list of all orders placed.
     * @return The list of all orders.
     */
    public List<Order> getAllOrders() {
        return allOrders;
    }

    /**
     * Retrieves the current order.
     * @return The current order.
     */
    public Order getCurrentOrder() {
        return currentOrder;
    }

    /**
     * Cancels an order by removing it from the list of all orders.
     * @param orderId The order number of the order to cancel.
     */
    public void cancelOrder(int orderId) {
        allOrders.removeIf(order -> order.getOrderNumber() == orderId);
    }

    /**
     * Calculates the subtotal for an order.
     * @param order The order for which to calculate the subtotal.
     * @return The subtotal of the order.
     */
    public static double calculateSubtotal(Order order) {
        return order.getPizzas().stream().mapToDouble(Pizza::price).sum();
    }

    /**
     * Calculates the sales tax for a given subtotal.
     * @param subtotal The subtotal for which to calculate the sales tax.
     * @return The sales tax amount.
     */
    public static double calculateSalesTax(double subtotal) {
        return subtotal * SALES_TAX_RATE;
    }

    /**
     * Calculates the total amount for an order including sales tax.
     * @param subtotal The subtotal of the order.
     * @param salesTax The sales tax amount.
     * @return The total amount of the order including sales tax.
     */
    public static double calculateTotalAmount(double subtotal, double salesTax) {
        return subtotal + salesTax;
    }

    /**
     * Saves orders to a file in the device's internal storage.
     * @param context The context used to open the file output stream.
     * @param filename The name of the file to save the orders to.
     * @return A string indicating the status of the save operation.
     */
    public String saveOrdersToFile(Context context, String filename) {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {
            for (Order order : allOrders) {
                writer.write("Order Number: " + order.getOrderNumber() + "\n");
                writer.write("Pizzas:\n");
                for (Pizza pizza : order.getPizzas()) {
                    writer.write("- " + pizza.getClass().getSimpleName() + "\n");
                }
                double subtotal = calculateSubtotal(order);
                double salesTax = calculateSalesTax(subtotal);
                double totalAmount = calculateTotalAmount(subtotal, salesTax);
                writer.write("Total Amount: $" + String.format("%.2f", totalAmount) + "\n\n");
            }
            stringBuilder.append("Orders saved to the file.\n");
        } catch (Exception e) {
            stringBuilder.append("Error saving orders to file: ").append(e.getMessage()).append("\n");
        }
        return stringBuilder.toString();
    }
}