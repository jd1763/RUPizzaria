package pizzeria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main application class for the RU Pizza application.
 * This class initializes and launches the main GUI for the pizza ordering system.
 * @author Frank Garcia
 */
public class RUPizzaApplication extends Application {

    /**
     * The entry point for the JavaFX application. This method is called by the
     * JavaFX framework to start the application and load the main menu view.
     *
     * @param stage the primary stage for this application, onto which the application scene is set.
     * @throws IOException if there is an error loading the FXML resource for the main menu view.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RUPizzaApplication.class.getResource("main-menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("RU Pizza");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method for the application, which launches the JavaFX application.
     *
     * @param args the command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch();
    }
}