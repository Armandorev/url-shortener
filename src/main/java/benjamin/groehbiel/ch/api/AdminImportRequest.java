package benjamin.groehbiel.ch.api;

public class AdminImportRequest {

    private Integer numberOfWords;
    private Integer wordLength;

    protected AdminImportRequest() {}

    public Integer getNumberOfWords() {
        return numberOfWords;
    }

    public void setNumberOfWords(Integer numberOfWords) {
        this.numberOfWords = numberOfWords;
    }

    public Integer getWordLength() {
        return wordLength;
    }

    public void setWordLength(Integer wordLength) {
        this.wordLength = wordLength;
    }
}
