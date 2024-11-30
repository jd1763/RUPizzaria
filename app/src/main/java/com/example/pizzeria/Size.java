package com.example.pizzeria;

/**
 * The size options presented to the user.
 * @author Frank Garcia
 */
public enum Size {
    SMALL("Small"),
    MEDIUM("Medium"),
    LARGE("Large");

    /**
     * The size of the pizza as a string.
     */
    private String size;

    /**
     * Initializes the enum with a corresponding string for ease of printing.
     * @param size The String form of the enum.
     */
    Size(String size) {
        this.size = size;
    }

    /**
     * Returns the string form assigned to the enum.
     * @return The String form of the enum.
     */
    @Override
    public String toString() {
        return size;
    }
}
