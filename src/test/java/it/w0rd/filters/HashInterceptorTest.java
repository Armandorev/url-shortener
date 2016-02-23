package it.w0rd.filters;

import it.w0rd.HashNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static it.w0rd.filters.RoutingFilter.HTTP_ATTRIBUTE_HASH_NOT_FOUND;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class HashInterceptorTest {

    HashInterceptor hashInterceptor;

    @Before
    public void setup() {
        hashInterceptor = new HashInterceptor();
    }

    @Test(expected = HashNotFoundException.class)
    public void shouldThrowExceptionForErrorHandling() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/wave");
        request.setAttribute(HTTP_ATTRIBUTE_HASH_NOT_FOUND, "wave");

        hashInterceptor.preHandle(request, null, null);
    }

    @Test
    public void shouldNotInterceptRequest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/wave");

        boolean isIntercepting = hashInterceptor.preHandle(request, null, null);

        assertThat(isIntercepting, equalTo(true));
    }


}
