package service;

import java.util.List;

/**
 * Represents a Question with its text, correct answer, and a list of possible options.
 * This class provides getter and setter methods for managing the question text,
 * correct answer, and the options available to the question.
 */
public class Question {

    /**
     * Stores the text of the question being asked.
     * This variable represents the main content of a question,
     * which could be a prompt or statement that requires a user response.
     */
    private String questionText;

    /**
     * Stores the correct answer to the question.
     * This variable represents the expected or accurate response
     * that validates a user's input against the question.
     */
    private String correctAnswer;

    /**
     * Represents a list of possible answer options for the question.
     * Each option is a string and provides users with pre-defined choices
     * for answering the question.
     */
    private List<String> options;

    /**
     * Retrieves the text of the question.
     *
     * @return the text of the question as a String.
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Updates the text of the question.
     *
     * @param questionText the new text of the question to be set
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    /**
     * Retrieves the correct answer associated with a question.
     *
     * @return the correct answer as a String.
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Sets the correct answer for the question.
     *
     * @param correctAnswer the correct answer to be set for the question
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * Retrieves the list of possible answer options for the question.
     *
     * @return a list of strings representing the possible answer options.
     */
    public List<String> getOptions() {
        return options;
    }

    /**
     * Updates the list of possible answer options for the question.
     * Replaces the current list of options with the provided list.
     *
     * @param options the new list of possible answer options to set
     */
    public void setOptions(List<String> options) {
        this.options = options;
    }
}