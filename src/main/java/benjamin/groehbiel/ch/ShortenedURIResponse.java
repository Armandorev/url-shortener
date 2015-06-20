package benjamin.groehbiel.ch;

import java.net.URI;

public class ShortenedURIResponse {

    private URI original;
    private Long index;

    protected ShortenedURIResponse() {}

    public ShortenedURIResponse(URI original, Long index) {
        this.original = original;
        this.index = index;
    }

    public URI getOriginal() {
        return original;
    }

    public Long getIndex() {
        return index;
    }
}
