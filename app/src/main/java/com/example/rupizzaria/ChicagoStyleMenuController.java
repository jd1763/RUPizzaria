package pizzeria;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller class for the Chicago Style Pizza Menu.
 * Manages pizza selection, customization, and adding pizzas to the current order.
 * @author Jorgeluis Done
 */
public class ChicagoStyleMenuController {

    /**
     * Menu button for pizza selection and radio buttons for size options.
     */
    @FXML
    private MenuButton pizzaSelect;
    @FXML
    private RadioButton sizeSmall, sizeMedium, sizeLarge;

    /**
     * Text fields for displaying the crust type, pizza price, and quantity.
     */
    @FXML
    private TextField crustTypeField, pizzaPriceField, quantity;

    /**
     * List views for available and selected toppings.
     */
    @FXML
    private ListView<String> toppingsList, selectedToppingsList;

    /**
     * Buttons for managing toppings, adding to order, and adjusting quantity.
     */
    @FXML
    private Button addButton, removeButton, addToOrderButton, plus, minus;

    /**
     * Toggle group for managing pizza size selection.
     */
    @FXML
    private ToggleGroup size;

    /**
     * Factory for creating pizzas based on selected attributes.
     */
    private PizzaFactory pizzaFactory;

    /**
     * The currently selected pizza being customized or added to the order.
     */
    private Pizza currentPizza;

    /**
     * Shared OrderManager instance to manage all orders.
     */
    private static OrderManager orderManager;

    /**
     * Utility for tracking key events, used for handling shortcuts and other key-based actions.
     */
    private KeysTrackerUtil keysTrackerUtil = new KeysTrackerUtil();

    /**
     * Initializes the Chicago Style Pizza Menu with default settings.
     * Configures pizza options, size selection, and topping management.
     */
    @FXML
    public void initialize() {
        pizzaFactory = new ChicagoPizza(Size.SMALL);  // or choose the desired default size
        setupPizzaSelectionMenu();
        setupSizeSelection();
        selectPizza("Build Your Own", "Pan"); // Default pizza type
        setupButtonHandlers(); // Sets up button handlers for topping management

        quantity.setText("1"); // Set default value to 1
        quantity.setEditable(false);

        // Set up button handlers for quantity adjustment
        plus.setOnAction(event -> increaseQuantity());
        minus.setOnAction(event -> decreaseQuantity());
    }

    /**
     * Increases the quantity of the pizza by 1 and updates the total price displayed.
     * This method is triggered when the "+" button is clicked.
     */
    private void increaseQuantity() {
        if(currentPizza != null) {
            int currentQuantity = Integer.parseInt(quantity.getText());
            quantity.setText(String.valueOf(currentQuantity + 1));
            updateTotalPrice(); // Update total price
        }
    }

    /**
     * Decreases the quantity of the pizza by 1 (but not below 1) and updates the total price displayed.
     * This method is triggered when the "-" button is clicked.
     */
    private void decreaseQuantity() {
        if (currentPizza != null) {
            int currentQuantity = Integer.parseInt(quantity.getText());
            if (currentQuantity > 1) {
                quantity.setText(String.valueOf(currentQuantity - 1));
                updateTotalPrice(); // Update total price
            }
        }
    }

    /**
     * Updates the total price displayed based on the current quantity and price of the pizza.
     * Calculates the total price by multiplying the pizza's base price by the current quantity.
     */
    private void updateTotalPrice() {
        int quantityValue = Integer.parseInt(quantity.getText());
        double totalPrice = currentPizza.price() * quantityValue;
        pizzaPriceField.setText(String.format("%.2f", totalPrice));
    }

