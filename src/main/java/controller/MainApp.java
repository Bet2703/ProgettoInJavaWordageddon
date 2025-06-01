package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Gruppo6
 */

/**
 * App Main class.
 */
public class MainApp extends Application {

    /**
     * Start the application.
     * This method is called after the init method has returned, and after the system is ready for the application to begin running.
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     *
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carica il file Login.fxml dalla cartella view
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/Login.fxml"));
        Parent root = loader.load();


        // Imposta titolo e scena
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Main method.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args); // Avvia l'applicazione JavaFX
    }
}
