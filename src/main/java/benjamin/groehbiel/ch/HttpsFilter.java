package benjamin.groehbiel.ch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Component
public class HttpsFilter implements Filter {

    @Value("${app.protocol}")
    private String protocol;

    private boolean enforceHttps = false;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        HttpServletRequest servletRequest = (HttpServletRequest) request;

        if (!isHttps(servletRequest) && isHttpsEnforced()) {
            servletResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            String forwardedUri = replaceProtocolToHttps(servletRequest);
            servletResponse.setHeader("Location", forwardedUri);
            Logger.getLogger("HttpsFilter").info("HTTP Request to " + servletRequest.getRequestURI() + " is being forwarded to " + forwardedUri);
        } else {
            chain.doFilter(request, servletResponse);
        }
    }

    private boolean isHttpsEnforced() {
        return "https".equals(protocol) || enforceHttps;
    }

    private String replaceProtocolToHttps(HttpServletRequest httpRequest) {
        return "https://" + httpRequest.getHeader("host") + httpRequest.getRequestURI();
    }

    private boolean isHttps(HttpServletRequest servletRequest) {
        String forwardedProtocol = servletRequest.getHeader("x-forwarded-proto");
        return "https".equals(forwardedProtocol);
    }

    public void setForceHttps(boolean forceHttps) {
        this.enforceHttps = forceHttps;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
