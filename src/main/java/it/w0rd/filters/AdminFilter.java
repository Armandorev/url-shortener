package it.w0rd.filters;

import it.w0rd.api.auth.AdminAuthenticationCredentialsError;
import it.w0rd.api.auth.AuthenticationProvider;
import it.w0rd.api.auth.SingleAdminAuthentication;
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
public class AdminFilter implements Filter {

    private final AuthenticationProvider authenticationProvider;

    public AdminFilter() throws AdminAuthenticationCredentialsError {
        authenticationProvider = new SingleAdminAuthentication("admin", "default");
    }

    public AdminFilter(AuthenticationProvider authenticationProvider) {
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
            chain.doFilter(request, response);
            return;
        }

        final String authKey = httpRequest.getHeader("Authorization");
        if(authKey != null && authKey.startsWith("Basic ")) {
            final String[] authInfo = new String(Base64.getDecoder().decode(authKey.substring(6))).split(":");
            final String userName = authInfo[0];
            final String password = authInfo[1];
            if(authenticationProvider.isAdministrator(userName, password)) {
                chain.doFilter(request, response);
                return;
            }
        }
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.setHeader("WWW-Authenticate", "Basic");
    }

    @Override
    public void destroy() {

    }
}
