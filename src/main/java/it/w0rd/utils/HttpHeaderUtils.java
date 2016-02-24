package it.w0rd.utils;

import org.springframework.util.StringUtils;

public class HttpHeaderUtils {
    public static String extractClientIpFromXForwardedFor(String xForwardedForHeader) {
        if (StringUtils.isEmpty(xForwardedForHeader)) {
            return null;
        }

        return xForwardedForHeader.split(",")[0];
    }
}
