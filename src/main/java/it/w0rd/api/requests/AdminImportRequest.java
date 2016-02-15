package it.w0rd.api.requests;

public class AdminImportRequest {

    private Integer numberOfWords;
    private Integer wordLength;

    public AdminImportRequest() {
    }

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
