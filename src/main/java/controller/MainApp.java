package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The MainApp class serves as the entry point for the JavaFX application.
 * It extends the Application class and is responsible for initializing
 * and displaying the primary stage of the application.
 *
 * @author Gruppo6
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

        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/Login.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * The main method serves as the entry point for the Java application.
     * It initializes and launches the JavaFX application by invoking the `launch` method.
     *
     * @param args the command-line arguments passed to the application.
     */
    public static void main(String[] args) {launch(args);}
}
