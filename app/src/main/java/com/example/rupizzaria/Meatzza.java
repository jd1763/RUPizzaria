package pizzeria;

import java.util.Arrays;

/**
 * Represents a Meatzza pizza with a specific set of toppings and a customizable crust.
 * The Meatzza pizza includes fixed toppings: sausage, pepperoni, beef, and ham.
 * The price of the pizza varies based on the selected size.
 * @author Jorgeluis Done
 */
public class Meatzza extends Pizza {

    /**
     * Constructs a Meatzza pizza with the specified crust type and size.
     * The Meatzza pizza always includes sausage, pepperoni, beef, and ham as toppings.
     *
     * @param crust The type of crust for the Meatzza pizza.
     * @param size The size of the pizza (Small, Medium, or Large).
     */
    public Meatzza(Crust crust, Size size) {
        // Initialize with fixed toppings for Meatzza pizza
        super(Arrays.asList(Topping.SAUSAGE, Topping.PEPPERONI, Topping.BEEF, Topping.HAM), crust, size);
    }

    /**
     * Calculates the price of the Meatzza pizza based on its size.
     *
     * @return The price of the pizza as a double.
     */
    @Override
    public double price() {
        // Determine price based on size
        switch (getSize()) {
            case SMALL:
                return 17.99;
            case MEDIUM:
                return 19.99;
            case LARGE:
                return 21.99;
            default:
                return 17.99; // Default to small price if size is undefined
        }
    }
}
