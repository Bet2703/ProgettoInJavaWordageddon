package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CreditViewController {
    /**
    * Gestisce il click sul pulsante per tornare al menu principale.
    * <p>
    * Carica la scena del menu principale e la imposta nello stage corrente.
    * </p>
    *
    * @param event l'evento di azione generato dal click sul pulsante
    */
    @FXML
    private void onBackToMenuClicked(ActionEvent event) {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
          
        }
    }
}
