package model;

public class Document {
    private int id;
    private String text;
    private String title;
    private String difficulty;

    public Document(int id, String text, String title, String difficulty) {
        this.id = id;
        this.text = text;
        this.title = title;
        this.difficulty = difficulty;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    private int countWords(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        String[] words = text.trim().split("\\P{L}+");
        return words.length;
    }

    @Override
    public String toString() {
        return "Document {" +
                "id=" + id +
                ", title=" + title +
                ", difficulty='" + difficulty +
                ", text='" + (text.length() > 30 ? text.substring(0, 30) + "..." : text) + '\'' +
                '}';
    }
}
