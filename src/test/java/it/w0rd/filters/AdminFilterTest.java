package it.w0rd.filters;

import it.w0rd.api.auth.AuthenticationProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.Base64;

public class AdminFilterTest {
    AdminFilter adminFilter;
    FilterChain filterChain;
    AuthenticationProvider authenticationProvider;

    @Before
    public void setUp() throws IOException, ServletException {
        authenticationProvider = mock(AuthenticationProvider.class);
        adminFilter = new AdminFilter(authenticationProvider);
        filterChain = mock(FilterChain.class);
    }

    @Test
    public void shouldBlockUsersWithoutAuthenticationOnAdminRoot() throws Throwable {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/");
        MockHttpServletResponse response = new MockHttpServletResponse();

        adminFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, times(0))
                .doFilter(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));

        assertThat(response.getStatus(), equalTo(HttpServletResponse.SC_UNAUTHORIZED));
        assertThat(response.getHeader("WWW-Authenticate"), equalTo("Basic"));
    }

    @Test
    public void shouldBlockUsersWithoutAuthenticationOnAdminSomething() throws Throwable {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/index.html");
        MockHttpServletResponse response = new MockHttpServletResponse();

        adminFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, times(0))
                .doFilter(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));

        assertThat(response.getStatus(), equalTo(HttpServletResponse.SC_UNAUTHORIZED));
        assertThat(response.getHeader("WWW-Authenticate"), equalTo("Basic"));
    }

    @Test
    public void shouldLetUsersWithCorrectCredentialsPass() throws Throwable {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin/");
        final String user = "test";
        final String password = "passwd";
        final String authKey = new String(Base64.getEncoder().encode((user + ":" + password).getBytes()));
        request.addHeader("Authorization", "Basic " + authKey);
        Mockito.when(authenticationProvider.isAdministrator(Mockito.eq(user), Mockito.eq(password))).thenReturn(true);

        MockHttpServletResponse response = new MockHttpServletResponse();

        adminFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, times(1))
                .doFilter(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));
        Mockito.verify(authenticationProvider, times(1)).isAdministrator(user, password);
        assertThat(response.getStatus(), equalTo(HttpServletResponse.SC_OK));
    }

    @Test
    public void shouldNotBlockOnNonAdminPage() throws Throwable {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/index.html");
        MockHttpServletResponse response = new MockHttpServletResponse();

        adminFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, times(1))
                .doFilter(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));
    }

}
