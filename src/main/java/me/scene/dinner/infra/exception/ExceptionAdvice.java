package me.scene.dinner.infra.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {
    String templateMessage = "{}({}) when request \"{}\"";

    @ExceptionHandler
    public String dinnerException(HttpServletRequest request, DinnerException exception) {
        log.warn(templateMessage, "DinnerException", exception.getMessage(), request.getRequestURI());
        return "page/error/dinner";
    }

    @ExceptionHandler
    public String usernameNotFoundException(HttpServletRequest request, UsernameNotFoundException exception) {
        log.warn(templateMessage, "UsernameNotFoundException", exception.getMessage(), request.getRequestURI());
        return "page/error/username_not_found";
    }

    @ExceptionHandler
    public String runtimeException(HttpServletRequest request, RuntimeException exception) {
        log.warn(templateMessage, "RuntimeException", exception.getMessage(), request.getRequestURI());
        return "page/error/runtime";
    }

}
