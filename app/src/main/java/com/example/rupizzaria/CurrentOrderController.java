package pizzeria;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import javax.swing.*;
import java.util.List;

/**
 * Controller class for the current-order-view.fxml. Allows the user to check their current running order of pizza(s).
 *
 * @author Frank Garcia
 */
public class CurrentOrderController {
    /**
     * Text field for displaying the current order number.
     */
    @FXML
    private TextField orderNumDisplay;

    /**
     * List view displaying the pizzas currently added to the order.
     */
    @FXML
    private ListView currentlyOrderedPizzas;

    /**
     * Text fields for displaying the subtotal, sales tax, and total of the order.
     */
    @FXML
    private TextField subtotal, salesTax, orderTotal;

    /**
     * Buttons for placing the order, removing a pizza from the order, and clearing the order.
     */
    @FXML
    private Button placeButtonOrder, removePizzaButton, clearOrderButton;

    /**
     * Shared OrderManager instance to manage all orders.
     */
    private static OrderManager orders;

    /**
     * The current order being managed in the session.
     */
    private Order currentOrder;

    /**
     * Utility for tracking key events, used for handling shortcuts and other key-based actions.
     */
    private KeysTrackerUtil keysTrackerUtil = new KeysTrackerUtil();

    /**
     * Initializes the text fields and list view of the Current Order window at the time of opening the window.
     * @param orders The orders shared between all windows of the Pizzeria client.
     */
    public void initialize (OrderManager orders) {
        this.orders = orders;
        this.currentOrder = orders.getCurrentOrder();
        orderNumDisplay.setText(String.format("%d", currentOrder.getOrderNumber()));
        updateCurrentOrderFields();
    }

    /**
     * Updates all fields in the Current Order window.
     */
    private void updateCurrentOrderFields() {
        currentlyOrderedPizzas.getItems().clear();
        currentlyOrderedPizzas.getItems().addAll(currentOrder.getPizzas());
        double subTotalPrice = OrderManager.calculateSubtotal(currentOrder);
        subtotal.setText(String.format("%.2f", subTotalPrice));
        double salesTaxPrice = OrderManager.calculateSalesTax(subTotalPrice);
        salesTax.setText(String.format("%.2f", salesTaxPrice));
        double orderTotalPrice = OrderManager.calculateTotalAmount(subTotalPrice, salesTaxPrice);
        orderTotal.setText(String.format("%.2f", orderTotalPrice));
    }

    /**
     * Finalizes the order and adds the full order into the order history while initializing a new current order object
     * for the next order of pizzas. The order number is incremented.
     */
    @FXML
    private void placeOrder() {
        try {
            if (currentOrder.getPizzas().isEmpty()) throw new ArithmeticException();
            // System.out.println("Pizza order placed.");
            orders.placeCurrentOrder();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order has been successfully placed.");
            alert.setHeaderText("Order Confirmation");
            alert.showAndWait();

            currentOrder = orders.getCurrentOrder();
            orderNumDisplay.setText(String.format("%d", currentOrder.getOrderNumber()));
            updateCurrentOrderFields();
        }
        catch (ArithmeticException e) {
            // System.out.println("No pizzas to add to order.");
        }
    }

    /**
     * Removes the Pizza currently selected in the ListView.
     */
    @FXML
    private void removePizza() {
        try {
            if (currentOrder.getPizzas().isEmpty()) throw new ArithmeticException();
            List<Pizza> pizzas = orders.getCurrentOrder().getPizzas();
            Pizza selectedPizza = (Pizza) currentlyOrderedPizzas.getSelectionModel().getSelectedItem();

            this.currentOrder = orders.getCurrentOrder();
            orderNumDisplay.setText(String.format("%d", currentOrder.getOrderNumber()));
            pizzas.remove(selectedPizza);
            updateCurrentOrderFields();
        }
        catch (ArithmeticException e) {
            // System.out.println("No more pizzas to remove from order.");
        }
    }

    /**
     * Clears the order of all pizzas without them being added to the order history.
     */
    @FXML
    private void clearOrder() {
        try {
            if (currentOrder.getPizzas().isEmpty()) throw new ArithmeticException();
            orders.clearCurrentOrder();
            updateCurrentOrderFields();
        }
        catch (ArithmeticException e) {
            // System.out.println("No pizzas to add to order.");
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
}