    /**
     * Sets the OrderManager instance to handle orders.
     *
     * @param orderManager The OrderManager instance to manage orders.
     */
    public void setOrderManager(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    /**
     * Configures size selection radio buttons and updates pizza size when selected.
     */
    private void setupSizeSelection() {
        sizeSmall.setToggleGroup(size);
        sizeMedium.setToggleGroup(size);
        sizeLarge.setToggleGroup(size);

        // Default to small size
        sizeSmall.setSelected(true);

        // Update pizza display when size changes
        sizeSmall.setOnAction(event -> updatePizzaDisplay(Size.SMALL));
        sizeMedium.setOnAction(event -> updatePizzaDisplay(Size.MEDIUM));
        sizeLarge.setOnAction(event -> updatePizzaDisplay(Size.LARGE));
    }

    /**
     * Updates the display of the selected pizza, including crust type and price.
     *
     * @param selectedSize The size to update the pizza to.
     */
    private void updatePizzaDisplay(Size selectedSize) {
        if (currentPizza != null) {
            currentPizza.setSize(selectedSize);
            crustTypeField.setText(currentPizza.getCrust().toString());
            updateTotalPrice();
            // // System.out.println("Updated display for size: " + selectedSize);
        } else {
            // System.out.println("No pizza selected to display.");
        }
    }

    /**
     * Sets up the pizza selection menu with different pizza types.
     */
    private void setupPizzaSelectionMenu() {
        pizzaSelect.getItems().clear();

        MenuItem deluxe = new MenuItem("Deluxe");
        deluxe.setOnAction(e -> selectPizza("Deluxe", "Deep Dish"));

        MenuItem buildYourOwn = new MenuItem("Build Your Own");
        buildYourOwn.setOnAction(e -> selectPizza("Build Your Own", "Pan"));

        MenuItem bbqChicken = new MenuItem("BBQ Chicken");
        bbqChicken.setOnAction(e -> selectPizza("BBQ Chicken", "Thin"));

        MenuItem meatzza = new MenuItem("Meatzza");
        meatzza.setOnAction(e -> selectPizza("Meatzza", "Stuffed"));

        pizzaSelect.getItems().addAll(deluxe, buildYourOwn, bbqChicken, meatzza);
        pizzaSelect.setText("Build Your Own"); // Default selection
    }

    /**
     * Selects a pizza type and crust, updates display, and loads default toppings for preset pizzas.
     *
     * @param pizzaType The type of pizza selected.
     * @param crustType The crust type selected.
     */
    private void selectPizza(String pizzaType, String crustType) {
        clearToppings();
        Crust crust = Crust.valueOf(crustType.replace(" ", "_").toUpperCase());
        quantity.setText("1");
        // Instantiate pizza based on selected type and size
        currentPizza = switch (pizzaType) {
            case "Deluxe" -> {
                pizzaSelect.setText("Deluxe");
                yield pizzaFactory.createDeluxe();
            }
            case "Build Your Own" -> {
                pizzaSelect.setText("Build Your Own");
                yield pizzaFactory.createBuildYourOwn();
            }
            case "BBQ Chicken" -> {
                pizzaSelect.setText("BBQ Chicken");
                yield pizzaFactory.createBBQChicken();
            }
            case "Meatzza" -> {
                pizzaSelect.setText("Meatzza");
                yield pizzaFactory.createMeatzza();
            }
            default -> null;
        };

        if (currentPizza != null) {
            currentPizza.setCrust(crust);
            updatePizzaDisplay(getSelectedSize());
            loadToppingsForPizzaType(pizzaType);
            // System.out.println("Selected pizza: " + pizzaType + " with crust: " + crust);
        } else {
            // System.out.println("Failed to create pizza of type: " + pizzaType);
        }
    }

    /**
     * Loads toppings based on the selected pizza type.
     *
     * @param pizzaType The type of pizza to load toppings for.
     */
    private void loadToppingsForPizzaType(String pizzaType) {
        List<Topping> toppings = switch (pizzaType) {
            case "Deluxe" -> Arrays.asList(Topping.SAUSAGE, Topping.PEPPERONI, Topping.GREENPEPPER, Topping.ONION, Topping.MUSHROOM);
            case "BBQ Chicken" -> Arrays.asList(Topping.BBQCHICKEN, Topping.GREENPEPPER, Topping.PROVOLONE, Topping.CHEDDAR);
            case "Meatzza" -> Arrays.asList(Topping.SAUSAGE, Topping.PEPPERONI, Topping.BEEF, Topping.HAM);
            case "Build Your Own" -> List.of();
            default -> List.of();
        };

        ObservableList<String> toppingNames = FXCollections.observableArrayList();
        toppings.forEach(topping -> toppingNames.add(topping.toString()));
        selectedToppingsList.setItems(toppingNames);

        // If "Build Your Own", load all available toppings
        if (pizzaType.equals("Build Your Own")) {
            initializeToppingsList();
        }
    }

    /**
     * Clears all selected and available toppings from the UI.
     */
    private void clearToppings() {
        toppingsList.getItems().clear();
        selectedToppingsList.getItems().clear();
    }

    /**
     * Initializes the list of all available toppings for "Build Your Own" pizza.
     */
    private void initializeToppingsList() {
        List<String> toppingNames = new ArrayList<>();
        for (Topping topping : Topping.values()) {
            toppingNames.add(topping.toString());
        }
        toppingsList.setItems(FXCollections.observableArrayList(toppingNames));
    }

    /**
     * Gets the currently selected pizza size.
     *
     * @return The selected Size enum.
     */
    private Size getSelectedSize() {
        return sizeLarge.isSelected() ? Size.LARGE : sizeMedium.isSelected() ? Size.MEDIUM : Size.SMALL;
    }

    /**
     * Sets up button handlers for adding or removing toppings.
     */
    private void setupButtonHandlers() {
        addButton.setOnAction(event -> moveToppingToSelected());
        removeButton.setOnAction(event -> moveToppingToAvailable());
    }

    /**
     * Moves a topping from available to selected list, updating pizza price.
     */
    private void moveToppingToSelected() {
        String selected = toppingsList.getSelectionModel().getSelectedItem();
        if (currentPizza != null && selected != null && selectedToppingsList.getItems().size() < 7) {
            try {
                Topping topping = Topping.fromString(selected);
                currentPizza.addTopping(topping);
                selectedToppingsList.getItems().add(selected);
                toppingsList.getItems().remove(selected);
                updateTotalPrice();
            } catch (IllegalArgumentException e) {
                // System.out.println("Invalid topping: " + selected);
            }
        }
    }

    /**
     * Moves a topping from the selected list back to available, updating pizza price.
     * Does nothing if the current pizza is not "Build Your Own".
     */
    private void moveToppingToAvailable() {
        // Check if the current pizza is "Build Your Own"
        if (!(currentPizza instanceof BuildYourOwn)) {
            // System.out.println("Topping removal is only allowed for 'Build Your Own' pizzas.");
            return;
        }

        String selected = selectedToppingsList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                Topping topping = Topping.fromString(selected);
                currentPizza.removeTopping(topping);
                toppingsList.getItems().add(selected);
                selectedToppingsList.getItems().remove(selected);
                updateTotalPrice();
            } catch (IllegalArgumentException e) {
                // System.out.println("Invalid topping: " + selected);
            }
        }
    }

    /**
     * Handler for the KeysTrackerUtil utility methods to help track for shortcuts.
     * @param keyEvent The key being pressed and tracked in the key tracker utility class.
     */
    @FXML
    private void trackKeysPressed(KeyEvent keyEvent) {
        keysTrackerUtil.keysPressedTracker(keyEvent);
    }

    /**
     * Handler for the KeysTrackerUtil utility methods to help track for shortcuts.
     * @param keyEvent The key being released and tracked in the key tracker utility class.
     */
    @FXML
    private void trackKeysReleased(KeyEvent keyEvent) {
        keysTrackerUtil.keysReleasedTracker(keyEvent);
    }

    /**
     * Adds the current pizza to the order via the OrderManager.
     */
    @FXML
    private void addToOrder() {
        if (currentPizza != null) {
            int orderQuantity = Integer.parseInt(quantity.getText());
            for (int i = 0; i < orderQuantity; i++) {
                // Adds the pizza to the order manager's current order
                orderManager.getCurrentOrder().addPizza(currentPizza);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pizza successfully added to order.");
            alert.setTitle("Pizza Confirmation");
            alert.showAndWait();
            // System.out.println("Pizza added to current order: " + currentPizza);
            // System.out.println("Number of pizzas in order: "+ orderManager.getCurrentOrder().getPizzas().size());
            currentPizza = null;
            quantity.setText("");
            pizzaPriceField.clear();
            crustTypeField.clear();
            clearToppings();
            pizzaSelect.setText("Select Pizza"); // Reset pizza select button text
        } else {
            // System.out.println("No pizza to add to order.");
        }
    }
}
