package it.w0rd.api.auth;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SingleAdminAuthenticationTest {

    @Test(expected = AdminAuthenticationCredentialsError.class)
    public void shouldThrowIfUsernameAndPasswordNotSet() throws AdminAuthenticationCredentialsError {
        new SingleAdminAuthentication(null, null);
    }

    @Test(expected = AdminAuthenticationCredentialsError.class)
    public void shouldThrowIfUsernameIsEmpty() throws AdminAuthenticationCredentialsError {
        new SingleAdminAuthentication("", "");
    }

    @Test
    public void shouldSuccessfullyAuthenticateAdmin() throws AdminAuthenticationCredentialsError {
        SingleAdminAuthentication singleAdminAuthentication = new SingleAdminAuthentication("test", "test");

        assertThat(singleAdminAuthentication.isAdministrator("test", "test"), equalTo(true));
    }

    @Test
    public void shouldFailAuthenticateAdmin() throws AdminAuthenticationCredentialsError {
        SingleAdminAuthentication singleAdminAuthentication = new SingleAdminAuthentication("test", "test");

        assertThat(singleAdminAuthentication.isAdministrator("test", "wrong password"), equalTo(false));
    }

}
