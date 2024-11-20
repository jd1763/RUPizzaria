package pizzeria;

/**
 * A factory class that produces different types of New York-style pizzas.
 * Each pizza created by this factory uses a specified default size and
 * specific crusts depending on the pizza type.
 * @author Jorgeluis Done
 */
public class NYPizza implements PizzaFactory {

    /**
     * The default size for pizzas created by this factory.
     */
    private Size defaultSize;

    /**
     * Constructs a NYPizza factory with a specified default size for pizzas.
     *
     * @param size The default size of pizzas produced by this factory.
     */
    public NYPizza(Size size) {
        this.defaultSize = size;
    }

    /**
     * Creates a Deluxe pizza with a Brooklyn crust.
     *
     * @return A new instance of a Deluxe pizza with New York-style crust and default size.
     */
    @Override
    public Pizza createDeluxe() {
        return new Deluxe(Crust.BROOKLYN, defaultSize);
    }

    /**
     * Creates a Meatzza pizza with a hand-tossed crust.
     *
     * @return A new instance of a Meatzza pizza with New York-style crust and default size.
     */
    @Override
    public Pizza createMeatzza() {
        return new Meatzza(Crust.HAND_TOSSED, defaultSize);
    }

    /**
     * Creates a BBQ Chicken pizza with a thin crust.
     *
     * @return A new instance of a BBQ Chicken pizza with New York-style crust and default size.
     */
    @Override
    public Pizza createBBQChicken() {
        return new BBQChicken(Crust.THIN, defaultSize);
    }

    /**
     * Creates a Build Your Own pizza with a hand-tossed crust.
     *
     * @return A new instance of a Build Your Own pizza with New York-style crust and default size.
     */
    @Override
    public Pizza createBuildYourOwn() {
        return new BuildYourOwn(Crust.HAND_TOSSED, defaultSize);
    }
}
