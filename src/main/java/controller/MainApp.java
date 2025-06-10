package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
     * @param primaryStage lo stage principale dell'applicazione, ovvero la finestra principale.
     *                     È su questo oggetto che viene impostata la scena iniziale.
     *
     * @throws Exception se si verifica un errore durante il caricamento del file FXML.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Caricamento del layout della schermata di login da file FXML
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/Login.fxml"));
        Parent root = loader.load();

        // Impostazione del titolo della finestra
        primaryStage.setTitle("Login");

        // Creazione e impostazione della scena principale
        primaryStage.setScene(new Scene(root));
        primaryStage.show(); // Visualizzazione dello stage
    }

    /**
     * Metodo main dell'applicazione Java.
     * Questo metodo viene eseguito all'avvio del programma e richiama il metodo {@code launch}
     * per inizializzare il framework JavaFX e avviare l'interfaccia grafica.
     *
     * @param args eventuali argomenti da riga di comando (non utilizzati in questa applicazione).
     */
    public static void main(String[] args) {
        launch(args); // Avvio dell'applicazione JavaFX
    }
}
