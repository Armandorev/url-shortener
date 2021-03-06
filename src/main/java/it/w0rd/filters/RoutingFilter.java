package it.w0rd.filters;

import it.w0rd.api.ShortenedUrl;
import it.w0rd.api.ShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE - 3)
public class RoutingFilter implements Filter {

    public static final String HTTP_ATTRIBUTE_HASH_NOT_FOUND = "hashNotFound";

    @Autowired
    ShortenerService shortenerService;

    public RoutingFilter() {}

    public RoutingFilter(ShortenerService shortenerService) {
        this.shortenerService = shortenerService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        boolean isWebAppFileOrApiCall = requestURI.matches("(^$|\\/$|.*\\.(html|js|css|ico)|\\/api\\/.*|\\/admin(\\/.*)*)");
        if (isWebAppFileOrApiCall) {
            Logger.getLogger("RedirectFilter").info("Request is file or api call " + requestURI);
            chain.doFilter(request, response);
        } else {
            String hash = requestURI.substring(1);
            Logger.getLogger("RedirectFilter").info("Resolving hash " + hash);
            try {
                ShortenedUrl shortenedUrl = shortenerService.expand(hash);
                httpResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                httpResponse.setHeader("Location", shortenedUrl.getOriginalURI().toString());
                Logger.getLogger("RedirectFilter").info("Redirect to url for hash: " + hash);
            } catch (Exception ex) {
                httpRequest.setAttribute(HTTP_ATTRIBUTE_HASH_NOT_FOUND, hash);
                chain.doFilter(httpRequest, httpResponse);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
