package pizzeria;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The OrderManager class manages pizza orders within the pizzeria system. It handles operations such as adding,
 * removing, and placing orders, calculating prices, and saving orders to a file.
 * It also maintains a list of all orders.
 *
 * @author Jorgeluis Done
 */
public class OrderManager {
    /**
     * The unique number assigned to each order, starting at 1.
     */
    private int orderNumber = 1;

    /**
     * The current order being processed, initialized with the default order number.
     */
    private Order currentOrder = new Order(orderNumber, new ArrayList<Pizza>());

    /**
     * The list of all completed orders.
     */
    private List<Order> allOrders = new ArrayList<>();

    /**
     * The sales tax rate applied to each order.
     */
    private static final double SALES_TAX_RATE = 0.06625;

    /**
     * Adds a pizza to the current order.
     *
     * @param pizza The pizza to be added.
     */
    public void addToCurrentOrder(Pizza pizza) {
        currentOrder.addPizza(pizza);
        // System.out.println("Added to current order: " + pizza + " | New subtotal: $" +
        //        String.format("%.2f", calculateSubtotal(currentOrder)));
    }

    /**
     * Removes a pizza from the current order.
     *
     * @param pizza The pizza to be removed.
     */
    public void removeFromCurrentOrder(Pizza pizza) {
        currentOrder.removePizza(pizza);
        // System.out.println("Removed from current order: " + pizza + " | New subtotal: $" +
        //        String.format("%.2f", calculateSubtotal(currentOrder)));
    }

    /**
     * Places the current order, adding it to the list of all orders.
     */
    public void placeCurrentOrder() {
        allOrders.add(currentOrder);
        orderNumber++;
        currentOrder = new Order(orderNumber, new ArrayList<>());
        // System.out.println("Placed order number: " + (orderNumber - 1));
    }

    /**
     * Retrieves an order by its order number.
     *
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
     *
     * @return The list of all orders.
     */
    public List<Order> getAllOrders() {
        return allOrders;
    }

    /**
     * Retrieves the current order.
     *
     * @return The current order.
     */
    public Order getCurrentOrder() {
        return currentOrder;
    }

    /**
     * Clears the list of pizzas in the currentOrder field.
     */
    public void clearCurrentOrder() {
        currentOrder.getPizzas().clear();
    }

    /**
     * Cancels an order by removing it from the list of all orders.
     *
     * @param orderId The order number of the order to cancel.
     */
    public void cancelOrder(int orderId) {
        int currentOrderNumber = 1;
        allOrders.removeIf(order -> order.getOrderNumber() == orderId);
        if (currentOrder.getPizzas().size() != this.orderNumber - 1) {
            currentOrder.setOrderNumber(--this.orderNumber);
            for (Order order : allOrders) {
                currentOrderNumber = Math.min(order.getOrderNumber(), currentOrderNumber);
                order.setOrderNumber(currentOrderNumber);
                currentOrderNumber++;
            }
        }
        // System.out.println("Cancelled order number: " + orderId);
    }

    /**
     * Calculates the subtotal for an order.
     *
     * @param order The order for which to calculate the subtotal.
     * @return The subtotal of the order.
     */
    public static double calculateSubtotal(Order order) {
        return order.getPizzas().stream().mapToDouble(Pizza::price).sum();
    }

    /**
     * Calculates the sales tax for a given subtotal.
     *
     * @param subtotal The subtotal for which to calculate the sales tax.
     * @return The sales tax amount.
     */
    public static double calculateSalesTax(double subtotal) {
        return subtotal * SALES_TAX_RATE;
    }

    /**
     * Calculates the total amount for an order including sales tax.
     *
     * @param subtotal The subtotal of the order.
     * @param salesTax The sales tax amount.
     * @return The total amount of the order including sales tax.
     */
    public static double calculateTotalAmount(double subtotal, double salesTax) {
        return subtotal + salesTax;
    }

    /**
     * Retrieves the total amount for an order including sales tax.
     *
     * @param order The order for which to calculate the total amount.
     * @return The total amount of the order including sales tax.
     */
    public static double getTotalAmount(Order order) {
        double subtotal = calculateSubtotal(order);
        double salesTax = calculateSalesTax(subtotal);
        return calculateTotalAmount(subtotal, salesTax);
    }

    /**
     * Saves orders to a file and returns a message indicating the status of the operation.
     *
     * @return A message indicating the status of the save operation.
     */
    public String saveOrdersToFile() {
        StringBuilder stringBuilder = new StringBuilder();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Orders");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                if (allOrders.isEmpty())
                    writer.println("There are no orders.");
                for (Order order : allOrders) {
                    writer.println("Order Number: " + order.getOrderNumber());
                    writer.println("Pizzas:");
                    for (Pizza pizza : order.getPizzas()) {
                        writer.println("- " + pizza.getClass().getSimpleName() + " (" + pizza.getSize() + ", " +
                                pizza.getCrust() + ")");
                        writer.println("  Toppings: " + pizza.getToppings());
                        writer.println("  Price: $" + String.format("%.2f", pizza.price()));
                    }
                    double subtotal = calculateSubtotal(order);
                    double salesTax = calculateSalesTax(subtotal);
                    double totalAmount = calculateTotalAmount(subtotal, salesTax);
                    writer.println("Subtotal: $" + String.format("%.2f", subtotal));
                    writer.println("Sales Tax: $" + String.format("%.2f", salesTax));
                    writer.println("Total Amount: $" + String.format("%.2f", totalAmount));
                    writer.println();
                }
                stringBuilder.append("Orders saved to the file.\n");
            } catch (Exception e) {
                stringBuilder.append("Error saving orders to file: ").append(e.getMessage()).append("\n");
            }
        } else {
            stringBuilder.append("File selection canceled by user.\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Filters pizzas by type from the current order.
     *
     * @param pizzaType The class of pizza to filter (e.g., Deluxe.class).
     * @return A list of pizzas of the specified type in the current order.
     */
    public List<Pizza> filterPizzaByType(Class<? extends Pizza> pizzaType) {
        return currentOrder.getPizzas().stream()
                .filter(pizza -> pizza.getClass() == pizzaType)
                .collect(Collectors.toList());
    }
}
