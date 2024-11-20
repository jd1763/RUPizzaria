package pizzeria;

import java.util.Arrays;

/**
 * Represents a BBQ Chicken pizza with predefined toppings and prices based on size.
 * This pizza includes toppings specific to BBQ Chicken, such as BBQ chicken, green pepper,
 * provolone, and cheddar, and it uses a specific crust and size.
 * @author Jorgeluis Done
 */
public class BBQChicken extends Pizza {

    /**
     * Constructs a BBQChicken pizza with the specified crust and size.
     * Initializes the pizza with toppings specific to BBQ Chicken.
     *
     * @param crust The crust type for the BBQ Chicken pizza.
     * @param size  The size of the BBQ Chicken pizza.
     */
    public BBQChicken(Crust crust, Size size) {
        // Initialize with specific toppings for BBQ Chicken
        super(Arrays.asList(Topping.BBQCHICKEN, Topping.GREENPEPPER, Topping.PROVOLONE, Topping.CHEDDAR), crust, size);
    }

    /**
     * Calculates the price of the BBQ Chicken pizza based on its size.
     *
     * @return The price of the pizza.
     */
    @Override
    public double price() {
        // Determine price based on the pizza size
        switch (getSize()) {
            case SMALL:
                return 14.99;
            case MEDIUM:
                return 16.99;
            case LARGE:
                return 19.99;
            default:
                return 14.99; // Default to small if size is not defined
        }
    }
}
