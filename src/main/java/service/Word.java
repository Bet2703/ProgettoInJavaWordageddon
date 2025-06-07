package service;

/**
 * Represents a word with its associated frequency.
 * This class provides methods to retrieve the text of the word,
 * its frequency, and a string representation of both.
 *
 * @author Gruppo6
 */
public class Word {
    private final String text;
    private final int frequency;

    /**
     * Constructs a Word object with the specified text and frequency.
     *
     * @param text the text of the word
     * @param frequency the frequency of the word
     */
    public Word(String text, int frequency) {
        this.text = text;
        this.frequency = frequency;
    }

    /**
     * Returns the text of the Word.
     *
     * @return the text of the Word as a String.
     */
    public String getText() {
        return text;
    }

    /**
     * Retrieves the frequency associated with the Word.
     *
     * @return the frequency of the Word as an integer.
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Converts the frequency of the Word into a string representation.
     *
     * @return the string representation of the frequency.
     */
    public String getFrequencyString() {
        return Integer.toString(frequency);
    }

    /**
     * Returns a string representation of the Word object, including its text
     * and frequency in the format "text (frequency)".
     *
     * @return the string representation of the Word object.
     */
    @Override
    public String toString() {
        return text + " (" + frequency + ")";
    }
}
