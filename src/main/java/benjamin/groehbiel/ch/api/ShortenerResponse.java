package benjamin.groehbiel.ch.api;

import benjamin.groehbiel.ch.shortener.ShortenerHandle;

import java.net.URI;
import java.net.URISyntaxException;

public class ShortenerResponse {

    public static ShortenerResponse summarise(ShortenerHandle shortenerHandle) throws URISyntaxException {
        return new ShortenerResponse(shortenerHandle.getOriginalURI(),
                shortenerHandle.getShortenedURI(),
                shortenerHandle.getHash(),
                shortenerHandle.getDescription());
    }

    private URI original;
    private URI shortened;
    private String hash;
    private String description;

    public ShortenerResponse() {}

    public ShortenerResponse(URI original, URI shortened, String hash, String description) throws URISyntaxException {
        this.original = original;
        this.shortened = shortened;
        this.hash = hash;
        this.description = description;
    }

    public URI getOriginal() {
        return original;
    }

    public URI getShortened() throws URISyntaxException {
        return this.shortened;
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

        ShortenerResponse that = (ShortenerResponse) o;

        if (!original.equals(that.original)) return false;
        return hash.equals(that.hash);
    }

    @Override
    public int hashCode() {
        int result = original.hashCode();
        result = 31 * result + hash.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Original: " + this.original + " Shortened: " + this.shortened;
    }
}
