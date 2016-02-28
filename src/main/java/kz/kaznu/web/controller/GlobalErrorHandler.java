package kz.kaznu.web.controller;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Sanzhar Aubakirov
 * Date: 2/3/16
 */
@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(value = Throwable.class)
    public void defaultErrorHandler(HttpServletRequest request, Exception throwable) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation(throwable.getClass(), ResponseStatus.class) != null)
            throw throwable;

        final Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        final String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");

        throwable.printStackTrace();

    }
}
