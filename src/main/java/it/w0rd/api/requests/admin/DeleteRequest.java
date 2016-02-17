package it.w0rd.api.requests.admin;

public class DeleteRequest {
    String hash;

    public DeleteRequest() {
    }

    public DeleteRequest(String hash) {
        this.hash = hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }
}
