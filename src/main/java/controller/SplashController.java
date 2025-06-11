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
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @FXML
    private ImageView splashImage;

    @FXML
    private StackPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            splashImage.setImage(new Image(getClass().getResourceAsStream("/images/splash_screen.png")));
        } catch (Exception e) {
            System.err.println("Errore nel caricamento dell'immagine splash_screen.png.");
        }

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> playFadeOut());
        delay.play();
    }

    private void playFadeOut() {
        FadeTransition fade = new FadeTransition(Duration.seconds(1), rootPane);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        fade.setOnFinished(e -> {
            Stage splashStage = (Stage) rootPane.getScene().getWindow();
            splashStage.hide();
            launchLoginView();
        });

        fade.play();
    }

    private void launchLoginView() {
        javafx.application.Platform.runLater(() -> {
            try {
                Stage splashStage = (Stage) splashImage.getScene().getWindow();
                splashStage.close();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
                Scene loginScene = new Scene(loader.load());

                Stage loginStage = new Stage();
                loginStage.setTitle("Wordageddon");
                loginStage.getIcons().add(new javafx.scene.image.Image(
                        getClass().getResourceAsStream("/images/icon.png")
                ));
                loginStage.setScene(loginScene);
                loginStage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}