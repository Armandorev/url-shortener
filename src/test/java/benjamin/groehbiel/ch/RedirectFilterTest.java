package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.ShortenerHandle;
import benjamin.groehbiel.ch.shortener.ShortenerService;
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
import java.net.URI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class RedirectFilterTest {

    ShortenerService shortenerService;
    RedirectFilter redirectFilter;
    FilterChain filterChain;

    @Before
    public void setUp() throws IOException, ServletException {
        shortenerService = mock(ShortenerService.class);
        redirectFilter = new RedirectFilter(shortenerService);
        filterChain = mock(FilterChain.class);
    }

    @Test
    public void shouldPassApiCalls() throws Throwable {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/all");
        MockHttpServletResponse response = new MockHttpServletResponse();

        redirectFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, times(1))
                .doFilter(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));
    }

    @Test
    public void shouldPassAdminRootProject() throws Throwable {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin");
        MockHttpServletResponse response = new MockHttpServletResponse();

        redirectFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, times(1))
                .doFilter(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));
    }

    @Test
    public void shouldPassAdminResourcesProject() throws Throwable {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/static/admin/index.html");
        MockHttpServletResponse response = new MockHttpServletResponse();

        redirectFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, times(1))
                .doFilter(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));
    }

    @Test
    public void shouldRedirectHash() throws Throwable {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/wave");
        MockHttpServletResponse response = new MockHttpServletResponse();

        Mockito.when(shortenerService.expand("wave")).thenReturn(new ShortenerHandle(new URI("http://www.test.com"), "wave", "Description"));

        redirectFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, times(0))
                .doFilter(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));
    }

    @Test
    public void shouldRedirectTo404IfHashNotExistent() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/wave");
        MockHttpServletResponse response = new MockHttpServletResponse();

        redirectFilter.doFilter(request, response, filterChain);

        assertThat(response.getRedirectedUrl(), equalTo("404.html?hash=wave"));
        //TODO: enforce 404 error code instead of 302
    }

}