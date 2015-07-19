package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.ShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

@Component
public class RedirectFilter implements Filter {

    @Autowired
    ShortenerService shortenerService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        boolean isWebAppFileOrApiCall = requestURI.matches("(^$|\\/$|.*\\.(html|js|css|ico)|\\/api\\/.*)");
        if (isWebAppFileOrApiCall) {
            Logger.getLogger("RedirectFilter").info("Request is file or api call " + requestURI);
            chain.doFilter(request, response);
        } else {
            String hash = requestURI.substring(1);
            Logger.getLogger("RedirectFilter").info("Resolving hash r" + hash);
            try {
                URI originalUri = shortenerService.expand(hash);
                httpResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                httpResponse.setHeader("Location", originalUri.toString());
            } catch (URISyntaxException e) {
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
