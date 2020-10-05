package me.scene.dinner.infra.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {
    String templateMessage = "{}({}) when request \"{}\"";

    @ExceptionHandler
    public String usernameNotFoundException(HttpServletRequest req, UsernameNotFoundException e) {
        log.warn(templateMessage, "UsernameNotFoundException", e.getMessage(), req.getRequestURI());
        return "page/error/username_not_found";
    }

    @ExceptionHandler
    public String verificationException(HttpServletRequest req, VerificationException e) {
        log.warn(templateMessage, "VerificationException", e.getMessage(), req.getRequestURI());
        return "page/error/verification";
    }

    @ExceptionHandler
    public String messagingException(HttpServletRequest req, MessagingException e) {
        log.warn(templateMessage, "MessagingException", e.getMessage(), req.getRequestURI());
        return "page/error/messaging";
    }

    @ExceptionHandler
    public String dinnerException(HttpServletRequest req, DinnerException e) {
        log.error(templateMessage, "DinnerException", e.getMessage(), req.getRequestURI());
        return "page/error/dinner_exception";
    }

    @ExceptionHandler
    public String runtimeException(HttpServletRequest req, RuntimeException e) {
        log.error(templateMessage, "RuntimeException", e.getMessage(), req.getRequestURI());
        return "page/error/unknown";
    }

    @ExceptionHandler
    public String exception(HttpServletRequest req, Exception e) {
        log.error(templateMessage, "Exception", e.getMessage(), req.getRequestURI());
        return "page/error/unknown";
    }

}
