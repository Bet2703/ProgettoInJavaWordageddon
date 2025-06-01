package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller per la schermata di selezione del livello di difficoltà.
 * Gestisce l'interazione dell'utente con i pulsanti e le opzioni di livello.
 * 
 * I livelli disponibili sono: Facile, Medio, Difficile.
 * Questo controller viene utilizzato in combinazione con un file FXML.
 * 
 * @author Gruppo6
 */
public class LevelsController {

    @FXML
    private RadioButton rbEasy;
    @FXML
    private RadioButton rbMedium;
    @FXML
    private RadioButton rbHard;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnBack;
    @FXML
    private Label messageLabel;
    
    private ToggleGroup difficultyGroup;

    private static String selectedDifficulty;

    @FXML
    public void initialize() {
        // Crea il ToggleGroup e assegna i RadioButton al gruppo
        difficultyGroup = new ToggleGroup();

        rbEasy.setToggleGroup(difficultyGroup);
        rbMedium.setToggleGroup(difficultyGroup);
        rbHard.setToggleGroup(difficultyGroup);
    }
    
    /**
     * Metodo invocato quando l'utente preme il pulsante "Start".
     * Avvia il gioco con il livello selezionato e passa alla schermata del quiz.
     * 
     * @param event l'evento generato dal click sul pulsante
     */
    @FXML
    private void onStartGame(ActionEvent event) {
       RadioButton selected = (RadioButton) difficultyGroup.getSelectedToggle();

        if (selected == null) {
            messageLabel.setText("Seleziona un livello di difficoltà per iniziare.");
            return;
        }

        String difficulty = selected.getText();  // Prende direttamente il testo dal RadioButton selezionato
        messageLabel.setText("Hai selezionato il livello: " + difficulty);

        switch (difficulty) {
            case "Facile":
                selectedDifficulty = "EASY";
                break;
            case "Medio":
                selectedDifficulty = "MEDIUM";
                break;
            case "Difficile":
                selectedDifficulty = "HARD";
                break;
            default:
                selectedDifficulty = "EASY"; // fallback
                break;
        }
        try {
            // Carica la vista del quiz
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DocumentReadView.fxml"));
            Parent quizView = loader.load();
                        
            // Ottiene lo stage corrente
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            
            // Imposta la nuova scena
            Scene scene = new Scene(quizView);
            stage.setScene(scene);
            stage.show();

            // Se necessario, puoi passare la difficoltà al controller del quiz:
            // QuizController quizController = loader.getController();
            // quizController.setDifficulty(difficulty);
            
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Errore nel caricamento del quiz.");
        }
    }

    /**
     * Metodo invocato quando l'utente preme il pulsante "Back".
     * Torna alla schermata di login.
     * 
     * @param event l'evento generato dal click sul pulsante
     */
    @FXML
    private void onBack(ActionEvent event) {
        try {
            // Carica la vista del login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent loginView = loader.load();
            
            // Ottiene lo stage corrente
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            
            // Imposta la nuova scena
            Scene scene = new Scene(loginView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Errore nel tornare alla schermata di login.");
        }
    }

    /**
     * Returns the selected difficulty.
     * Used by the QuizController to pass the difficulty to the quiz.
     *
     * @return
     */
    public static String getDifficulty() {
        return selectedDifficulty;
    }
}