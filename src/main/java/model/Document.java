package model;

public class Document {
    private int id;
    private String text;
    private int wordCount;  // opzionale, utile per filtri

    public Document(int id, String text) {
        this.id = id;
        this.text = text;
        this.wordCount = countWords(text);
    }

    public Document(int id, String text, int wordCount) {
        this.id = id;
        this.text = text;
        this.wordCount = wordCount;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
        this.wordCount = countWords(text);
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    private int countWords(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        String[] words = text.trim().split("\\s+");
        return words.length;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", wordCount=" + wordCount +
                ", text='" + (text.length() > 30 ? text.substring(0, 30) + "..." : text) + '\'' +
                '}';
    }
}
