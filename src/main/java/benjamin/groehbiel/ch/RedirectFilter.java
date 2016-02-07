package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.ShortenerHandle;
import benjamin.groehbiel.ch.shortener.ShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE - 2)
public class RedirectFilter implements Filter {

    @Autowired
    ShortenerService shortenerService;

    public RedirectFilter() {
    }

    public RedirectFilter(ShortenerService shortenerService) {
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
                ShortenerHandle shortenerHandle = shortenerService.expand(hash);
                httpResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                httpResponse.setHeader("Location", shortenerHandle.getOriginalURI().toString());
            } catch (Exception ex) {
                httpResponse.sendRedirect("404.html?hash=" + hash);
                httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
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
