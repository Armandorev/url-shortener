package it.w0rd.api;

public interface AuthenticationProvider {
 boolean isAdmin(final String username, final String password);
}
