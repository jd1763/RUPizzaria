package com.example.pizzeria;

import java.util.Arrays;

/**
 * Represents a Deluxe pizza with a specific set of toppings and Chicago-style crust.
 * The Deluxe pizza comes with a fixed set of toppings: sausage, pepperoni, green pepper, onion, and mushroom.
 * The price of the pizza is based on the selected size.
 * @author Jorgeluis Done
 */
public class Deluxe extends Pizza {

    /**
     * Constructs a Deluxe pizza with the specified crust type and size.
     * The Deluxe pizza always includes sausage, pepperoni, green pepper, onion, and mushroom as toppings.
     *
     * @param crust The type of crust for the Deluxe pizza.
     * @param size The size of the pizza (Small, Medium, or Large).
     */
    public Deluxe(Crust crust, Size size) {
        // Initialize with fixed toppings for Deluxe pizza
        super(Arrays.asList(Topping.SAUSAGE, Topping.PEPPERONI, Topping.GREENPEPPER,
                Topping.ONION, Topping.MUSHROOM), crust, size);
    }

    /**
     * Calculates the price of the Deluxe pizza based on its size.
     *
     * @return The price of the pizza as a double.
     */
    @Override
    public double price() {
        // Set price based on size
        switch (getSize()) {
            case SMALL:
                return 16.99;
            case MEDIUM:
                return 18.99;
            case LARGE:
                return 20.99;
            default:
                return 16.99; // Default to small price if size is undefined
        }
    }
}
