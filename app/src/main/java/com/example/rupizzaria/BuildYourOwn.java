package pizzeria;

import java.util.ArrayList;

/**
 * Represents a customizable pizza where users can add their own toppings up to a limit.
 * This pizza allows customers to build their own with a choice of crust, size, and selected toppings.
 * Additional cost is applied based on the number of toppings added.
 * @author Jorgeluis Done
 */
public class BuildYourOwn extends Pizza {

    /**
     * Constructs a BuildYourOwn pizza with a specified crust and size but no initial toppings.
     *
     * @param crust The crust type for the Build Your Own pizza.
     * @param size  The size of the Build Your Own pizza.
     */
    public BuildYourOwn(Crust crust, Size size) {
        super(new ArrayList<>(), crust, size);
//        System.out.println("Created BuildYourOwn pizza with size: " + size);
    }

    /**
     * Adds a topping to the pizza, if it doesn't exceed the topping limit or duplicate existing toppings.
     *
     * @param topping The topping to add to the pizza.
     */
    public void addTopping(Topping topping) {
        if (getToppings().size() < 7 && !getToppings().contains(topping)) {
            getToppings().add(topping);
            // System.out.println("Added topping: " + topping + " | New price: $" + String.format("%.2f", price()));
        } else if (getToppings().contains(topping)) {
            // System.out.println("Topping already added: " + topping);
        } else {
            // System.out.println("Cannot add more than 7 toppings.");
        }
    }

    /**
     * Removes a topping from the pizza if it is currently added.
     *
     * @param topping The topping to remove from the pizza.
     */
    public void removeTopping(Topping topping) {
        if (getToppings().contains(topping)) {
            getToppings().remove(topping);
            // System.out.println("Removed topping: " + topping + " | New price: $" + String.format("%.2f", price()));
        } else {
            // System.out.println("Topping not found: " + topping);
        }
    }

    /**
     * Adds a topping to the pizza by the topping's name.
     * Attempts to convert the string name into a Topping enum using Topping.fromString.
     *
     * @param toppingName The name of the topping to add.
     */
    public void addToppingByName(String toppingName) {
        try {
            Topping topping = Topping.fromString(toppingName);
            addTopping(topping);
        } catch (IllegalArgumentException e) {
            // System.out.println("Invalid topping: " + toppingName);
        }
    }

    /**
     * Removes a topping from the pizza by the topping's name.
     * Attempts to convert the string name into a Topping enum using Topping.fromString.
     *
     * @param toppingName The name of the topping to remove.
     */
    public void removeToppingByName(String toppingName) {
        try {
            Topping topping = Topping.fromString(toppingName);
            removeTopping(topping);
        } catch (IllegalArgumentException e) {
            // System.out.println("Invalid topping: " + toppingName);
        }
    }

    /**
     * Calculates the total price of the Build Your Own pizza, including the base price
     * and an additional charge for each topping added.
     *
     * @return The total price of the pizza.
     */
    @Override
    public double price() {
//        System.out.println("Calculating price with base price: " + basePrice() + " and toppings count: " + getToppings().size());
        return basePrice() + 1.69 * getToppings().size();
    }
}
