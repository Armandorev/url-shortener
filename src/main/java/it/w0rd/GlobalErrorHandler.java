package it.w0rd;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(HashNotFoundException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public ModelAndView hashNotFound(HashNotFoundException exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404.html");
        modelAndView.addObject("requestedHash", exception.getRequestedUrl());
        return modelAndView;
    }

}
