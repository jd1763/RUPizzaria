package pizzeria;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks all instances of Pizza and generates a serial number for the order where a unique integer
 * is assigned as their identifier.
 * @author Jorgeluis Done
 */
public class Order {
    /**
     * The unique number identifying this order.
     */
    private int orderNumber;

    /**
     * The list of pizzas included in this order.
     */
    private List<Pizza> pizzas;

    /**
     * Constructs an Order with a specified order number and list of pizzas.
     *
     * @param orderNumber The unique order number.
     * @param pizzas      The list of pizzas in the order.
     *
     * @author Jorgeluis Done
     * @author Frank Garcia
     */
    public Order(int orderNumber, List<Pizza> pizzas) {
        this.orderNumber = orderNumber;
        this.pizzas = new ArrayList<>(pizzas); // Initialize with the provided pizzas or an empty list
    }

    /**
     * Adds a pizza to the order.
     *
     * @param pizza The pizza to add.
     */
    public void addPizza(Pizza pizza) {
        pizzas.add(pizza);
    }

    /**
     * Removes a pizza from the order.
     *
     * @param pizza The pizza to remove.
     */
    public void removePizza(Pizza pizza) {
        pizzas.remove(pizza);
    }

    /**
     * Gets the unique order number.
     *
     * @return The order number.
     */
    public int getOrderNumber() {
        return orderNumber;
    }

    /**
     * Retrieves the list of pizzas in the order.
     *
     * @return The list of pizzas.
     */
    public List<Pizza> getPizzas() {
        return pizzas;
    }

    /**
     * Sets the orderNumber field of the order object
     * @param orderNumber The new integer the orderNumber field will be assigned.
     */
    public void setOrderNumber(int orderNumber) { this.orderNumber = orderNumber; }

    /**
     * Returns a string representation of the current order, including the order number and details
     * of each pizza in the order.
     *
     * @return a formatted string with the order number and each pizza's details listed line by line.
     */
    @Override
    public String toString() {
        String currOrderString = "";
        currOrderString += "Order number: " + orderNumber + "\n";
        for (Pizza pizza : pizzas) {
            currOrderString += pizza.toString() + "\n";
        }

        return currOrderString;
    }
}
