package it.w0rd.filters;

import it.w0rd.persistence.redis.RedisManager;
import it.w0rd.utils.HttpHeaderUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RateLimitingFilter implements Filter {
    public static final int MAX_DAILY_REQUESTS_PER_CLIENT = 100;
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RateLimitingFilter.class);

    @Autowired
    private RedisManager redisManager;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String xForwardedForHeader = ((HttpServletRequest) request).getHeader(X_FORWARDED_FOR);

        String clientIp = HttpHeaderUtils.extractClientIpFromXForwardedFor(xForwardedForHeader);

        long current24hRequestCount = redisManager.incrementRate(clientIp);
        if (current24hRequestCount <= MAX_DAILY_REQUESTS_PER_CLIENT) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            LOGGER.info("Client {} exceeded total daily request limit", clientIp);
        }
    }

    @Override
    public void destroy() {

    }
}
