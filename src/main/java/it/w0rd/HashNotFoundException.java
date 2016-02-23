package it.w0rd;

public class HashNotFoundException extends RuntimeException {

    private String requestedUrl;

    public HashNotFoundException(String requestedUrl) {
        this.requestedUrl = requestedUrl;
    }

    public String getRequestedUrl() {
        return this.requestedUrl;
    }

}
