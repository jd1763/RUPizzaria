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
 * Controller class for the New York Style Pizza menu.
 * Manages pizza selection, customization, and adding pizzas to the current order.
 * @author Jorgeluis Done
 */
public class NYPizzaStyleMenuController {

    /**
     * Menu button for selecting pizza type.
     */
    @FXML
    private MenuButton pizzaSelect;

    /**
     * Radio buttons for selecting pizza size.
     */
    @FXML
    private RadioButton sizeSmall, sizeMedium, sizeLarge;

    /**
     * Text fields for displaying crust type, pizza price, and quantity.
     */
    @FXML
    private TextField crustTypeField, pizzaPriceField, quantity;

    /**
     * List views for available and selected toppings.
     */
    @FXML
    private ListView<String> toppingsList, selectedToppingsList;

    /**
     * Buttons for managing toppings, adding to the order, and adjusting quantity.
     */
    @FXML
    private Button addButton, removeButton, addToOrderButton, plus, minus;

    /**
     * Toggle group for selecting pizza size.
     */
    @FXML
    private ToggleGroup size;

    /**
     * Factory instance for creating pizzas based on selected attributes.
     */
    private PizzaFactory pizzaFactory;

    /**
     * Currently selected pizza being customized or added to the order.
     */
    private Pizza currentPizza;

    /**
     * Shared OrderManager instance for managing all orders.
     */
    private static OrderManager orderManager;

    /**
     * Utility for tracking key events, used for handling shortcuts and key-based actions.
     */
    private KeysTrackerUtil keysTrackerUtil = new KeysTrackerUtil();

