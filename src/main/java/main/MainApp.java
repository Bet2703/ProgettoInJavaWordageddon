package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

/**
 * La classe MainApp rappresenta il punto di ingresso principale dell'applicazione JavaFX.
 * Estende la classe {@link Application} e ha il compito di inizializzare e mostrare
 * lo stage primario dell'interfaccia grafica.
 * 
 * Funzionalità:
 * - Carica lo SplashScreen come prima scena.
 * - Imposta uno stile trasparente e non decorato per un effetto moderno.
 * - Aggiunge un'icona personalizzata alla finestra.
 * 
 * Autore: Gruppo6
 */
public class MainApp extends Application {

    /**
     * Metodo chiamato automaticamente all'avvio dell'applicazione JavaFX.
     * 
     * @param primaryStage lo stage principale fornito da JavaFX, non usato direttamente
     *                     qui poiché viene usato uno stage separato per la splash screen.
     * @throws Exception in caso di errore durante il caricamento del file FXML.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carica il file FXML dello SplashScreen dalla cartella view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SplashScreen.fxml"));
        Parent root = loader.load(); // Carica l'albero dei nodi JavaFX a partire dal file FXML

        // Crea un nuovo Stage per lo splash screen (indipendente da primaryStage)
        Stage splashStage = new Stage();

        // Rimuove le decorazioni della finestra (titolo, bordi)
        splashStage.initStyle(StageStyle.UNDECORATED);
        
        // Imposta la finestra con stile trasparente (utile per effetti grafici)
        splashStage.initStyle(StageStyle.TRANSPARENT);
        
        // Centra lo splash screen al centro dello schermo
        splashStage.centerOnScreen();

        // Aggiunge un'icona personalizzata alla finestra (si trova in /resources/images)
        splashStage.getIcons().add(new javafx.scene.image.Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/images/icon.png"))
        ));

        // Crea una nuova scena a partire dal root caricato da FXML
        Scene scene = new Scene(root);
        
        // Rende lo sfondo della scena trasparente per effetti grafici moderni
        scene.setFill(null);

        // Imposta la scena nello stage
        splashStage.setScene(scene);

        // Mostra lo splash screen all’utente
        splashStage.show();
    }

    /**
     * Metodo main dell'applicazione Java.
     * Questo è il punto di ingresso dell'applicazione ed è necessario per avviare JavaFX
     * tramite il metodo statico {@code launch}, che invoca il metodo {@code start}.
     *
     * @param args eventuali argomenti da riga di comando (non utilizzati in questa applicazione).
     */
    public static void main(String[] args) {
        launch(args); // Avvia l'applicazione JavaFX (chiama automaticamente start)
    }
}
