package it.w0rd.api;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.net.URI;
import java.net.URISyntaxException;

public class ShortenedUrl {

    private URI originalURI;
    private String hash;
    private String description;

    protected ShortenedUrl() {
    }

    public ShortenedUrl(URI originalURI, String hash, String desc) {
        this.originalURI = originalURI;
        this.hash = hash;
        this.description = desc;
    }

    public URI getOriginalURI() {
        return originalURI;
    }

    @JsonIgnore
    public URI getShortenedURI() throws URISyntaxException {
        // TODO: use spring values, how?
        return new URI(System.getProperty("app.protocol") + "://" + System.getProperty("app.host") + "/" + this.hash);
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

        ShortenedUrl that = (ShortenedUrl) o;

        if (originalURI != null ? !originalURI.equals(that.originalURI) : that.originalURI != null) return false;
        return !(hash != null ? !hash.equals(that.hash) : that.hash != null);
    }
}
