package it.w0rd;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UriValidatorTest {

    // TODO: Custom matcher to enable parameterizing tests

    UriValidator uriValidator = new UriValidator();

    @Test
    public void testValidUriPasses() {
        assertThat(uriValidator.validate("http://www.google.com"), equalTo(true));
    }

    @Test
    public void testInvalidUriFails() {
        assertThat(uriValidator.validate("htp://www.google.com"), equalTo(false));
    }

    @Test
    public void testCaseInProtocol() {
        assertThat(uriValidator.validate("Http://www.google.com"), equalTo(true));
        assertThat(uriValidator.validate("HttP://www.google.com"), equalTo(true));
        assertThat(uriValidator.validate("HTTPS://www.google.com"), equalTo(true));
    }

    @Test
    public void testLocalhostUriFails() {
        assertThat(uriValidator.validate("http://localhost:8000"), equalTo(false));
    }

    @Test
    public void testValidUriContainingLocalhostPasses() {
        assertThat(uriValidator.validate("http://google.com/localhost"), equalTo(true));
    }

    @Test
    public void testNatUriFails() {
        assertThat(uriValidator.validate("http://192.168.0.2"), equalTo(false));
    }

    @Test
    public void testAllZeroUriFails() {
        assertThat(uriValidator.validate("http://0.0.0.0"), equalTo(false));
    }

    @Test
    public void test127UriFails() {
        assertThat(uriValidator.validate("http://127.0.0.1"), equalTo(false));
    }

    @Test
    public void testStupidProtocolFails() {
        assertThat(uriValidator.validate("stupid://google.com"), equalTo(false));
    }

}
