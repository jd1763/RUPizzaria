package com.example.pizzeria;

/**
 * Enum containing the possible crust options that will be presented for the user to pick from.
 *
 * @author Frank Garcia
 */
public enum Crust {
    DEEP_DISH("Deep Dish"),
    BROOKLYN("Brooklyn"),
    PAN("Pan"),
    THIN("Thin"),
    STUFFED("Stuffed"),
    HAND_TOSSED("Hand-tossed");

    /**
     * The name of the pizza or item.
     */
    private final String name;

    /**
     * Initializes the enum with a corresponding string for ease of printing.
     * @param name The String form of the enum.
     */
    Crust(String name) {
        this.name = name;
    }

    /**
     * Returns the string form assigned to the enum.
     * @return The String form of the enum.
     */
    @Override
    public String toString() {
        return name;
    }
}
