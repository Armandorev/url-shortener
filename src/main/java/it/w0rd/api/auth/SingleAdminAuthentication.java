package it.w0rd.api.auth;

public class SingleAdminAuthentication implements AuthenticationProvider {

    private final String username;
    private final String password;

    public SingleAdminAuthentication(String username, String password) throws AdminAuthenticationCredentialsError {
        if ( (username == null || username.isEmpty()) || password == null) throw new AdminAuthenticationCredentialsError();

        this.username = username;
        this.password = password;
    }

    @Override
    public boolean isAdministrator(String username, String password) {
        if (username.equals(this.username) && password.equals(this.password)) return true;
        return false;
    }

}