    /**
     * Initializes the New York Style Pizza menu view, setting default selections and handlers.
     */
    @FXML
    public void initialize() {
        pizzaFactory = new NYPizza(Size.SMALL);  // or choose the desired default size
        setupPizzaSelectionMenu();
        setupSizeSelection();
        selectPizza("Build Your Own", "Hand Tossed"); // Default pizza type
        setupButtonHandlers(); // Setup the buttons for moving toppings

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
     * Sets the OrderManager instance for managing the user's current order.
     *
     * @param orderManager the OrderManager instance to be used
     */
    public void setOrderManager(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    /**
     * Configures the pizza size selection radio buttons.
     * Each button, when selected, updates the display to reflect the chosen size.
     */
    private void setupSizeSelection() {
        sizeSmall.setToggleGroup(size);
        sizeMedium.setToggleGroup(size);
        sizeLarge.setToggleGroup(size);
        sizeSmall.setSelected(true); // Default size selection

        sizeSmall.setOnAction(event -> updatePizzaDisplay(Size.SMALL));
        sizeMedium.setOnAction(event -> updatePizzaDisplay(Size.MEDIUM));
        sizeLarge.setOnAction(event -> updatePizzaDisplay(Size.LARGE));
    }

    /**
     * Updates the pizza display fields with current size, crust, and price.
     *
     * @param selectedSize the size to update the pizza with
     */
    private void updatePizzaDisplay(Size selectedSize) {
        if (currentPizza != null) {
            currentPizza.setSize(selectedSize);
            crustTypeField.setText(currentPizza.getCrust().toString());
            updateTotalPrice();
            // System.out.println("Updated display for size: " + selectedSize);
        } else {
            // System.out.println("No pizza selected to display.");
        }
    }

    /**
     * Sets up the menu for pizza type selection. Each type has a specific crust.
     */
    private void setupPizzaSelectionMenu() {
        pizzaSelect.getItems().clear();

        MenuItem deluxe = new MenuItem("Deluxe");
        deluxe.setOnAction(e -> selectPizza("Deluxe", "Brooklyn"));

        MenuItem buildYourOwn = new MenuItem("Build Your Own");
        buildYourOwn.setOnAction(e -> selectPizza("Build Your Own", "Hand Tossed"));

        MenuItem bbqChicken = new MenuItem("BBQ Chicken");
        bbqChicken.setOnAction(e -> selectPizza("BBQ Chicken", "Thin"));

        MenuItem meatzza = new MenuItem("Meatzza");
        meatzza.setOnAction(e -> selectPizza("Meatzza", "Hand Tossed"));

        pizzaSelect.getItems().addAll(deluxe, buildYourOwn, bbqChicken, meatzza);
        pizzaSelect.setText("Build Your Own"); // Default selection
    }

    /**
     * Handles pizza selection based on the selected type and crust.
     * Clears toppings for each new pizza type.
     *
     * @param pizzaType the type of pizza to create (e.g., "Deluxe")
     * @param crustType the type of crust associated with the pizza type
     */
    private void selectPizza(String pizzaType, String crustType) {
        clearToppings();
        Crust crust = Crust.valueOf(crustType.replace(" ", "_").toUpperCase());
        quantity.setText("1");
        // Instantiate pizza based on selected type and size
        currentPizza = switch (pizzaType) {
            case "Deluxe" -> {
                pizzaSelect.setText("Deluxe");
                yield new Deluxe(crust, getSelectedSize());
            }
            case "Build Your Own" -> {
                pizzaSelect.setText("Build Your Own");
                yield new BuildYourOwn(crust, getSelectedSize());
            }
            case "BBQ Chicken" -> {
                pizzaSelect.setText("BBQ Chicken");
                yield new BBQChicken(crust, getSelectedSize());
            }
            case "Meatzza" -> {
                pizzaSelect.setText("Meatzza");
                yield new Meatzza(crust, getSelectedSize());
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
     * Loads default toppings for a specific pizza type (non-customizable pizzas).
     *
     * @param pizzaType the type of pizza for which toppings are loaded
     */
    private void loadToppingsForPizzaType(String pizzaType) {
        List<Topping> toppings = switch (pizzaType) {
            case "Deluxe" -> Arrays.asList(Topping.SAUSAGE, Topping.PEPPERONI, Topping.GREENPEPPER,
                    Topping.ONION, Topping.MUSHROOM);
            case "BBQ Chicken" -> Arrays.asList(Topping.BBQCHICKEN, Topping.GREENPEPPER,
                    Topping.PROVOLONE, Topping.CHEDDAR);
            case "Meatzza" -> Arrays.asList(Topping.SAUSAGE, Topping.PEPPERONI, Topping.BEEF, Topping.HAM);
            case "Build Your Own" -> List.of(); // No default toppings
            default -> List.of();
        };

        ObservableList<String> toppingNames = FXCollections.observableArrayList();
        toppings.forEach(topping -> toppingNames.add(topping.toString()));
        selectedToppingsList.setItems(toppingNames);
        if (pizzaType.equals("Build Your Own")) {
            initializeToppingsList(); // Allow user to select custom toppings
        }
    }

    /**
     * Clears current topping selections in the list views.
     */
    private void clearToppings() {
        toppingsList.getItems().clear();
        selectedToppingsList.getItems().clear();
    }

    /**
     * Initializes available toppings list for customizable pizzas.
     */
    private void initializeToppingsList() {
        List<String> toppingNames = new ArrayList<>();
        for (Topping topping : Topping.values()) {
            toppingNames.add(topping.toString());
        }
        toppingsList.setItems(FXCollections.observableArrayList(toppingNames));
    }

    /**
     * Determines the currently selected size from radio buttons.
     *
     * @return the selected pizza size
     */
    private Size getSelectedSize() {
        return sizeLarge.isSelected() ? Size.LARGE : sizeMedium.isSelected() ? Size.MEDIUM : Size.SMALL;
    }

    /**
     * Configures button handlers for adding and removing toppings in customizable pizzas.
     */
    private void setupButtonHandlers() {
        addButton.setOnAction(event -> moveToppingToSelected());
        removeButton.setOnAction(event -> moveToppingToAvailable());
    }

    /**
     * Moves a topping from the available list to the selected toppings list.
     * Updates the pizza price accordingly.
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
     * Adds the currently selected pizza to the order manager's current order.
     * This method is triggered when the user confirms their pizza selection and wants to add it to their order.
     */
    @FXML
    public void addToOrder() {
        if (currentPizza != null) {
            int orderQuantity = Integer.parseInt(quantity.getText());
            for (int i = 0; i < orderQuantity; i++) {
                // Adds the pizza to the order manager's current order
                orderManager.getCurrentOrder().addPizza(currentPizza);
            }
            // System.out.println("Pizza added to current order: " + currentPizza.toString());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pizza successfully added to order.");
            alert.setTitle("Pizza Confirmation");
            alert.showAndWait();
            // Clear current pizza selection to prepare for a new one
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
