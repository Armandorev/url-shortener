package benjamin.groehbiel.ch.shortener.admin;

import java.io.Serializable;

public class AdminShortenerRequest implements Serializable {

    private String url;
    private String hash;

    protected AdminShortenerRequest() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}