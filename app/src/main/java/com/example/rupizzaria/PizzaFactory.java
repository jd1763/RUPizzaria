package pizzeria;

/**
 * Public interface containing methods to be implemented by the ChicagoPizza and NYPizza classes.
 *
 * @author Frank Garcia
 */
public interface PizzaFactory {
    Pizza createDeluxe();
    Pizza createMeatzza();
    Pizza createBBQChicken();
    Pizza createBuildYourOwn();
}
