package benjamin.groehbiel.ch;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.logging.Logger;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE - 1)
public class SubdomainFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String subdomain = request.getServerName().split("\\.")[0];

        if ("manage".equals(subdomain)) {
            Logger.getLogger("Subdomain Filter").info("Request URL routed to subdomain " + subdomain);
            Logger.getLogger(getClass().getName()).info("Request for admin panel");
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
