package it.w0rd.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class HttpHeaderUtilsTest {

    @Test
    public void extractClientIpFromXForwardedForHeaderReturnsTheOriginatingClientIp() {
        String originatingClientIp = "80.169.144.194";
        String xForwardedForHeader = originatingClientIp + ", 141.101.98.140";

        assertThat(HttpHeaderUtils.extractClientIpFromXForwardedFor(xForwardedForHeader), is(originatingClientIp));
    }

    @Test
    public void extractClientIpFromXForwardedForHeaderReturnsNullWhenTheOriginatingClientIpIsNull() {
        assertThat(HttpHeaderUtils.extractClientIpFromXForwardedFor(null), is(nullValue()));
    }

    @Test
    public void extractClientIpFromXForwardedForHeaderReturnsNullWhenTheOriginatingClientIpIsEmpty() {
        assertThat(HttpHeaderUtils.extractClientIpFromXForwardedFor(""), is(nullValue()));
    }

}