package it.w0rd.api.responses;

import it.w0rd.api.ShortenedUrl;

import java.net.URI;
import java.net.URISyntaxException;

public class ShortenedUrlResponse {

    public static ShortenedUrlResponse summarise(ShortenedUrl shortenedUrl) throws URISyntaxException {
        return new ShortenedUrlResponse(shortenedUrl.getOriginalURI(),
                shortenedUrl.getShortenedURI(),
                shortenedUrl.getHash(),
                shortenedUrl.getDescription(),
                shortenedUrl.getCreationTimestamp());
    }

    private URI original;
    private URI shortened;
    private String hash;
    private String description;
    private Long creationTimestamp;

    protected ShortenedUrlResponse () {}

    public ShortenedUrlResponse(URI original, URI shortened, String hash, String description, Long creationTimestamp) throws URISyntaxException {
        this.original = original;
        this.shortened = shortened;
        this.hash = hash;
        this.description = description;
        this.creationTimestamp = creationTimestamp;
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

    public Long getCreationTimestamp() {
        return creationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShortenedUrlResponse that = (ShortenedUrlResponse) o;

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
