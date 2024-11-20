package pizzeria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the BuildYourOwn pizza class.
 * Uses JUnit to test the price calculation based on size and the number of toppings.
 */
public class BuildYourOwnTest {

    private BuildYourOwn smallPizza;
    private BuildYourOwn mediumPizza;
    private BuildYourOwn largePizza;

    /**
     * Initializes new pizza instances before each test.
     * Ensures that each test starts with a clean instance of BuildYourOwn.
     */
    @BeforeEach
    public void setUp() {
        smallPizza = new BuildYourOwn(Crust.PAN, Size.SMALL);
        mediumPizza = new BuildYourOwn(Crust.PAN, Size.MEDIUM);
        largePizza = new BuildYourOwn(Crust.PAN, Size.LARGE);
    }

    /**
     * Tests the price of a small pizza with no toppings.
     * Verifies that the price matches the base price for a small pizza.
     */
    @Test
    public void testPriceSmallPizzaNoToppings() {
        double expectedPrice = smallPizza.basePrice();
        assertEquals(expectedPrice, smallPizza.price(), 0.01, "Price for small pizza with no toppings should match base price");
    }

    /**
     * Tests the price of a medium pizza with no toppings.
     * Ensures that toppings count is zero at the start and price matches the base price.
     */
    @Test
    public void testPriceMediumPizzaWithNoToppings() {
        assertEquals(0, mediumPizza.getToppings().size(), "Toppings should be empty initially");

        double expectedPrice = mediumPizza.basePrice();
        assertEquals(expectedPrice, mediumPizza.price(), 0.01, "Price for medium pizza with no toppings should match base price");
    }

    /**
     * Tests the price of a medium pizza with two toppings.
     * The expected price is the base price plus the cost of two toppings.
     */
    @Test
    public void testPriceMediumPizzaWithTwoToppings() {
        mediumPizza.addTopping(Topping.PEPPERONI);
        mediumPizza.addTopping(Topping.MUSHROOM);

        double expectedPrice = mediumPizza.basePrice() + 2 * 1.69;
        assertEquals(expectedPrice, mediumPizza.price(), 0.01, "Price for medium pizza with two toppings should match base + 2 * topping price");
    }

    /**
     * Tests the price of a large pizza with the maximum number of toppings (7).
     * The expected price is the base price plus the cost of seven toppings.
     */
    @Test
    public void testPriceLargePizzaWithMaxToppings() {
        for (int i = 0; i < 7; i++) {
            largePizza.addTopping(Topping.values()[i]);
        }

        double expectedPrice = largePizza.basePrice() + 7 * 1.69;
        assertEquals(expectedPrice, largePizza.price(), 0.01, "Price for large pizza with max toppings should match base + 7 * topping price");
    }

    /**
     * Tests the price of a small pizza with three toppings.
     * The expected price is the base price plus the cost of three toppings.
     */
    @Test
    public void testPriceSmallPizzaWithThreeToppings() {
        smallPizza.addTopping(Topping.SAUSAGE);
        smallPizza.addTopping(Topping.ONION);
        smallPizza.addTopping(Topping.GREENPEPPER);

        double expectedPrice = smallPizza.basePrice() + 3 * 1.69;
        assertEquals(expectedPrice, smallPizza.price(), 0.01, "Price for small pizza with three toppings should match base + 3 * topping price");
    }

    /**
     * Tests that the base price of a BuildYourOwn pizza matches the expected values for each size.
     */
    @Test
    public void testBasePriceForSizes() {
        assertEquals(8.99, smallPizza.basePrice(), 0.01, "Base price for small pizza");
        assertEquals(10.99, mediumPizza.basePrice(), 0.01, "Base price for medium pizza");
        assertEquals(12.99, largePizza.basePrice(), 0.01, "Base price for large pizza");
    }

    /**
     * Main method to run tests individually.
     * Note: This is an additional method and is not commonly used with JUnit tests, as
     * JUnit runners handle test execution. However, it's included here as requested.
     */
    public static void main(String[] args) {
        BuildYourOwnTest test = new BuildYourOwnTest();
        test.setUp();

        // Run each test
        test.testPriceSmallPizzaNoToppings();
        test.testPriceMediumPizzaWithNoToppings();
        test.testPriceMediumPizzaWithTwoToppings();
        test.testPriceLargePizzaWithMaxToppings();
        test.testPriceSmallPizzaWithThreeToppings();
        test.testBasePriceForSizes();

        System.out.println("All tests executed successfully.");
    }
}
