package it.w0rd.api.requests;

public class AdminDeleteRequest {
    String hash;

    public AdminDeleteRequest() {
    }

    public AdminDeleteRequest(String hash) {
        this.hash = hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }
}
