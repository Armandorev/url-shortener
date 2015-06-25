package benjamin.groehbiel.ch;

public class ShortenerException {

    private String message;

    protected ShortenerException() {}

    public ShortenerException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
