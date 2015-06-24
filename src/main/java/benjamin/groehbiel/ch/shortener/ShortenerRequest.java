package benjamin.groehbiel.ch.shortener;

import java.io.Serializable;

public class ShortenerRequest implements Serializable{

    private String url;

    protected ShortenerRequest() {}

    public ShortenerRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}