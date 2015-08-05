package benjamin.groehbiel.ch.shortener;

import java.net.URI;

public class ShortenerHandle {
    private URI originalURI;
    private URI shortenedURI;
    private String hash;
    private String description;

    protected ShortenerHandle() {
    }

    public ShortenerHandle(URI originalURI, URI shortenedURI, String hash, String desc) {
        this.originalURI = originalURI;
        this.shortenedURI = shortenedURI;
        this.hash = hash;
        this.description = desc;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShortenerHandle that = (ShortenerHandle) o;

        if (originalURI != null ? !originalURI.equals(that.originalURI) : that.originalURI != null) return false;
        return !(hash != null ? !hash.equals(that.hash) : that.hash != null);

    }
}
