package com.example.pizzeria;

import java.util.List;

/**
 * Abstract base class representing a pizza with customizable toppings, crust, and size.
 * Each pizza has a set price based on its size, with an option to add or remove toppings.
 * This class provides a base for specific pizza types to define their unique price and toppings.
 *
 * The pizza price can vary based on the size and type, and toppings can be modified in "Build Your Own" pizzas.
 * Specific pizza types will have default toppings set by subclasses.
 *
 * @author Jorgeluis Done
 * @author Frank Garcia
 */
public abstract class Pizza {
    /**
     * List of toppings selected for the pizza.
     */
    private List<Topping> toppings;

    /**
     * The crust type for the pizza.
     */
    private Crust crust;

    /**
     * The size of the pizza.
     */
    private Size size;

    /**
     * Constructs a pizza with the specified toppings, crust, and size.
     *
     * @param toppings List of toppings on the pizza.
     * @param crust The type of crust for the pizza.
     * @param size The size of the pizza (small, medium, large).
     */
    public Pizza(List<Topping> toppings, Crust crust, Size size) {
        this.toppings = toppings;
        this.crust = crust;
        this.size = size;
    }

    /**
     * Returns the list of toppings currently on the pizza.
     *
     * @return List of toppings.
     */
    public List<Topping> getToppings() {
        return toppings;
    }

    /**
     * Returns the crust type of the pizza.
     *
     * @return The crust type.
     */
    public Crust getCrust() {
        return crust;
    }

    /**
     * Adds a topping to the pizza.
     *
     * @param topping The topping to be added.
     */
    public void addTopping(Topping topping) {
        toppings.add(topping);
    }

    /**
     * Removes a topping from the pizza.
     *
     * @param topping The topping to be removed.
     */
    public void removeTopping(Topping topping) {
        toppings.remove(topping);
    }

    /**
     * Returns the size of the pizza.
     *
     * @return The pizza size.
     */
    public Size getSize() {
        return size;
    }

    /**
     * Sets the crust type for the pizza.
     *
     * @param crust The crust type to be set.
     */
    public void setCrust(Crust crust) {
        this.crust = crust;
    }

    /**
     * Sets the size of the pizza.
     *
     * @param size The size to be set.
     */
    public void setSize(Size size) {
        this.size = size;
    }

    /**
     * Calculates the price of the pizza. Must be implemented by subclasses to define specific pricing.
     *
     * @return The calculated price of the pizza.
     */
    public abstract double price();

    /**
     * Returns the base price of the pizza based on its size.
     * This method can be used by subclasses to calculate the final price with any additional toppings.
     *
     * @return The base price depending on the pizza size.
     */
    protected double basePrice() {
//        System.out.println("Calculating base price for size: " + size);
        switch (size) {
            case SMALL:
                return 8.99; // Start price for small size base
            case MEDIUM:
                return 10.99; // Start price for medium size base
            case LARGE:
                return 12.99; // Start price for large size base
            default:
                return 8.99; // Default to small if unknown
        }
    }

    /**
     * Returns a string representation of the pizza, including its size, crust, and toppings.
     *
     * @return a formatted string with the pizza's size, crust, and toppings.
     */
    @Override
    public String toString() {
        return String.format("Size: %s, Crust: %s, Toppings: %s", size, crust, toppings);
    }
}
