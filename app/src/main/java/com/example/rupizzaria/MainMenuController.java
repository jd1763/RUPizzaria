package pizzeria;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller class for main-menu-view.fxml. The main menu allows the user to navigate to order Chicago style pizzas,
 * New York style pizzas, view the current running order, or view their order history.
 *
 * @author Frank Garcia
 * @author Jorgeluis Done
 */
public class MainMenuController {

    /**
     * Shared OrderManager instance to manage orders across the application.
     */
    private static OrderManager orderManager;

    /**
     * Utility for tracking key events, used for handling shortcuts and other key-based actions.
     */
    private KeysTrackerUtil keysTrackerUtil = new KeysTrackerUtil();

    /**
     * Resets the orders by initializing a new instance of OrderManager.
     * This method is useful for clearing all existing orders and starting fresh.
     */
    protected static void resetOrders() {
        orderManager = new OrderManager();
    }

    /**
     * Changes the background color of a button when it is hovered over by the mouse.
     * Sets the button's background color to a darker shade to indicate hover.
     *
     * @param mouseEvent the MouseEvent triggered when the mouse hovers over the button.
     */
    @FXML
    private void changeBackgroundOnHover(MouseEvent mouseEvent) {
        Button hoveredButton = (Button) mouseEvent.getSource();
        hoveredButton.setStyle("-fx-background-color: A9A9A9");
    }

    /**
     * Reverts the background color of a button after the mouse hover ends.
     * Resets the button's background color to its original shade.
     *
     * @param mouseEvent the MouseEvent triggered when the mouse exits the button area.
     */
    @FXML
    private void changeBackgroundAfterHover(MouseEvent mouseEvent) {
        Button hoveredButton = (Button) mouseEvent.getSource();
        hoveredButton.setStyle("-fx-background-color: D3D3D3");
    }


    /**
     * Opens the Chicago Style Pizza view.
     */
    @FXML
    private void openChicagoStyle() {
        try {
            FXMLLoader chicagoLoader = new FXMLLoader(MainMenuController.class.getResource("/pizzeria/ChicagoStylePizza-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(chicagoLoader.load());

            ChicagoStyleMenuController chicagoController = chicagoLoader.getController();
            chicagoController.setOrderManager(getOrderManagerInstance()); // Pass the shared OrderManager instance
            chicagoController.initialize();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Chicago Style Pizza Menu");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading Chicago Style Pizza view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Opens the New York Style Pizza view.
     */
    @FXML
    private void openNYStyle() {
        try {
            FXMLLoader nyStyleLoader = new FXMLLoader(MainMenuController.class.getResource("/pizzeria/NYStylePizza-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(nyStyleLoader.load());

            NYPizzaStyleMenuController nyStyleController = nyStyleLoader.getController();
            nyStyleController.setOrderManager(getOrderManagerInstance()); // Pass the shared OrderManager instance
            nyStyleController.initialize();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("New York Style Pizza Menu");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading New York Style Pizza view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Opens the Current Order view.
     */
    @FXML
    private void openCurrentOrder() {
        try {
            FXMLLoader currOrderLoader = new FXMLLoader(MainMenuController.class.getResource(
                    "/pizzeria/current-order-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(currOrderLoader.load());

            CurrentOrderController currOrderController = currOrderLoader.getController();
            currOrderController.initialize(getOrderManagerInstance());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Current Order");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading Current Order view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Opens the Orders Placed view.
     */
    @FXML
    private void openOrdersPlaced() {
        try {
            FXMLLoader ordersPlacedLoader = new FXMLLoader(MainMenuController.class.getResource(
                    "/pizzeria/orders-placed-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(ordersPlacedLoader.load());

            OrdersPlacedController ordersPlacedController = ordersPlacedLoader.getController();
            ordersPlacedController.initialize(getOrderManagerInstance());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Orders Placed");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading Orders Placed view: " + e.getMessage());
            e.printStackTrace();
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
     * Retrieves the shared OrderManager instance.
     *
     * @return The shared OrderManager instance.
     */
    private OrderManager getOrderManagerInstance() {
        if (orderManager == null) {
            orderManager = new OrderManager();
        }
        return orderManager;
    }
}
