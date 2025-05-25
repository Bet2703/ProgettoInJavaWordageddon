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
 * Controller class for managing the user interface where questions are presented.
 * Handles answer selection, submission, skipping, and displaying feedback.
 * 
 * This controller is associated with a JavaFX FXML layout.
 * 
 * @author Gruppo6
 */
public class QuestionsController {

    /**
     * Label used to display the question text.
     */
    @FXML
    private Label questionLabel;

    /**
     * Radio button for answer option A.
     */
    @FXML
    private RadioButton optionA;

    /**
     * Radio button for answer option B.
     */
    @FXML
    private RadioButton optionB;

    /**
     * Radio button for answer option C.
     */
    @FXML
    private RadioButton optionC;

    /**
     * Radio button for answer option D.
     */
    @FXML
    private RadioButton optionD;
    
    private ToggleGroup answerGroup;

    @FXML
    public void initialize() {
        // Crea il ToggleGroup e assegna i RadioButton al gruppo
        answerGroup = new ToggleGroup();

        optionA.setToggleGroup(answerGroup);
        optionB.setToggleGroup(answerGroup);
        optionC.setToggleGroup(answerGroup);
        optionD.setToggleGroup(answerGroup);
    }

    /**
     * Button used to submit the selected answer.
     */
    @FXML
    private Button submitButton;

    /**
     * Button used to skip the current question.
     */
    @FXML
    private Button skipButton;

    /**
     * Label used to show feedback to the user after submitting an answer.
     */
    @FXML
    private Label feedbackLabel;

    /**
     * Handles the event triggered when the user clicks the "Submit" button.
     * This method should validate the selected answer and display feedback.
     * 
     * @param event the ActionEvent triggered by clicking the submit button
     */
    @FXML
    private void onSubmitAnswer(ActionEvent event) {
    }

    /**
     * Handles the event triggered when the user clicks the "Skip" button.
     * This method should proceed to the next question without evaluation.
     * 
     * @param event the ActionEvent triggered by clicking the skip button
     */
    @FXML
    private void onSkipQuestion(ActionEvent event) {
    }

}
