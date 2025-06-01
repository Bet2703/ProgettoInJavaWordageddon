package service;

/**
 *
 * @author Nicols97
 */

public class Word {
    private final String text;
    private final int frequency;

    /**
     * Creates a new Word object with the specified text and frequency.
     *
     * @param text
     * @param frequency
     */
    public Word(String text, int frequency) {
        this.text = text;
        this.frequency = frequency;
    }

    /**
     * Returns the text of the Word.
     *
     * @return the text of the Word, as a String object.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the frequency of the Word.
     *
     * @return the frequency of the Word, as an integer.
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Returns the frequency of the Word as a String.
     *
     * @return the frequency of the Word, as a String object.
     */
    public String getFrequencyString() {
        return Integer.toString(frequency);
    }

    /**
     * Returns a string representation of the Word, including the text and frequency.
     *
     * @return a formatted string with the text and frequency of the Word.
     */
    @Override
    public String toString() {
        return text + " (" + frequency + ")";
    }
}
