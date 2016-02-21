package it.w0rd.api.auth;

public interface AuthenticationProvider {
    boolean isAdministrator(final String username, final String password);
}
