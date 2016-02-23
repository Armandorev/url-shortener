package it.w0rd.filters;

import it.w0rd.HashNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static it.w0rd.filters.RoutingFilter.HTTP_ATTRIBUTE_HASH_NOT_FOUND;

@Component
public class HashInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String hashNotFound = (String) request.getAttribute(HTTP_ATTRIBUTE_HASH_NOT_FOUND);

        if (hashNotFound == null) {
            return true;
        }

        throw new HashNotFoundException(hashNotFound);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}
