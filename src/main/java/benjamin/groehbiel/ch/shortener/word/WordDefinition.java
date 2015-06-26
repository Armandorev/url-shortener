package benjamin.groehbiel.ch.shortener.word;

public class WordDefinition {
    private final String word;
    private final String description;

    public WordDefinition(String word, String description) {
        this.word = word;
        this.description = description;
    }

    public String getWord() {
        return word;
    }

    public String getDescription() {
        return description;
    }
}
