package it.w0rd.filters;

import it.w0rd.api.auth.AdminAuthenticationCredentialsError;
import it.w0rd.api.auth.AuthenticationProvider;
import it.w0rd.api.auth.SingleAdminAuthentication;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE - 2)
public class AdminAuthFilter implements Filter {

    private final AuthenticationProvider authenticationProvider;
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AdminAuthFilter.class);

    public AdminAuthFilter() throws AdminAuthenticationCredentialsError {
        String username = System.getProperty("admin.username");
        String password = System.getProperty("admin.password");

        authenticationProvider = new SingleAdminAuthentication(username, password);
    }

    public AdminAuthFilter(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        final String requestURI = httpRequest.getRequestURI();
        if(!requestURI.matches("/admin/.*") && !requestURI.matches("/api/admin.*")){
            LOGGER.info("Request does need not be authorized: " + httpRequest.getRequestURI());
            chain.doFilter(request, response);
            return;
        }

        final String authKey = httpRequest.getHeader("Authorization");
        if(authKey != null && authKey.startsWith("Basic ")) {
            final String[] authInfo = new String(Base64.getDecoder().decode(authKey.substring(6))).split(":");
            if (authInfo.length < 2) {
                challenge(httpResponse);
                return;
            }

            final String userName = authInfo[0];
            final String password = authInfo[1];

            if(authenticationProvider.isAdministrator(userName, password)) {
                LOGGER.info("User " + userName + " has successfully logged in");
                chain.doFilter(request, response);
                return;
            }
        }

        challenge(httpResponse);
    }

    private void challenge(HttpServletResponse httpResponse) {
        LOGGER.info("No or wrong credentials");
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.setHeader("WWW-Authenticate", "Basic");
    }

    @Override
    public void destroy() {

    }
}
