package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * La classe MainApp rappresenta il punto di ingresso principale dell'applicazione JavaFX.
 * Estende la classe {@link Application} e ha il compito di inizializzare e mostrare
 * lo stage primario dell'interfaccia grafica.
 * 
 * In particolare, carica il file FXML associato alla schermata di login e lo imposta
 * come scena iniziale dell'applicazione.
 * 
 * Autore: Gruppo6
 */
public class MainApp extends Application {

    /**
     * Metodo di avvio dell'applicazione.
     * Questo metodo viene invocato automaticamente dalla piattaforma JavaFX
     * dopo l'inizializzazione, e rappresenta il punto in cui l'interfaccia grafica viene caricata.
     *
     * Viene configurata anche una semplice icona per l'applicazione, situata nella cartella resources/images.
     *
     * @param primaryStage lo stage principale dell'applicazione, ovvero la finestra principale.
     *                     È su questo oggetto che viene impostata la scena iniziale.
     *
     * @throws Exception se si verifica un errore durante il caricamento del file FXML.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SplashScreen.fxml"));
        Parent root = loader.load();

        primaryStage.getIcons().add(new javafx.scene.image.Image(
                getClass().getResourceAsStream("/images/icon.png")
        ));

        Stage splashStage = new Stage();
        splashStage.initStyle(StageStyle.UNDECORATED);
        splashStage.initStyle(StageStyle.TRANSPARENT);
        splashStage.centerOnScreen();

        Scene scene = new Scene(root);
        scene.setFill(null);

        splashStage.setScene(scene);
        splashStage.show();
    }
    /**
     * Metodo main dell'applicazione Java.
     * Questo metodo viene eseguito all'avvio del programma e richiama il metodo {@code launch}
     * per inizializzare il framework JavaFX e avviare l'interfaccia grafica.
     *
     * @param args eventuali argomenti da riga di comando (non utilizzati in questa applicazione).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
