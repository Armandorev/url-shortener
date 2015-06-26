package benjamin.groehbiel.ch.api;

import benjamin.groehbiel.ch.shortener.ShortenerHandle;

import java.net.URI;

public class ShortenerResponse {

    public static ShortenerResponse summarise(ShortenerHandle shortenerHandle) {
        return new ShortenerResponse(shortenerHandle.getOriginalURI(), shortenerHandle.getShortenedURI(), shortenerHandle.getDescription());
    }

    private URI original;
    private URI shortened;
    private String description;

    public ShortenerResponse() {}

    public ShortenerResponse(URI original, URI shortened, String description) {
        this.original = original;
        this.shortened = shortened;
        this.description = description;
    }

    public ShortenerResponse(URI original, URI uri) {
        this(original, uri, "");
    }

    public URI getOriginal() {
        return original;
    }

    public URI getShortened() {
        return shortened;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ShortenerResponse) {
            ShortenerResponse that = (ShortenerResponse) o;
            return original.toString().equals(that.getOriginal().toString()) && shortened.toString().equals(that.getShortened().toString());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Original: " + this.original + " Shortened: " + this.shortened;
    }
}
