package it.w0rd.api.requests.user;

import java.io.Serializable;

public class CreateRequest implements Serializable {

    private String url;

    protected CreateRequest() {
    }

    public CreateRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
