package benjamin.groehbiel.ch.shortener;

import java.net.URI;

public class ShortenerHandle {
    private URI originalURI;
    private URI shortenedURI;
    private String hash;
    private String description;
    private Long nextUniqueId;

    protected ShortenerHandle() {}

    public ShortenerHandle(URI originalURI, URI shortenedURI, String hash, String desc, Long nextUniqueId) {
        this.originalURI = originalURI;
        this.shortenedURI = shortenedURI;
        this.hash = hash;
        this.description = desc;
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

    public String getDescription() {
        return description;
    }

    public Long getNextUniqueId() {
        return nextUniqueId;
    }
}
