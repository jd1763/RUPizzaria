package pizzeria;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for orders-placed-view.fxml. Allows the user to view order history of all orders placed.
 *
 * @author Frank Garcia
 */
public class OrdersPlacedController {
    /**
     * Button for canceling the current order.
     */
    @FXML
    private Button cancelOrderButton;

    /**
     * Text field for displaying the total price of the current order.
     */
    @FXML
    private TextField totalPriceTextField;

    /**
     * List view displaying all items in the current order.
     */
    @FXML
    private ListView orderListView;

    /**
     * Menu button for selecting different orders.
     */
    @FXML
    private MenuButton orderSelector;

    /**
     * Shared OrderManager instance for managing all orders.
     */
    private static OrderManager orders;

    /**
     * The current order being processed or viewed.
     */
    private Order currOrder;

    /**
     * Utility for tracking key events, used for handling shortcuts and other key-based actions.
     */
    private KeysTrackerUtil keysTrackerUtil = new KeysTrackerUtil();

    /**
     * Initializes the window with the order selection button being initialized with the orders currently placed.
     * @param orders The complete orders object initialized from the MainMenuController class.
     */
    public void initialize(OrderManager orders) {
        this.orders = orders;
        updateSelector();
    }

    /**
     * Cancels the currently selected order, which is then removed from the order history.
     */
    public void cancelOrder() {
        try {
            orders.cancelOrder(currOrder.getOrderNumber());
            updateOrdersPlacedWindow();
        }
        catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No Order was selected.");
            alert.showAndWait();
        }
    }

    /**
     * Saves current orders to a file. Displays an error if no orders are present.
     */
    public void saveOrders() {
        try {
            if (orders.getAllOrders().isEmpty()) throw new Exception(); // Check if there are orders
            orders.saveOrdersToFile(); // Save orders to file
            this.orders = new OrderManager(); // Reset orders
            MainMenuController.resetOrders(); // Reset shared order data
            updateOrdersPlacedWindow(); // Update display
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No Order to save.");
            alert.showAndWait();
        }
    }

    /**
     * Updates the Orders Placed window and resets the fields according to the current state of the orders object.
     */
    private void updateOrdersPlacedWindow() {
        updateSelector();
        orderSelector.setText("Select Order #");
        orderListView.getItems().clear();
        totalPriceTextField.clear();
    }

    /**
     * Updates the order selection button in order to contain all orders placed on the pizzeria application.
     */
    private void updateSelector() {
        List<Order> pizzas = orders.getAllOrders();
        int orderNumber;
        String orderStr;

        orderSelector.getItems().clear();

        for (Order order : pizzas) {
            orderNumber = order.getOrderNumber();
            orderStr = "Order #" + orderNumber;
            MenuItem orderSelection = new MenuItem(orderStr);
            orderSelection.setOnAction(event -> selectOrder(order));
            orderSelector.getItems().add(orderSelection);
        }
    }

    /**
     * Updates the orderListView to display the list of pizzas ordered in the selected order and updates the total
     * price text field with the total price of the current order.
     * @param order The selected order object whose contents will be used to update the orderListView and total price.
     */
    private void selectOrder(Order order) {
        this.currOrder = order;
        orderSelector.setText("Order #" + this.currOrder.getOrderNumber());
        double totalPrice = OrderManager.getTotalAmount(currOrder);

        orderListView.getItems().clear();
        orderListView.getItems().addAll(currOrder.getPizzas());
        totalPriceTextField.setText(String.format("%.2f", totalPrice));
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
