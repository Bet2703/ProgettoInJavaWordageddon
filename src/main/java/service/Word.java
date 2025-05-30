package service;

/**
 *
 * @author Nicols97
 */

public class Word {
    private final String text;
    private final int frequency;

    public Word(String text, int frequency) {
        this.text = text;
        this.frequency = frequency;
    }

    public String getText() {
        return text;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getFrequencyString() {
        return Integer.toString(frequency);
    }

    @Override
    public String toString() {
        return text + " (" + frequency + ")";
    }
}
