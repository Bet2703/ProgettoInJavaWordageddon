package controller;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller per la schermata di splash (schermata iniziale)
 */
public class SplashController implements Initializable {

    @FXML
    private ImageView splashImage; // Immagine visualizzata nello splash screen

    @FXML
    private StackPane rootPane; // Pannello radice della scena

    /**
     * Metodo chiamato durante l'inizializzazione del controller
     * @param location URL utilizzato per risolvere i percorsi relativi per l'oggetto radice
     * @param resources Risorse utilizzate per localizzare l'oggetto radice
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Carica l'immagine dello splash screen dalla cartella resources
            splashImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/splash_screen.png"))));
        } catch (Exception e) {
            System.err.println("Errore nel caricamento dell'immagine splash_screen.png.");
        }

        // Crea una pausa di 2 secondi prima di avviare l'animazione di fade out
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> playFadeOut());
        delay.play();
    }

    /**
     * Esegue l'animazione di dissolvenza in uscita (fade out)
     */
    private void playFadeOut() {
        // Crea un'animazione di fade della durata di 1 secondo
        FadeTransition fade = new FadeTransition(Duration.seconds(1), rootPane);
        fade.setFromValue(1.0); // Opacità iniziale (completamente visibile)
        fade.setToValue(0.0);   // Opacità finale (completamente trasparente)

        // Quando l'animazione è completata, nasconde la finestra e lancia la view di login
        fade.setOnFinished(e -> {
            Stage splashStage = (Stage) rootPane.getScene().getWindow();
            splashStage.hide();
            launchLoginView();
        });

        fade.play(); // Avvia l'animazione
    }

    /**
     * Carica e mostra la schermata di login
     */
    private void launchLoginView() {
        // Esegue l'operazione sul thread JavaFX
        javafx.application.Platform.runLater(() -> {
            try {
                // Chiude la finestra dello splash screen
                Stage splashStage = (Stage) splashImage.getScene().getWindow();
                splashStage.close();

                // Carica il file FXML per la schermata di login
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
                Scene loginScene = new Scene(loader.load());

                // Crea e configura la nuova finestra per il login
                Stage loginStage = new Stage();
                loginStage.setTitle("Wordageddon"); // Imposta il titolo della finestra
                // Aggiunge l'icona dell'applicazione
                loginStage.getIcons().add(new javafx.scene.image.Image(
                        Objects.requireNonNull(getClass().getResourceAsStream("/images/icon.png"))
                ));
                loginStage.setScene(loginScene); // Imposta la scena
                loginStage.show(); // Mostra la finestra

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}