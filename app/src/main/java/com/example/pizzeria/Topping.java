package com.example.pizzeria;

/**
 * Enum representing available pizza toppings with display-friendly names.
 */
public enum Topping {
    SAUSAGE("Sausage"),
    PEPPERONI("Pepperoni"),
    GREENPEPPER("Green Pepper"),
    ONION("Onion"),
    MUSHROOM("Mushroom"),
    BBQCHICKEN("BBQ Chicken"),
    PROVOLONE("Provolone"),
    CHEDDAR("Cheddar"),
    BEEF("Beef"),
    HAM("Ham");

    /**
     * The display-friendly name of the topping.
     */
    private final String displayIngredient;

    /**
     * Constructs a Topping with a display-friendly ingredient name.
     *
     * @param displayIngredient the display name of the topping.
     */
    Topping(String displayIngredient) {
        this.displayIngredient = displayIngredient;
    }

    /**
     * Returns the display name of the topping.
     *
     * @return the display-friendly name of the topping.
     */
    @Override
    public String toString() {
        return displayIngredient;
    }

    /**
     * Converts a string to a corresponding Topping enum, matching by display name.
     *
     * @param text the display name to match.
     * @return the Topping enum corresponding to the given display name.
     * @throws IllegalArgumentException if no matching Topping is found.
     */
    public static Topping fromString(String text) {
        for (Topping topping : Topping.values()) {
            if (topping.displayIngredient.equalsIgnoreCase(text)) {
                return topping;
            }
        }
        throw new IllegalArgumentException("No enum constant for " + text);
    }
}