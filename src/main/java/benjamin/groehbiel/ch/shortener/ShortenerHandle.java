package benjamin.groehbiel.ch.shortener;

import java.net.URI;

public class ShortenerHandle {
    private URI originalURI;
    private URI shortenedURI;
    private String hash;
    private Long nextUniqueId;

    protected ShortenerHandle() {}

    public ShortenerHandle(URI originalURI, URI shortenedURI, String hash, Long nextUniqueId) {
        this.originalURI = originalURI;
        this.shortenedURI = shortenedURI;
        this.hash = hash;
        this.nextUniqueId = nextUniqueId;
    }

    public URI getOriginalURI() {
        return originalURI;
    }

    public URI getShortenedURI() {
        return shortenedURI;
    }

    public String getHash() {
        return hash;
    }

    public Long getNextUniqueId() {
        return nextUniqueId;
    }
}
