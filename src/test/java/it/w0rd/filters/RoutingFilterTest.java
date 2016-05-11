package it.w0rd.filters;

import it.w0rd.api.ShortenedUrl;
import it.w0rd.api.ShortenerService;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class RoutingFilterTest {

    ShortenerService shortenerService;
    RoutingFilter routingFilter;
    FilterChain filterChain;

    @Before
    public void setUp() throws IOException, ServletException {
        shortenerService = mock(ShortenerService.class);
        routingFilter = new RoutingFilter(shortenerService);
        filterChain = mock(FilterChain.class);
    }

    @Test
    public void shouldPassApiCalls() throws Throwable {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/all");
        MockHttpServletResponse response = new MockHttpServletResponse();

        routingFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, times(1))
                .doFilter(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));
    }

    @Test
    public void shouldPassAdminRootProject() throws Throwable {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/admin");
        MockHttpServletResponse response = new MockHttpServletResponse();

        routingFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, times(1))
                .doFilter(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));
    }

    @Test
    public void shouldPassAdminResourcesProject() throws Throwable {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/static/admin/index.html");
        MockHttpServletResponse response = new MockHttpServletResponse();

        routingFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, times(1))
                .doFilter(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));
    }

    @Test
    public void shouldRedirectHash() throws Throwable {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/wave");
        MockHttpServletResponse response = new MockHttpServletResponse();

        Mockito.when(shortenerService.expand("wave")).thenReturn(new ShortenedUrl(new URI("http://www.test.com"), "wave", "Description"));

        routingFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, times(0))
                .doFilter(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));

        assertThat(response.getStatus(),equalTo(HttpServletResponse.SC_MOVED_PERMANENTLY));
    }

    @Test
    public void shouldRedirectHashCaseInsensitive() throws Throwable {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/wAvE");
        MockHttpServletResponse response = new MockHttpServletResponse();

        Mockito.when(shortenerService.expand("wave")).thenReturn(new ShortenedUrl(new URI("http://www.test.com"), "wave", "Description"));

        routingFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, times(0))
                .doFilter(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));

        assertThat(response.getStatus(),equalTo(HttpServletResponse.SC_MOVED_PERMANENTLY));
    }

}