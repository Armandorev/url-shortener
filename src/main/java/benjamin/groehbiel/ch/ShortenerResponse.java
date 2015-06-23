package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.ShortenerHandle;

import java.net.URI;

public class ShortenerResponse {

    public static ShortenerResponse summarise(ShortenerHandle shortenerHandle) {
        return new ShortenerResponse(shortenerHandle.getOriginalURI(), shortenerHandle.getShortenedURI());
    }

    private URI original;
    private URI shortened;

    public ShortenerResponse() {}

    public ShortenerResponse(URI original, URI shortened) {
        this.original = original;
        this.shortened = shortened;
    }

    public URI getOriginal() {
        return original;
    }

    public URI getShortened() {
        return shortened;
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
