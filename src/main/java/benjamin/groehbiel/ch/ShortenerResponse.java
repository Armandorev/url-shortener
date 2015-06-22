package benjamin.groehbiel.ch;

import java.net.URI;

public class ShortenerResponse {

    private URI original;
    private URI shortened;
    private Long index;

    protected ShortenerResponse() {}

    public ShortenerResponse(URI original, URI shortened) {
        this.original = original;
        this.shortened = shortened;
    }

    public ShortenerResponse(URI original, Long index) {
        this.original = original;
        this.index = index;
    }

    public URI getOriginal() {
        return original;
    }

    public URI getShortened() {
        return shortened;
    }

}
