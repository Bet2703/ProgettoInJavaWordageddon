/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

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

    /**
     * Opzione di livello "Facile".
     */
    @FXML
    private RadioButton rbEasy;

    /**
     * Opzione di livello "Medio".
     */
    @FXML
    private RadioButton rbMedium;

    /**
     * Opzione di livello "Difficile".
     */
    @FXML
    private RadioButton rbHard;

    /**
     * Pulsante per avviare il gioco con il livello selezionato.
     */
    @FXML
    private Button btnStart;

    /**
     * Pulsante per tornare alla schermata precedente.
     */
    @FXML
    private Button btnBack;

    /**
     * Etichetta per mostrare messaggi all'utente (es. errori o conferme).
     */
    @FXML
    private Label messageLabel;
    
    private ToggleGroup difficultyGroup;

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
     * Deve avviare il gioco utilizzando il livello selezionato.
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

        String difficulty = selected.getText();
        messageLabel.setText("Hai selezionato il livello: " + difficulty);
        
        //------------ CONTINUARE--------------------
        // Avvia il gioco con la difficoltà scelta...
    }

    /**
     * Metodo invocato quando l'utente preme il pulsante "Back".
     * Deve riportare l'utente alla schermata precedente.
     * 
     * @param event l'evento generato dal click sul pulsante
     */
    @FXML
    private void onBack(ActionEvent event) {
    }
}
