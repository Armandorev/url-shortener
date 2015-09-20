package benjamin.groehbiel.ch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class HttpsFilter implements Filter {

    @Value("${app.protocol}")
    private String protocol;

    private boolean enforceHttps = false;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        HttpServletRequest servletRequest = (HttpServletRequest) request;

        if (isNotHttps(servletRequest) && ("https".equals(protocol) || enforceHttps)) {
            servletResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            servletResponse.setHeader("Location", replaceProtocolToHttps(servletRequest));
        } else {
            chain.doFilter(request, servletResponse);
        }
    }

    private String replaceProtocolToHttps(HttpServletRequest httpRequest) {
        return "https://" + httpRequest.getHeader("host") + httpRequest.getRequestURI();
    }

    private boolean isNotHttps(HttpServletRequest servletRequest) {
        return !"https".equals(servletRequest.getProtocol());
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
